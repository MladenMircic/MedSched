package rs.ac.bg.etf.diplomski.medsched.domain.background

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.authentication.LogoutUseCase

@HiltWorker
class AutoLogoutWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val logoutUseCase: LogoutUseCase
): CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        logoutUseCase()
        return Result.success()
    }
}