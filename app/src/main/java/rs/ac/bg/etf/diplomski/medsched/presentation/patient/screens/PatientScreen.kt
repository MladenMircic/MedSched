package rs.ac.bg.etf.diplomski.medsched.presentation.patient.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
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
    var visibleBottomBar by remember { mutableStateOf(true) }
    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = visibleBottomBar,
                enter = slideInVertically(
                    animationSpec = tween(
                        durationMillis = 300,
                        delayMillis = 400
                    ),
                    initialOffsetY = { it }
                ),
                exit = slideOutVertically(
                    animationSpec = tween(
                        durationMillis = 300,
                        delayMillis = 400
                    ),
                    targetOffsetY = { it }
                )
            ) {
                CustomBottomBar(
                    destinations = patientRoutes,
                    navController = navController
                )
            }
        },
        backgroundColor = MaterialTheme.colors.primary
    ) {
        AnimatedNavHost(
            navController = navController,
            startDestination = PatientHome.route,
            modifier = Modifier.padding(it),
            enterTransition = { fadeIn(tween(durationMillis = 1)) },
            exitTransition = { fadeOut(tween(durationMillis = 1)) }
        ) {
            composable(route = PatientHome.route) {
                PatientHomeScreen(
                    toggleBottomBar = {
                        visibleBottomBar = !visibleBottomBar
                    }
                )
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