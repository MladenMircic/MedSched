package rs.ac.bg.etf.diplomski.medsched.presentation.graphs

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import rs.ac.bg.etf.diplomski.medsched.presentation.RootViewModel
import rs.ac.bg.etf.diplomski.medsched.presentation.clinic.screens.ClinicScreen
import rs.ac.bg.etf.diplomski.medsched.presentation.doctor.screens.DoctorScreen
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.screens.PatientScreen

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RootNavigationGraph(
    navController: NavHostController,
    rootViewModel: RootViewModel = hiltViewModel()
) {
    val token by rootViewModel.tokenFlow.collectAsState(initial = null)

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    // If user has been auto logged out, and was not in the app
    // when he comes back to app switch to authentication screen
    LaunchedEffect(key1 = token) {
        token?.let {
            if (currentDestination?.hierarchy?.any { it.route == Graph.AUTHENTICATION } == false
                && token == "") {
                val stackFirstDestination = currentDestination.hierarchy.first()
                navController.navigate(Graph.AUTHENTICATION) {
                    popUpTo(stackFirstDestination.route ?: Graph.PATIENT) {
                        inclusive = true
                    }
                }
                rootViewModel.loggedIn = false
            }
        }
    }

    AnimatedNavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = Graph.AUTHENTICATION
    ) {
        authenticationNavGraph(
            navController = navController,
            rootViewModel = rootViewModel
        )
        composable(route = Graph.PATIENT) {
            LaunchedEffect(true) {
                rootViewModel.triggerPeriodicSyncIfPatient()
            }
            PatientScreen()
        }
        composable(route = Graph.DOCTOR) {
            DoctorScreen()
        }
        composable(route = Graph.CLINIC) {
            ClinicScreen()
        }
    }
}

object Graph {
    const val ROOT = "root_graph"
    const val AUTHENTICATION = "auth_graph"
    const val PATIENT = "patient_graph"
    const val DOCTOR = "doctor_graph"
    const val CLINIC = "clinic_graph"
}