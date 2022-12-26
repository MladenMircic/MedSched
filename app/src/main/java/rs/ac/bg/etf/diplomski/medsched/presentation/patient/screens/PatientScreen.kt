package rs.ac.bg.etf.diplomski.medsched.presentation.patient.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import rs.ac.bg.etf.diplomski.medsched.presentation.composables.CustomBottomBar
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.PatientHome
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.PatientInfo
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.PatientScheduled
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.patientRoutes

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PatientScreen(navController: NavHostController = rememberAnimatedNavController()) {
    Scaffold(
        bottomBar = {
            CustomBottomBar(
                destinations = patientRoutes,
                navController = navController
            )
        },
        backgroundColor = MaterialTheme.colors.primary
    ) {
        AnimatedNavHost(
            navController = navController,
            startDestination = PatientHome.route,
            modifier = Modifier.padding(it)
        ) {
            composable(route = PatientHome.route) {
                PatientHomeScreen()
            }
            composable(route = PatientScheduled.route) {
                Text(text = "RADI2")
            }
            composable(route = PatientInfo.route) {
                Text(text = "RADI3")
            }
        }
    }
}