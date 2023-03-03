package rs.ac.bg.etf.diplomski.medsched.domain.use_case.authentication

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.onesignal.OneSignal
import rs.ac.bg.etf.diplomski.medsched.commons.PreferenceKeys
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    suspend operator fun invoke() {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.USER_TOKEN_KEY] = ""
        }
        OneSignal.removeExternalUserId()
    }
}