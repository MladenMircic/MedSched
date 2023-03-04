package rs.ac.bg.etf.diplomski.medsched.presentation.graphs

import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Doctor
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Patient
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.User
import rs.ac.bg.etf.diplomski.medsched.presentation.RootViewModel
import rs.ac.bg.etf.diplomski.medsched.presentation.login_register.Authentication
import rs.ac.bg.etf.diplomski.medsched.presentation.login_register.Login
import rs.ac.bg.etf.diplomski.medsched.presentation.login_register.Register
import rs.ac.bg.etf.diplomski.medsched.presentation.login_register.screens.LoginScreen
import rs.ac.bg.etf.diplomski.medsched.presentation.login_register.screens.RegisterScreen
import rs.ac.bg.etf.diplomski.medsched.presentation.login_register.screens.SplashScreen


@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.authenticationNavGraph(
    navController: NavHostController,
    rootViewModel: RootViewModel
) {
    navigation(
        route = Graph.AUTHENTICATION,
        startDestination = Authentication.route
    ) {
        composable(route = Authentication.route) {
            SplashScreen(
                onJumpDestination = { route ->
                    navController.navigateWithPopInclusive(
                        toRoute = route,
                        popToRoute = Authentication.route,
                        launchSingle = true
                    )
                }
            )
        }
        composable(route = Login.route) {
            val user: User? by rootViewModel.userFlow.collectAsState(initial = null)

            LoginScreen(
                onLoginSuccess = {
                    navController.navigateWithPopInclusive(
                        toRoute = when (user) {
                            is Patient -> Graph.PATIENT
                            is Doctor -> Graph.DOCTOR
                            else -> Graph.CLINIC
                        },
                        popToRoute = Login.route,
                        launchSingle = true
                    )
                },
                onGoToRegister = {
                    navController.navigateWithPopInclusive(
                        toRoute = Register.route,
                        popToRoute = Login.route,
                    )
                }
            )
        }
        composable(route = Register.route) {
            RegisterScreen(
                onBackToLogin = {
                    navController.navigateWithPopInclusive(
                        toRoute = Login.route,
                        popToRoute = Register.route,
                    )
                }
            )

            BackHandler(enabled = true) {
                navController.navigateWithPopInclusive(
                    toRoute = Login.route,
                    popToRoute = Register.route,
                )
            }
        }
    }
}

fun NavHostController.navigateWithPopInclusive(
    toRoute: String,
    popToRoute: String,
    launchSingle: Boolean = false
) {
    navigate(toRoute) {
        popUpTo(popToRoute) {
            inclusive = true
        }
        launchSingleTop = launchSingle
    }
}