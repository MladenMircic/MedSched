package rs.ac.bg.etf.diplomski.medsched

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.*
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.work.WorkManager
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint
import rs.ac.bg.etf.diplomski.medsched.presentation.RootViewModel
import rs.ac.bg.etf.diplomski.medsched.presentation.graphs.RootNavigationGraph
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.MedSchedTheme
import javax.inject.Inject

@OptIn(ExperimentalAnimationApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var workManager: WorkManager
    lateinit var rootViewModel: RootViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MedSchedTheme {
                rootViewModel = hiltViewModel()
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