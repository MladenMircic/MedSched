package rs.ac.bg.etf.diplomski.medsched.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.work.ExistingWorkPolicy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import rs.ac.bg.etf.diplomski.medsched.commons.Constants
import rs.ac.bg.etf.diplomski.medsched.commons.PreferenceKeys
import rs.ac.bg.etf.diplomski.medsched.domain.background.AutoLogoutWorker
import rs.ac.bg.etf.diplomski.medsched.domain.background.BackgroundTaskDispatcher
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class RootViewModel @Inject constructor(
    dataStore: DataStore<Preferences>,
    private val backgroundTaskDispatcher: BackgroundTaskDispatcher
): ViewModel() {

    var loggedIn by mutableStateOf(false)

    val tokenFlow = dataStore.data.map { preferences ->
        preferences[PreferenceKeys.USER_TOKEN_KEY]
    }

    fun triggerAutoLogout() {
        loggedIn = true
        backgroundTaskDispatcher.doDelayedBackgroundTask<AutoLogoutWorker>(
            Constants.AUTO_LOGOUT_TASK_NAME,
            ExistingWorkPolicy.REPLACE,
            30,
            TimeUnit.MINUTES
        )
    }
}