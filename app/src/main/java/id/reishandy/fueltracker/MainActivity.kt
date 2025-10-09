package id.reishandy.fueltracker

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint
import id.reishandy.fueltracker.data.FuelTrackerPreferenceManager
import id.reishandy.fueltracker.data.sync.DataSyncService
import id.reishandy.fueltracker.data.sync.SyncObserver
import id.reishandy.fueltracker.ui.FuelTracker
import id.reishandy.fueltracker.ui.theme.FuelTrackerTheme
import id.reishandy.fueltracker.util.LocaleHelper
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var preferenceManager: FuelTrackerPreferenceManager
    @Inject
    lateinit var syncObserver: SyncObserver
    @Inject
    lateinit var dataSyncService: DataSyncService

    override fun attachBaseContext(base: Context) {
        val sharedPreferences =
            base.getSharedPreferences("fuel_tracker_prefs", MODE_PRIVATE)
        val localeTag =
            sharedPreferences.getString("key_locale", null) ?: Locale.getDefault().toLanguageTag()
        val locale = Locale.forLanguageTag(localeTag)
        val context = LocaleHelper.updateLocale(base, locale)
        super.attachBaseContext(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FuelTrackerTheme {
                AutoSyncStarter(
                    preferenceManager = preferenceManager,
                    syncObserver = syncObserver,
                    dataSyncService = dataSyncService
                )
                FuelTracker()
            }
        }
    }
}

@Composable
fun AutoSyncStarter(
    preferenceManager: FuelTrackerPreferenceManager,
    syncObserver: SyncObserver,
    dataSyncService: DataSyncService
) {
    LaunchedEffect(Unit) {
        val (name, email, _) = preferenceManager.getUser()
        val isUserSignedInPrefs = !name.isNullOrBlank() && !email.isNullOrBlank()
        val isUserSignedInFirebase = Firebase.auth.currentUser != null

        if (isUserSignedInPrefs && isUserSignedInFirebase) {
            try {
                dataSyncService.syncFromCloud()
                syncObserver.startSyncing()
            } catch (e: Exception) {
                Log.e("AutoSyncStarter", "Failed to start sync", e)
            }
        }
    }
}