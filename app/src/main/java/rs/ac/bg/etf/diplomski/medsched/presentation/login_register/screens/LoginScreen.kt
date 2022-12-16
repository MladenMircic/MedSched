package rs.ac.bg.etf.diplomski.medsched.presentation.login_register.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.commons.DEFAULT_FORM_PADDING
import rs.ac.bg.etf.diplomski.medsched.commons.FORM_SURFACE_HEIGHT
import rs.ac.bg.etf.diplomski.medsched.commons.NEXT_BUTTON_HEIGHT
import rs.ac.bg.etf.diplomski.medsched.presentation.login_register.LoginViewModel
import rs.ac.bg.etf.diplomski.medsched.presentation.login_register.composables.LoginForm
import rs.ac.bg.etf.diplomski.medsched.presentation.login_register.composables.UserRoleCard
import rs.ac.bg.etf.diplomski.medsched.presentation.login_register.models.roles
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = viewModel()
) {

    val loginUIState by loginViewModel.loginState.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Box {
        // Bottom part for register option
        Surface(
            color = MaterialTheme.colors.secondary,
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.padding(bottom = 60.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.no_account),
                    color = Blue90
                )
                Text(
                    text = stringResource(id = R.string.register_now),
                    color = Blue90,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Main part of the login UI
        Surface(
            color = MaterialTheme.colors.primary,
            shape = RoundedShape60
                .copy(topStart = ZeroCornerSize, topEnd = ZeroCornerSize),
            modifier = Modifier
                .height(FORM_SURFACE_HEIGHT)
                .fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.login_as),
                    color = MaterialTheme.colors.textOnPrimary,
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
                    if (!loginUIState.isRolePicked) {
                        for (role in roles) {
                            UserRoleCard(
                                roleName = stringResource(id = role.roleName),
                                roleImage = role.roleImage,
                                selectedRole = loginUIState.currentSelectedRole,
                                onRoleSelect = loginViewModel::setSelectedRole
                            )
                        }
                    }
                }

                AnimatedContent(
                    targetState = loginUIState.isRolePicked,
                    modifier = Modifier.fillMaxWidth(),
                    transitionSpec = {
                        slideInVertically(
                            initialOffsetY = { it / 2 },
                            animationSpec = tween(
                                durationMillis = 300,
                                delayMillis = 100
                            )
                        ) + fadeIn(
                            animationSpec = tween(
                                durationMillis = 300,
                                delayMillis = 100
                            )
                        ) with slideOutVertically(
                            targetOffsetY = { -it / 2 },
                            animationSpec = tween(
                                durationMillis = 300
                            )
                        ) + fadeOut(
                            animationSpec = tween(
                                durationMillis = 300
                            )
                        )
                    }
                ) { isRolePicked ->
                    if (!isRolePicked) {
                        Button(
                            onClick = {
                                if (loginUIState.currentSelectedRole == null) {
                                    Toast.makeText(
                                        context,
                                        context.resources.getString(R.string.no_role_picked),
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else {
                                    loginViewModel.setIsRolePicked(true)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(NEXT_BUTTON_HEIGHT)
                                .padding(
                                    horizontal = DEFAULT_FORM_PADDING,
                                ),
                            shape = RoundedShape20,
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.selectable
                            )
                        ) {
                            Text(
                                text = stringResource(id = R.string.next_button_text),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = BackgroundPrimaryLight,
                                modifier = Modifier.padding(bottom = 10.dp)
                            )
                        }
                    } else {
                        LoginForm(
                            email = loginUIState.email,
                            password = loginUIState.password,
                            updateEmail = loginViewModel::setEmail,
                            updatePassword = loginViewModel::setPassword,
                            onLoginButtonClick = {
                                coroutineScope.launch {
                                    loginViewModel.loginUser()
                                    loginViewModel.loginStatusChannel.collect {
                                        Toast.makeText(context, it, Toast.LENGTH_LONG)
                                            .show()
                                        Log.d("TESTIRANJE", it)
                                    }
                                }
                            }
                        )
                    }
                }
//                AnimatedNavHost(
//                    navController = navController,
//                    startDestination = RoleSelectDestination.route,
//                    enterTransition = {
//                        slideIntoContainer(
//                            towards = AnimatedContentScope.SlideDirection.Up,
//                            animationSpec = tween(
//                                durationMillis = 300,
//                                delayMillis = 300
//                            )
//                        ) + fadeIn(
//                            animationSpec = tween(
//                                durationMillis = 300,
//                                delayMillis = 300
//                            )
//                        )
//                    },
//                    exitTransition = {
//                        slideOutOfContainer(
//                            towards = AnimatedContentScope.SlideDirection.Down,
//                            animationSpec = tween(
//                                durationMillis = 300
//                            )
//                        ) + fadeOut(
//                            animationSpec = tween(
//                                durationMillis = 300
//                            )
//                        )
//                    }
//                ) {
//                    composable(route = RoleSelectDestination.route) {
//                        Spacer(modifier = Modifier.padding(top = DEFAULT_FORM_PADDING))
//
//                    }
//                    composable(route = LoginFormDestination.route) {
//
//                    }
//                }
            }
        }
    }
}