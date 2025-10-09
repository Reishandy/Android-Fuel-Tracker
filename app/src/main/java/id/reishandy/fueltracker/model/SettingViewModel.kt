package id.reishandy.fueltracker.model

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import id.reishandy.fueltracker.data.FuelTrackerPreferenceManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale
import javax.inject.Inject

data class SettingState(
    val selectedLocale: Locale = Locale.getDefault(),
    val locales: List<Locale> = listOf()
)

@HiltViewModel
class SettingViewModel @Inject constructor(
    val preferenceManager: FuelTrackerPreferenceManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingState())
    val uiState: StateFlow<SettingState> = _uiState.asStateFlow()

    init {
        val locales = Locale.getAvailableLocales().toList()
        val savedLocaleTag = preferenceManager.getLocale()
        val selectedLocale = if (savedLocaleTag != null) {
            Locale.forLanguageTag(savedLocaleTag)
        } else {
            Locale.getDefault()
        }
        _uiState.value = _uiState.value.copy(
            locales = locales,
            selectedLocale = selectedLocale
        )
    }

    fun updateSelectedLocale(locale: Locale, activity: Activity?) {
        if (_uiState.value.selectedLocale == locale) return

        _uiState.value = _uiState.value.copy(selectedLocale = locale)
        preferenceManager.setLocale(locale.toLanguageTag())

        activity?.let { act ->
            act.runOnUiThread {
                val intent = act.intent
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                act.startActivity(intent)
                act.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
        }
    }
}