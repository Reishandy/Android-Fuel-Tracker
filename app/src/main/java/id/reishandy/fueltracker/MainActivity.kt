package id.reishandy.fueltracker

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import id.reishandy.fueltracker.ui.FuelTracker
import id.reishandy.fueltracker.ui.theme.FuelTrackerTheme
import id.reishandy.fueltracker.util.LocaleHelper
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun attachBaseContext(base: Context) {
        val sharedPreferences = base.getSharedPreferences("fuel_tracker_prefs", Context.MODE_PRIVATE)
        val localeTag = sharedPreferences.getString("key_locale", null) ?: Locale.getDefault().toLanguageTag()
        val locale = Locale.forLanguageTag(localeTag)
        val context = LocaleHelper.updateLocale(base, locale)
        super.attachBaseContext(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FuelTrackerTheme {
                FuelTracker()
            }
        }
    }
}