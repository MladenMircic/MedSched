package rs.ac.bg.etf.diplomski.medsched.presentation.graphs

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.PatientDestinations
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.screens.PatientMainScreen

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.patientNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.PATIENT,
        startDestination = PatientDestinations.PatientMainScreen.route
    ) {
        composable(route = PatientDestinations.PatientMainScreen.route) {
            PatientMainScreen()
        }
    }
}