package rs.ac.bg.etf.diplomski.medsched.presentation.graphs

import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import rs.ac.bg.etf.diplomski.medsched.presentation.login_register.LoginRegisterDestinations
import rs.ac.bg.etf.diplomski.medsched.presentation.login_register.screens.LoginScreen
import rs.ac.bg.etf.diplomski.medsched.presentation.login_register.screens.RegisterScreen
import rs.ac.bg.etf.diplomski.medsched.presentation.login_register.screens.SplashScreen


@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.authenticationNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.AUTHENTICATION,
        startDestination = LoginRegisterDestinations.SplashScreen.route
    ) {
        composable(route = LoginRegisterDestinations.SplashScreen.route) {
            SplashScreen(
                onJumpDestination = { route ->
                    navController.navigateWithPopInclusive(
                        toRoute = route,
                        popToRoute = LoginRegisterDestinations.SplashScreen.route,
                        launchSingle = true
                    )
                }
            )
        }
        composable(route = LoginRegisterDestinations.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigateWithPopInclusive(
                        toRoute = Graph.PATIENT,
                        popToRoute = LoginRegisterDestinations.Login.route,
                        launchSingle = true
                    )
                },
                onGoToRegister = {
                    navController.navigateWithPopInclusive(
                        toRoute = LoginRegisterDestinations.Register.route,
                        popToRoute = LoginRegisterDestinations.Login.route,
                    )
                }
            )
        }
        composable(route = LoginRegisterDestinations.Register.route) {
            RegisterScreen(
                onBackToLogin = {
                    navController.navigateWithPopInclusive(
                        toRoute = LoginRegisterDestinations.Login.route,
                        popToRoute = LoginRegisterDestinations.Register.route,
                    )
                }
            )

            BackHandler(enabled = true) {
                navController.navigateWithPopInclusive(
                    toRoute = LoginRegisterDestinations.Login.route,
                    popToRoute = LoginRegisterDestinations.Register.route,
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