package rs.ac.bg.etf.diplomski.medsched.domain.use_case.patient

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.squareup.moshi.Moshi
import rs.ac.bg.etf.diplomski.medsched.commons.PreferenceKeys.Companion.APPOINTMENT_FETCH_KEY
import rs.ac.bg.etf.diplomski.medsched.di.json_adapters.fromList
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Appointment
import javax.inject.Inject

class SaveAppointmentsUseCase @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val moshi: Moshi
) {

    suspend operator fun invoke(appointments: List<Appointment>) {
        dataStore.edit { preferences ->
            preferences[APPOINTMENT_FETCH_KEY] = moshi.fromList(appointments)
        }
    }
}