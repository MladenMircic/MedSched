package rs.ac.bg.etf.diplomski.medsched.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import rs.ac.bg.etf.diplomski.medsched.commons.PreferenceKeys
import rs.ac.bg.etf.diplomski.medsched.data.remote.PatientApi
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Service
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.User
import rs.ac.bg.etf.diplomski.medsched.domain.repository.PatientRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PatientRepositoryImpl @Inject constructor(
    dataStore: DataStore<Preferences>,
    private val moshi: Moshi,
    private val patientApi: PatientApi
) : PatientRepository {

    override val user: Flow<User?> = dataStore.data.map { preferences ->
        moshi.adapter(User::class.java).fromJson(
            preferences[PreferenceKeys.USER_INFO_KEY] ?: ""
        )
    }

    override suspend fun getAllServices(): List<Service> =
        patientApi.getAllServices().map { it.toService() }
}