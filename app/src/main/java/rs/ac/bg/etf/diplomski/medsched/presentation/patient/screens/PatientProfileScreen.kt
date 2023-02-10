package rs.ac.bg.etf.diplomski.medsched.presentation.patient.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.EmptyUser
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Patient
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.*
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.stateholders.PatientProfileViewModel
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PatientProfileScreen(
    patientProfileViewModel: PatientProfileViewModel = hiltViewModel(),
    profileDestination: PatientProfileDestinations = MainProfile,
    setStartProfileDestination: () -> Unit
) {
    val user by patientProfileViewModel.userFlow.collectAsState(initial = EmptyUser.instance)
    val profileScreenNavController = rememberAnimatedNavController()
    LaunchedEffect(key1 = profileDestination) {
        if (profileDestination != MainProfile) {
            profileScreenNavController.navigate(
                "${EditProfile.route}/${profileDestination.route}"
            )
        }
    }
    BackHandler(enabled = profileDestination != MainProfile) {
        setStartProfileDestination()
        profileScreenNavController.popBackStack()
    }

    AnimatedNavHost(
        navController = profileScreenNavController,
        startDestination = MainProfile.route
    ) {
        composable(
            route = MainProfile.route,
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it }
                ) + fadeOut(
                    animationSpec = tween(durationMillis = 300)
                )
            },
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(durationMillis = 300)
                ) + fadeIn(
                    animationSpec = tween(durationMillis = 300)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                  initialOffsetX = { -it },
                  animationSpec = tween(durationMillis = 300)
                ) + fadeIn(
                    animationSpec = tween(durationMillis = 300)
                )
            }
        ) {
            MainProfileScreen(
                patient = user as Patient?,
                onLogoutClick = { patientProfileViewModel.logout() }
            )
        }
        composable(
            route = "${EditProfile.route}/{editType}",
            arguments = EditProfile.arguments,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it }
                ) + fadeIn(
                    animationSpec = tween(durationMillis = 300)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(durationMillis = 300)
                ) + fadeOut(
                    animationSpec = tween(durationMillis = 300)
                )
            }
        ) { navBackStackEntry ->
            val editType =
                navBackStackEntry.arguments?.getString("editType") ?: ChangeEmail.route
            EditProfileScreen(editType = editType)
        }
    }
}