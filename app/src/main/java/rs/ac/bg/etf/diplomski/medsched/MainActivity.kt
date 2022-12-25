package rs.ac.bg.etf.diplomski.medsched

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.*
import androidx.compose.runtime.*
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint
import rs.ac.bg.etf.diplomski.medsched.commons.Constants.AUTO_LOGOUT_TASK_NAME
import rs.ac.bg.etf.diplomski.medsched.domain.background.AutoLogoutWorker
import rs.ac.bg.etf.diplomski.medsched.presentation.graphs.RootNavigationGraph
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.MedSchedTheme
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@OptIn(ExperimentalAnimationApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var workManager: WorkManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scheduleAutoLogout(20, TimeUnit.SECONDS)
        setContent {
            MedSchedTheme {
                RootNavigationGraph(navController = rememberAnimatedNavController())
            }
        }
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        scheduleAutoLogout(20, TimeUnit.SECONDS)
    }

    private fun scheduleAutoLogout(delay: Long, timeUnit: TimeUnit) {
        workManager.beginUniqueWork(
            AUTO_LOGOUT_TASK_NAME,
            ExistingWorkPolicy.REPLACE,
            OneTimeWorkRequestBuilder<AutoLogoutWorker>()
                .setInitialDelay(delay, timeUnit)
                .build()
        ).enqueue()
    }
}