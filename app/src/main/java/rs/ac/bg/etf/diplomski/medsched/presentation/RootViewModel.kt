package rs.ac.bg.etf.diplomski.medsched.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import rs.ac.bg.etf.diplomski.medsched.commons.Constants.DOCTOR_APPOINTMENT_FETCH_TASK_NAME
import rs.ac.bg.etf.diplomski.medsched.commons.Constants.PATIENT_APPOINTMENT_FETCH_TASK_NAME
import rs.ac.bg.etf.diplomski.medsched.commons.PreferenceKeys
import rs.ac.bg.etf.diplomski.medsched.domain.background.AppointmentAvailabilityCheckWorker
import rs.ac.bg.etf.diplomski.medsched.domain.background.AppointmentFetchAndCheckDoctorWorker
import rs.ac.bg.etf.diplomski.medsched.domain.background.BackgroundTaskDispatcher
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Doctor
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Patient
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.GetUserUseCase
import java.time.Duration
import javax.inject.Inject

@HiltViewModel
class RootViewModel @Inject constructor(
    getUserUseCase: GetUserUseCase,
    dataStore: DataStore<Preferences>,
    private val backgroundTaskDispatcher: BackgroundTaskDispatcher
): ViewModel() {

    var loggedIn by mutableStateOf(false)

    val userFlow = getUserUseCase.userFlow
    val tokenFlow = dataStore.data.map { preferences ->
        preferences[PreferenceKeys.USER_TOKEN_KEY]
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun triggerPeriodicSyncIfPatient() = viewModelScope.launch {
        userFlow.collect { user ->
            if (user is Patient) {
                backgroundTaskDispatcher
                    .doPeriodicBackgroundTaskWithConstraints<AppointmentAvailabilityCheckWorker>(
                        duration = Duration.ofMinutes(15),
                        constraints = Constraints.Builder()
                            .setRequiredNetworkType(NetworkType.CONNECTED)
                            .build(),
                        workName = PATIENT_APPOINTMENT_FETCH_TASK_NAME,
                        workPolicy = ExistingPeriodicWorkPolicy.REPLACE
                    )
            } else if (user is Doctor) {
                backgroundTaskDispatcher
                    .doPeriodicBackgroundTaskWithConstraints<AppointmentFetchAndCheckDoctorWorker>(
                        duration = Duration.ofMinutes(15),
                        constraints = Constraints.Builder()
                            .setRequiredNetworkType(NetworkType.CONNECTED)
                            .build(),
                        workName = DOCTOR_APPOINTMENT_FETCH_TASK_NAME,
                        workPolicy = ExistingPeriodicWorkPolicy.REPLACE
                    )
            }
        }
    }
}