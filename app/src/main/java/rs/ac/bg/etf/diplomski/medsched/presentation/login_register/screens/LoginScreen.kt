package rs.ac.bg.etf.diplomski.medsched.presentation.login_register.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.launch
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.commons.DEFAULT_FORM_PADDING
import rs.ac.bg.etf.diplomski.medsched.commons.NEXT_BUTTON_HEIGHT
import rs.ac.bg.etf.diplomski.medsched.presentation.login_register.LoginDestinations
import rs.ac.bg.etf.diplomski.medsched.presentation.login_register.LoginViewModel
import rs.ac.bg.etf.diplomski.medsched.presentation.login_register.composables.LoginForm
import rs.ac.bg.etf.diplomski.medsched.presentation.login_register.composables.UserRoleCard
import rs.ac.bg.etf.diplomski.medsched.presentation.login_register.models.roles
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = hiltViewModel(),
    onGoToRegister: () -> Unit
) {

    val loginUIState by loginViewModel.loginState.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val loginNavController = rememberAnimatedNavController()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colors.secondary
            )
    ) {

        // Main part of the login UI
        Surface(
            color = MaterialTheme.colors.primary,
            shape = RoundedShape60
                .copy(topStart = ZeroCornerSize, topEnd = ZeroCornerSize),
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.8f)
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

                // NavHost for login progression with animation
                AnimatedNavHost(
                    navController = loginNavController,
                    startDestination = LoginDestinations.RoleSelectDestination.route,
                    modifier = Modifier.fillMaxWidth(),
                    enterTransition = {
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
                        )
                    },
                    exitTransition = {
                        slideOutVertically(
                            targetOffsetY = { -it / 2 },
                            animationSpec = tween(
                                durationMillis = 300
                            )
                        ) + fadeOut(
                            animationSpec = tween(
                                durationMillis = 300
                            )
                        )
                    },
                    popEnterTransition = {
                        slideInVertically(
                            initialOffsetY = { -it / 2 },
                            animationSpec = tween(
                                durationMillis = 300,
                                delayMillis = 100
                            )
                        ) + fadeIn(
                            animationSpec = tween(
                                durationMillis = 300,
                                delayMillis = 100
                            )
                        )
                    },
                    popExitTransition = {
                        slideOutVertically(
                            targetOffsetY = { it / 2 },
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
                    composable(route = LoginDestinations.RoleSelectDestination.route) {
                        Column(modifier = Modifier.fillMaxWidth()) {
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
                            Button(
                                onClick = {
                                    if (loginUIState.currentSelectedRole == null) {
                                        Toast.makeText(
                                            context,
                                            context.resources.getString(R.string.no_role_picked),
                                            Toast.LENGTH_LONG
                                        ).show()
                                    } else {
                                        loginNavController.navigate(LoginDestinations.LoginFormDestination.route)
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
                        }
                    }
                    composable(route = LoginDestinations.LoginFormDestination.route) {
                        Column {
                            Row(
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 36.dp)
                            ) {
                                val selectedRole = roles.first {
                                    stringResource(id = it.roleName) == loginUIState.currentSelectedRole
                                }
                                UserRoleCard(
                                    roleName = stringResource(id = selectedRole.roleName),
                                    roleImage = selectedRole.roleImage,
                                    selectedRole = stringResource(id = selectedRole.roleName),
                                    onRoleSelect = {}
                                )
                            }
                            LoginForm(
                                loginState = loginUIState,
                                updateEmail = loginViewModel::setEmail,
                                updatePassword = loginViewModel::setPassword,
                                onLoginButtonClick = {
                                    if (loginViewModel.validateLoginForm()) {
                                        loginViewModel.loginUser()
                                        coroutineScope.launch {
                                            loginViewModel.loginStatusChannel.collect {
                                                Toast.makeText(
                                                    context,
                                                    context.getString(it),
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }

        // Bottom part for register option
        Surface(
            color = MaterialTheme.colors.secondary,
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.2f)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(id = R.string.no_account),
                    color = Blue90
                )
                Text(
                    text = stringResource(id = R.string.register_now),
                    color = Blue90,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        onGoToRegister()
                        loginNavController.popBackStack()
                    }
                )
            }
        }
    }
}