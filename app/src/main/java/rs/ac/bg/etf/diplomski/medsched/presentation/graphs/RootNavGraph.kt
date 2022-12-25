package rs.ac.bg.etf.diplomski.medsched.presentation.graphs

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import rs.ac.bg.etf.diplomski.medsched.presentation.RootViewModel
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.screens.PatientMainScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RootNavigationGraph(
    navController: NavHostController,
    rootViewModel: RootViewModel = hiltViewModel()
) {

    val lifecycleOwner = LocalLifecycleOwner.current
    val token by rootViewModel.tokenFlow.collectAsState(initial = null)

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    // If user has been auto logged out, and was not in the app
    // when he comes back to app switch to authentication screen
    LaunchedEffect(true) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            if (currentDestination?.hierarchy?.any { it.route == Graph.AUTHENTICATION } == false) {
                rootViewModel.tokenFlow.collect {
                    if (it == "") {
                        navController.navigate(Graph.AUTHENTICATION)
                    }
                }
            }
        }
    }
    LaunchedEffect(key1 = token) {
        token?.let {
            if (currentDestination?.hierarchy?.any { it.route == Graph.AUTHENTICATION } == false
                && token == "") {
                navController.navigate(Graph.AUTHENTICATION)
            }
        }
    }

    AnimatedNavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = Graph.AUTHENTICATION
    ) {
        authenticationNavGraph(navController)
        composable(route = Graph.PATIENT) {
            PatientMainScreen()
        }
    }
}

object Graph {
    const val ROOT = "root_graph"
    const val AUTHENTICATION = "auth_graph"
    const val PATIENT = "patient_graph"
}