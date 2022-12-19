package rs.ac.bg.etf.diplomski.medsched

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint
import rs.ac.bg.etf.diplomski.medsched.presentation.graphs.RootNavigationGraph
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.MedSchedTheme

@OptIn(ExperimentalAnimationApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MedSchedTheme {
                RootNavigationGraph(navController = rememberAnimatedNavController())
            }
        }
    }
}