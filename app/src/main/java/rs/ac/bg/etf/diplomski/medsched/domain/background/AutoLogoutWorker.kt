package rs.ac.bg.etf.diplomski.medsched.domain.background

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import rs.ac.bg.etf.diplomski.medsched.commons.PreferenceKeys

@HiltWorker
class AutoLogoutWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val dataStore: DataStore<Preferences>
): CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.USER_TOKEN_KEY] = ""
            preferences[PreferenceKeys.USER_INFO_KEY] = ""
        }
        return Result.success()
    }
}