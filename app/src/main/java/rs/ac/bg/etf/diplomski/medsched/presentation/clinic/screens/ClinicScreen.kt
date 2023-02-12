package rs.ac.bg.etf.diplomski.medsched.presentation.clinic.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import rs.ac.bg.etf.diplomski.medsched.presentation.clinic.ClinicDoctorEdit
import rs.ac.bg.etf.diplomski.medsched.presentation.clinic.ClinicHome
import rs.ac.bg.etf.diplomski.medsched.presentation.clinic.clinicRoutes
import rs.ac.bg.etf.diplomski.medsched.presentation.composables.CustomBottomBar

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ClinicScreen(
    navController: NavHostController = rememberAnimatedNavController()
) {
    Scaffold(
        bottomBar = {
            CustomBottomBar(
                destinations = clinicRoutes,
                navController = navController
            )
        },
        backgroundColor = MaterialTheme.colors.primary
    ) {
        AnimatedNavHost(
            navController = navController,
            startDestination = ClinicHome.route,
            modifier = Modifier.padding(it)
        ) {
            composable(
                route = ClinicHome.route,
            ) {
                ClinicHomeScreen()
            }
            composable(route = ClinicDoctorEdit.route) {
                ClinicAddDoctorScreen()
            }
        }
    }
}