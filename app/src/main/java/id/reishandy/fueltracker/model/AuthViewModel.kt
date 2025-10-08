package id.reishandy.fueltracker.model

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import id.reishandy.fueltracker.data.FuelTrackerAppDatabase
import id.reishandy.fueltracker.data.FuelTrackerPreferenceManager
import id.reishandy.fueltracker.util.generateSecureRandomNonce
import id.reishandy.fueltracker.util.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import id.reishandy.fueltracker.BuildConfig
import id.reishandy.fueltracker.data.sync.DataSyncService
import id.reishandy.fueltracker.data.sync.SyncObserver

data class AuthState(
    val name: String? = null,
    val email: String? = null,
    val photoUrl: String? = null,
    val isProcessing: Boolean = false
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val credentialManager: CredentialManager,
    private val preferenceManager: FuelTrackerPreferenceManager,
    private val dataSyncService: DataSyncService,
    private val syncObserver: SyncObserver
): ViewModel() {
    private val _uiState = MutableStateFlow(AuthState())
    val uiState: StateFlow<AuthState> = _uiState.asStateFlow()

    init {
        val (name, email, photoUrl) = preferenceManager.getUser()
        _uiState.value = AuthState(name, email, photoUrl)
    }

    fun setProcessing(isProcessing: Boolean) {
        _uiState.value = _uiState.value.copy(isProcessing = isProcessing)
    }

    fun createGoogleSignInOption(filterByAuthorized: Boolean = true): GetGoogleIdOption {
        return GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(filterByAuthorized)
            .setServerClientId(BuildConfig.GOOGLE_CLIENT_ID)
            .setAutoSelectEnabled(true)
            .setNonce(generateSecureRandomNonce())
            .build()
    }

    fun createSignInRequest(googleIdOption: GetGoogleIdOption): GetCredentialRequest {
        return GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
    }

    fun signInWithGoogle(context: Context, request: GetCredentialRequest) {
        viewModelScope.launch {
            setProcessing(true)
            try {
                val response = credentialManager.getCredential(context, request)
                handleCredentialResponse(context, response)
            } catch (e: Exception) {
                // e is often GetCredentialException or subclass
                // fallback if it's "no credential found" or something
                handleSignInException(context, e)
            } finally {
                setProcessing(false)
            }
        }
    }

    private fun handleCredentialResponse(context: Context, response: GetCredentialResponse) {
        val credential = response.credential

        Log.d("AuthViewModel", "Credential type: ${credential.type}")

        if (credential.type == com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            try {
                val googleIdTokenCredential = com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.createFrom(credential.data)
                val idToken = googleIdTokenCredential.idToken

                val name = googleIdTokenCredential.displayName
                val email = googleIdTokenCredential.id
                val photo = googleIdTokenCredential.profilePictureUri?.toString()

                Log.d("AuthViewModel", "Signed in as $name ($email)")

                preferenceManager.saveUser(name, email, photo)
                _uiState.value = AuthState(name, email, photo)

                val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                Firebase.auth.signInWithCredential(firebaseCredential)
                    .addOnSuccessListener { authResult ->
                        viewModelScope.launch {
                            // On success, sync data from cloud
                            try {
                                dataSyncService.syncFromCloud()
                                syncObserver.startSyncing()
                                showToast(context, "Data restored from cloud", false)
                            } catch (e: Exception) {
                                Log.e("AuthViewModel", "Sync failed", e)
                                showToast(context, "Sync failed: ${e.message}", true)
                            }
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.e("AuthViewModel", "Firebase sign-in failed", exception)
                        showToast(context, "Sync failed: ${exception.message}", true)
                    }

                showToast(context, "Welcome $name", false)
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Failed to parse GoogleIdTokenCredential", e)
                showToast(context, "Sign-in failed: ${e.message}", true)
            }
        } else {
            showToast(context, "Unsupported credential type: ${credential.type}", true)
            Log.e("AuthViewModel", "Unsupported credential type: ${credential.type}")
        }
    }

    private suspend fun handleSignInException(
        context: Context,
        e: Exception
    ) {
        // If the exception indicates "no credential" (user has never signed in before),
        // fallback by issuing the same request but with filterByAuthorized = false
        val className = e::class.simpleName ?: ""
        if (className.contains("GetCredentialException") ||
            className.contains("NoCredentialException")
        ) {
            // fallback path
            val fallbackOption = createGoogleSignInOption(filterByAuthorized = false)
            val fallbackReq = createSignInRequest(fallbackOption)
            try {
                val resp2 = credentialManager.getCredential(context, fallbackReq)
                handleCredentialResponse(context, resp2)
            } catch (e2: Exception) {
                showToast(context, "Sign-in failed: ${e2.message}", true)
                Log.e("AuthViewModel", "Sign-in failed", e2)
            }
        } else {
            showToast(context, "Sign-in failed: ${e.message}", true)
            Log.e("AuthViewModel", "Sign-in failed", e)
        }
    }

    fun signOut(context: Context) {
        // Clear credential state so that the system doesn't auto-select the previous credential
        viewModelScope.launch {
            setProcessing(true)

            try {
                val clearReq = androidx.credentials.ClearCredentialStateRequest(
                    androidx.credentials.ClearCredentialStateRequest.TYPE_CLEAR_RESTORE_CREDENTIAL
                )

                // Sign out
                credentialManager.clearCredentialState(clearReq)

                withContext(Dispatchers.IO) {
                    // Clear user data from preferences
                    _uiState.value = AuthState()
                    preferenceManager.clearUser()
                    // Stop sync observer
                    syncObserver.stopSyncing()
                    // Sign out from Firebase
                    Firebase.auth.signOut()
                }

                showToast(context, "Signed out")
            } catch (ex: Exception) {
                showToast(context, "Sign-out failed: ${ex.message}", true)
                Log.e("AuthViewModel", "Sign-out failed", ex)
            } finally {
                setProcessing(false)
            }
        }
    }
}