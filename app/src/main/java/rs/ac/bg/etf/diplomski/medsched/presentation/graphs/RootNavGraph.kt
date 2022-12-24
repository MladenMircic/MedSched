package rs.ac.bg.etf.diplomski.medsched.presentation.graphs

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.screens.PatientMainScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RootNavigationGraph(navController: NavHostController) {
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