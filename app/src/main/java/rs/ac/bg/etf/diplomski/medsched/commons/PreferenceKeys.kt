package rs.ac.bg.etf.diplomski.medsched.commons

import androidx.datastore.preferences.core.stringPreferencesKey

class PreferenceKeys {
    companion object {
        const val USER_TOKEN_PREFERENCE = "UserToken"
        val USER_TOKEN_KEY = stringPreferencesKey("user_token")
        const val USER_INFO_PREFERENCE = "UserInfo"
        val USER_INFO_KEY = stringPreferencesKey("user_info")
        const val APPOINTMENT_FETCH_PREFERENCE = "AppointmentFetch"
        val APPOINTMENT_FETCH_KEY = stringPreferencesKey("appointment_fetch")
    }
}