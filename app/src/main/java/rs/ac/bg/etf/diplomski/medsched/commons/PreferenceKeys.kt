package rs.ac.bg.etf.diplomski.medsched.commons

import androidx.datastore.preferences.core.stringPreferencesKey

class PreferenceKeys {
    companion object {
        const val USER_TOKEN_PREFERENCE = "UserToken"
        val USER_TOKEN_KEY = stringPreferencesKey("user_token")
    }
}