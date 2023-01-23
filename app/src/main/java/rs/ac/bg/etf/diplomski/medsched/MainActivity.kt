package rs.ac.bg.etf.diplomski.medsched

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.*
import androidx.compose.runtime.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.work.*
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint
import rs.ac.bg.etf.diplomski.medsched.commons.Constants.APPOINTMENT_CANCEL_CHANNEL_DESCRIPTION
import rs.ac.bg.etf.diplomski.medsched.commons.Constants.APPOINTMENT_CANCEL_CHANNEL_ID
import rs.ac.bg.etf.diplomski.medsched.commons.Constants.APPOINTMENT_CANCEL_CHANNEL_NAME
import rs.ac.bg.etf.diplomski.medsched.commons.Constants.APPOINTMENT_FETCH_TASK_NAME
import rs.ac.bg.etf.diplomski.medsched.commons.NotificationUtil
import rs.ac.bg.etf.diplomski.medsched.domain.background.AppointmentFetchWorker
import rs.ac.bg.etf.diplomski.medsched.presentation.RootViewModel
import rs.ac.bg.etf.diplomski.medsched.presentation.graphs.RootNavigationGraph
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.MedSchedTheme
import java.time.Duration
import javax.inject.Inject

@OptIn(ExperimentalAnimationApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var workManager: WorkManager
    lateinit var rootViewModel: RootViewModel

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {}

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        NotificationUtil.createNotificationChannel(
            applicationContext,
            APPOINTMENT_CANCEL_CHANNEL_ID,
            APPOINTMENT_CANCEL_CHANNEL_NAME,
            APPOINTMENT_CANCEL_CHANNEL_DESCRIPTION
        )

        val appointmentFetchWork = PeriodicWorkRequestBuilder<AppointmentFetchWorker>(
            Duration.ofSeconds(5),
        ).setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        ).build()

        workManager.enqueueUniquePeriodicWork(
            APPOINTMENT_FETCH_TASK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            appointmentFetchWork
        )

        setContent {
            MedSchedTheme {
                rootViewModel = viewModel()
                RootNavigationGraph(
                    navController = rememberAnimatedNavController(),
                    rootViewModel = rootViewModel
                )
            }
        }
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        if (rootViewModel.loggedIn) {
            rootViewModel.triggerAutoLogout()
        }
    }
}