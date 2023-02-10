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
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.commons.DEFAULT_FORM_PADDING
import rs.ac.bg.etf.diplomski.medsched.commons.NEXT_BUTTON_HEIGHT
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Role
import rs.ac.bg.etf.diplomski.medsched.presentation.composables.LoginForm
import rs.ac.bg.etf.diplomski.medsched.presentation.composables.UserRoleCard
import rs.ac.bg.etf.diplomski.medsched.presentation.login_register.InfoForm
import rs.ac.bg.etf.diplomski.medsched.presentation.login_register.RoleSelect
import rs.ac.bg.etf.diplomski.medsched.presentation.login_register.events.LoginEvent
import rs.ac.bg.etf.diplomski.medsched.presentation.login_register.stateholders.LoginViewModel
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit,
    onGoToRegister: () -> Unit
) {

    val loginState by loginViewModel.loginState.collectAsState()
    val context = LocalContext.current
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
                    startDestination = RoleSelect.route,
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
                    composable(route = RoleSelect.route) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 36.dp)
                            ) {
                                UserRoleCard(
                                    role = Role.PATIENT,
                                    roleImage = R.drawable.patient_icon,
                                    selectedRole = loginState.currentSelectedRole,
                                    onRoleSelect = {
                                        loginViewModel.onEvent(LoginEvent.RoleChange(it))
                                    }
                                )
                                UserRoleCard(
                                    role = Role.DOCTOR,
                                    roleImage = R.drawable.doctor_icon,
                                    selectedRole = loginState.currentSelectedRole,
                                    onRoleSelect = {
                                        loginViewModel.onEvent(LoginEvent.RoleChange(it))
                                    }
                                )
                            }
                            Row(
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 36.dp)
                            ) {
                                UserRoleCard(
                                    role = Role.CLINIC,
                                    roleImage = R.drawable.clinic_icon,
                                    selectedRole = loginState.currentSelectedRole,
                                    onRoleSelect = {
                                        loginViewModel.onEvent(LoginEvent.RoleChange(it))
                                    }
                                )
                            }
                            Button(
                                onClick = {
                                    if (loginState.currentSelectedRole == null) {
                                        Toast.makeText(
                                            context,
                                            context.resources.getString(R.string.no_role_picked),
                                            Toast.LENGTH_LONG
                                        ).show()
                                    } else {
                                        loginNavController.navigate(InfoForm.route)
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
                    composable(route = InfoForm.route) {
                        Column {
                            Row(
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 36.dp)
                            ) {
                                UserRoleCard(
                                    role = loginState.currentSelectedRole!!,
                                    roleImage = when (loginState.currentSelectedRole) {
                                        Role.PATIENT -> R.drawable.patient_icon
                                        Role.DOCTOR -> R.drawable.doctor_icon
                                        Role.CLINIC -> R.drawable.clinic_icon
                                        Role.EMPTY -> R.drawable.patient_icon
                                        null -> R.drawable.patient_icon
                                    },
                                    selectedRole = loginState.currentSelectedRole!!,
                                    onRoleSelect = {}
                                )
                            }
                            LoginForm(
                                loginState = loginState,
                                updateEmail = {
                                    loginViewModel.onEvent(LoginEvent.EmailChange(it))
                                },
                                updatePassword = {
                                    loginViewModel.onEvent(LoginEvent.PasswordChange(it))
                                },
                                onLoginButtonClick = {
                                    loginViewModel.onEvent(LoginEvent.Submit)
                                }
                            )
                            // If login is not successful, display message here
                            LaunchedEffect(key1 = loginState.snackBarMessageId) {
                                loginState.snackBarMessageId?.let {
                                    Toast.makeText(
                                        context,
                                        context.getString(loginState.snackBarMessageId!!),
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                            // If login is successful, continue to the main page
                            LaunchedEffect(key1 = loginState.isSuccess) {
                                loginState.isSuccess?.let {
                                    onLoginSuccess()
                                }
                            }
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
                    color = MaterialTheme.colors.textOnSecondary
                )
                Text(
                    text = stringResource(id = R.string.register_now),
                    color = MaterialTheme.colors.textOnSecondary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        onGoToRegister()
                    }
                )
            }
        }
    }
}