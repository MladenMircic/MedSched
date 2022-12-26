package rs.ac.bg.etf.diplomski.medsched.presentation.utils

import androidx.work.ExistingWorkPolicy
import androidx.work.ListenableWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BackgroundTaskDispatcher @Inject constructor(
    val workManager: WorkManager
) {

    inline fun <reified T: ListenableWorker> doBackgroundTask(
        workName: String,
        workPolicy: ExistingWorkPolicy
    ) {
        workManager.beginUniqueWork(
            workName,
            workPolicy,
            OneTimeWorkRequestBuilder<T>()
                .build()
        ).enqueue()
    }
    inline fun <reified T: ListenableWorker> doDelayedBackgroundTask(
        workName: String,
        workPolicy: ExistingWorkPolicy,
        delay: Long,
        timeUnit: TimeUnit
    ) {
        workManager.beginUniqueWork(
            workName,
            workPolicy,
            OneTimeWorkRequestBuilder<T>()
                .setInitialDelay(delay, timeUnit)
                .build()
        ).enqueue()
    }
}