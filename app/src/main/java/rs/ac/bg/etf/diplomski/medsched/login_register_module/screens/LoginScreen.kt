package rs.ac.bg.etf.diplomski.medsched.login_register_module.screens

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.login_register_module.LoginFormDestination
import rs.ac.bg.etf.diplomski.medsched.login_register_module.RoleSelectDestination
import rs.ac.bg.etf.diplomski.medsched.login_register_module.composables.LoginForm
import rs.ac.bg.etf.diplomski.medsched.login_register_module.composables.UserRoleCard
import rs.ac.bg.etf.diplomski.medsched.login_register_module.models.roles
import rs.ac.bg.etf.diplomski.medsched.login_register_module.stateholders.LoginViewModel
import rs.ac.bg.etf.diplomski.medsched.ui.theme.*
import rs.ac.bg.etf.diplomski.medsched.utils.FORM_SURFACE_HEIGHT
import rs.ac.bg.etf.diplomski.medsched.utils.DEFAULT_FORM_PADDING
import rs.ac.bg.etf.diplomski.medsched.utils.NEXT_BUTTON_HEIGHT

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = viewModel()
) {

    val loginUIState by loginViewModel.loginState.collectAsState()

    Box {
        // Bottom part for register option
        Surface(
            color = Blue15,
            modifier = Modifier.fillMaxSize()
        ) {

        }

        // Main part of the login UI
        Surface(
            shape = RoundedShape60
                .copy(topStart = ZeroCornerSize, topEnd = ZeroCornerSize),
            modifier = Modifier
                .height(FORM_SURFACE_HEIGHT)
                .fillMaxWidth(),

        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.login_as),
                    color = Blue15,
                    style = MaterialTheme.typography.h1
                )

                // Cards for choosing user role
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 36.dp)
                ) {
                    for (role in roles) {
                        UserRoleCard(
                            roleName = stringResource(id = role.roleName),
                            roleImage = role.roleImage,
                            selectedRole = loginUIState.currentSelectedRole,
                            onRoleSelect = loginViewModel::setSelectedRole
                        )
                    }
                }

                val navController = rememberAnimatedNavController()
                AnimatedNavHost(
                    navController = navController,
                    startDestination = RoleSelectDestination.route,
                    enterTransition = {
                        slideIntoContainer(
                            towards = AnimatedContentScope.SlideDirection.Up,
                            animationSpec = tween(
                                durationMillis = 300,
                                delayMillis = 300
                            )
                        ) + fadeIn(
                            animationSpec = tween(
                                durationMillis = 300,
                                delayMillis = 300
                            )
                        )
                    },
                    exitTransition = {
                        slideOutOfContainer(
                            towards = AnimatedContentScope.SlideDirection.Down,
                            animationSpec = tween(
                                durationMillis = 300
                            )
                        ) + fadeOut(
                            animationSpec = tween(
                                durationMillis = 300
                            )
                        )
                    }
                ) {
                    composable(route = RoleSelectDestination.route) {
                        Spacer(modifier = Modifier.padding(top = DEFAULT_FORM_PADDING))
                        Button(
                            onClick = {
                                navController.navigate(LoginFormDestination.route)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(NEXT_BUTTON_HEIGHT)
                                .padding(
                                    horizontal = DEFAULT_FORM_PADDING,
                                ),
                            shape = RoundedShape20,
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Blue40
                            )
                        ) {
                            Text(
                                text = stringResource(id = R.string.next_button_text),
                                fontSize = 24.sp,
                                fontFamily = Quicksand,
                                fontWeight = FontWeight.Bold,
                                color = Blue95,
                                modifier = Modifier.padding(bottom = 10.dp)
                            )
                        }
                    }
                    composable(route = LoginFormDestination.route) {
                        LoginForm(
                            email = loginUIState.email,
                            password = loginUIState.password,
                            updateEmail = loginViewModel::setEmail,
                            updatePassword = loginViewModel::setPassword
                        )
                    }
                }
            }
        }
    }
}