package id.reishandy.fueltracker.data

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.edit

@Singleton
class FuelTrackerPreferenceManager @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    companion object {
        private const val KEY_LOCALE = "key_locale"
        private const val KEY_USER_NAME = "key_user_name"
        private const val KEY_USER_EMAIL = "key_user_email"
        private const val KEY_USER_PHOTO = "key_user_photo"
    }

    fun getLocale(): String? {
        return sharedPreferences.getString(KEY_LOCALE, null)
    }

    fun setLocale(locale: String) {
        sharedPreferences.edit { putString(KEY_LOCALE, locale) }
    }

    fun saveUser(name: String?, email: String?, photoUrl: String?) {
        sharedPreferences.edit {
            putString(KEY_USER_NAME, name)
            putString(KEY_USER_EMAIL, email)
            putString(KEY_USER_PHOTO, photoUrl)
        }
    }

    fun getUser(): Triple<String?, String?, String?> {
        return Triple(
            sharedPreferences.getString(KEY_USER_NAME, null),
            sharedPreferences.getString(KEY_USER_EMAIL, null),
            sharedPreferences.getString(KEY_USER_PHOTO, null)
        )
    }

    fun clearUser() {
        sharedPreferences.edit {
            remove(KEY_USER_NAME)
            remove(KEY_USER_EMAIL)
            remove(KEY_USER_PHOTO)
        }
    }
}