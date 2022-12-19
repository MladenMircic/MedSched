package rs.ac.bg.etf.diplomski.medsched.presentation.graphs

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import rs.ac.bg.etf.diplomski.medsched.presentation.login_register.LoginRegisterDestinations
import rs.ac.bg.etf.diplomski.medsched.presentation.login_register.screens.LoginScreen
import rs.ac.bg.etf.diplomski.medsched.presentation.login_register.screens.RegisterScreen


@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.authenticationNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.AUTHENTICATION,
        startDestination = LoginRegisterDestinations.LoginDestination.route
    ) {
        composable(route = LoginRegisterDestinations.LoginDestination.route) {
            LoginScreen(
                onGoToRegister = {
                    navController.navigate(LoginRegisterDestinations.RegisterDestination.route)
                }
            )
        }
        composable(route = LoginRegisterDestinations.RegisterDestination.route) {
            RegisterScreen(
                onBackToLogin = {
                    navController.popBackStack()
                }
            )
        }
    }
}