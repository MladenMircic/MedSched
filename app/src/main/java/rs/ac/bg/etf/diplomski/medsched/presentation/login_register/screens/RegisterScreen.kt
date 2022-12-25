package rs.ac.bg.etf.diplomski.medsched.presentation.login_register.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.presentation.composables.RegisterForm
import rs.ac.bg.etf.diplomski.medsched.presentation.login_register.events.RegisterEvent
import rs.ac.bg.etf.diplomski.medsched.presentation.login_register.stateholders.RegisterViewModel
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.Blue90
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.RoundedShape60
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.success
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.textOnPrimary

@Composable
fun RegisterScreen(
    registerViewModel: RegisterViewModel = hiltViewModel(),
    onBackToLogin: () -> Unit,
) {
    val registerState by registerViewModel.registerState.collectAsState()
    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current

    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = {
            SnackbarHost(
                hostState = it,
                modifier = Modifier
                    .fillMaxWidth()
            ) { data ->
                Snackbar(
                    snackbarData = data,
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor =
                        if (registerState.isSuccess) MaterialTheme.colors.success
                        else MaterialTheme.colors.error,
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp, bottom = 30.dp)
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colors.secondary
                )
                .padding(it)
        ) {
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
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(vertical = 10.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.registration),
                        color = MaterialTheme.colors.textOnPrimary,
                        style = MaterialTheme.typography.h1,
                        modifier = Modifier.padding(bottom = 15.dp)
                    )

                    RegisterForm(
                        registerState = registerState,
                        updateEmail = { text ->
                            registerViewModel.onEvent(
                                RegisterEvent.FieldChange(
                                    RegisterViewModel.RegisterField.EMAIL, text
                                )
                            )
                        },
                        updateFirstName = { text ->
                            registerViewModel.onEvent(
                                RegisterEvent.FieldChange(
                                    RegisterViewModel.RegisterField.FIRST_NAME, text
                                )
                            )
                        },
                        updateLastName = { text ->
                            registerViewModel.onEvent(
                                RegisterEvent.FieldChange(
                                    RegisterViewModel.RegisterField.LAST_NAME, text
                                )
                            )
                        },
                        updatePassword = { text ->
                            registerViewModel.onEvent(
                                RegisterEvent.FieldChange(
                                    RegisterViewModel.RegisterField.PASSWORD, text
                                )
                            )
                        },
                        updateConfirmPassword = { text ->
                            registerViewModel.onEvent(
                                RegisterEvent.FieldChange(
                                    RegisterViewModel.RegisterField.CONFIRM_PASSWORD, text
                                )
                            )
                        },
                        updatePhone = { text ->
                            registerViewModel.onEvent(
                                RegisterEvent.FieldChange(
                                    RegisterViewModel.RegisterField.PHONE, text
                                )
                            )
                        },
                        updateLBO = { text ->
                            registerViewModel.onEvent(
                                RegisterEvent.FieldChange(
                                    RegisterViewModel.RegisterField.LBO, text
                                )
                            )
                        },
                        onRegisterButtonClick = { registerViewModel.onEvent(RegisterEvent.Submit) }
                    )

                    // Show an error snackBar and go back to login if register is success
                    LaunchedEffect(key1 = registerState.snackBarMessage) {
                        registerState.snackBarMessage?.let {
                            scaffoldState.snackbarHostState.showSnackbar(
                                message = context.getString(registerState.snackBarMessage!!),
                                duration = SnackbarDuration.Short
                            )
                            if (registerState.isSuccess) {
                                onBackToLogin()
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
                        text = stringResource(id = R.string.already_has_account),
                        color = Blue90
                    )
                    Text(
                        text = stringResource(id = R.string.login_now),
                        color = Blue90,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            onBackToLogin()
                        }
                    )
                }
            }
        }
    }
}