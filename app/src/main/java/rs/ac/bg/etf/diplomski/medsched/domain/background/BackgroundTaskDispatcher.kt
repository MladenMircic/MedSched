package rs.ac.bg.etf.diplomski.medsched.domain.background

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

    inline fun <reified Worker: ListenableWorker> doBackgroundTask(
        workName: String,
        workPolicy: ExistingWorkPolicy
    ) {
        workManager.beginUniqueWork(
            workName,
            workPolicy,
            OneTimeWorkRequestBuilder<Worker>()
                .build()
        ).enqueue()
    }
    inline fun <reified Worker: ListenableWorker> doDelayedBackgroundTask(
        workName: String,
        workPolicy: ExistingWorkPolicy,
        delay: Long,
        timeUnit: TimeUnit
    ) {
        workManager.beginUniqueWork(
            workName,
            workPolicy,
            OneTimeWorkRequestBuilder<Worker>()
                .setInitialDelay(delay, timeUnit)
                .build()
        ).enqueue()
    }
}