package rs.ac.bg.etf.diplomski.medsched.presentation.doctor.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import rs.ac.bg.etf.diplomski.medsched.presentation.composables.CustomBottomBar
import rs.ac.bg.etf.diplomski.medsched.presentation.doctor.DoctorAccount
import rs.ac.bg.etf.diplomski.medsched.presentation.doctor.DoctorHome
import rs.ac.bg.etf.diplomski.medsched.presentation.doctor.doctorRoutes

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DoctorScreen(navController: NavHostController = rememberAnimatedNavController()) {

    Scaffold(
        bottomBar = {
            CustomBottomBar(
                destinations = doctorRoutes,
                navController = navController
            )
        },
        backgroundColor = MaterialTheme.colors.primary
    ) {
        AnimatedNavHost(
            navController = navController,
            startDestination = DoctorHome.route,
            modifier = Modifier.padding(it)
        ) {
            composable(
                route = DoctorHome.route,
                deepLinks = DoctorHome.deepLinks
            ) {
                DoctorHomeScreen()
            }
            composable(route = DoctorAccount.route) {
                DoctorProfileScreen()
            }
        }
    }
}