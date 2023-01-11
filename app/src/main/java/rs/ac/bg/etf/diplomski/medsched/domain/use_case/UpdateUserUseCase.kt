package rs.ac.bg.etf.diplomski.medsched.domain.use_case

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.squareup.moshi.Moshi
import rs.ac.bg.etf.diplomski.medsched.commons.PreferenceKeys
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.User
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val moshi: Moshi
) {

    suspend operator fun invoke(user: User) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.USER_INFO_KEY] = moshi.adapter(User::class.java).toJson(user)
        }
    }
}