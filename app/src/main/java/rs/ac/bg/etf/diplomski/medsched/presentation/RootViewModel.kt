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
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import rs.ac.bg.etf.diplomski.medsched.commons.Constants
import rs.ac.bg.etf.diplomski.medsched.commons.Constants.APPOINTMENT_FETCH_TASK_NAME
import rs.ac.bg.etf.diplomski.medsched.commons.PreferenceKeys
import rs.ac.bg.etf.diplomski.medsched.domain.background.AppointmentAvailabilityCheckWorker
import rs.ac.bg.etf.diplomski.medsched.domain.background.AutoLogoutWorker
import rs.ac.bg.etf.diplomski.medsched.domain.background.BackgroundTaskDispatcher
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Patient
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.GetUserUseCase
import java.time.Duration
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class RootViewModel @Inject constructor(
    getUserUseCase: GetUserUseCase,
    dataStore: DataStore<Preferences>,
    private val backgroundTaskDispatcher: BackgroundTaskDispatcher
): ViewModel() {

    var loggedIn by mutableStateOf(false)

    private val userFlow = getUserUseCase.userFlow
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
                        workName = APPOINTMENT_FETCH_TASK_NAME,
                        workPolicy = ExistingPeriodicWorkPolicy.REPLACE
                    )
            }
        }
    }

    fun triggerAutoLogout() {
        loggedIn = true
        backgroundTaskDispatcher.doDelayedBackgroundTask<AutoLogoutWorker>(
            Constants.AUTO_LOGOUT_TASK_NAME,
            ExistingWorkPolicy.REPLACE,
            1,
            TimeUnit.HOURS
        )
    }
}