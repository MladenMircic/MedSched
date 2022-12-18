package rs.ac.bg.etf.diplomski.medsched.presentation.login_register.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.presentation.login_register.RegisterViewModel
import rs.ac.bg.etf.diplomski.medsched.presentation.login_register.composables.RegisterForm
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.Blue90
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.RoundedShape60
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.textOnPrimary

@Composable
fun RegisterScreen(
    registerViewModel: RegisterViewModel = viewModel()
) {
    val registerState by registerViewModel.registerState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colors.secondary
            )
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
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = stringResource(id = R.string.registration),
                    color = MaterialTheme.colors.textOnPrimary,
                    style = MaterialTheme.typography.h1
                )

                RegisterForm(
                    registerState = registerState,
                    updateEmail = {
                        registerViewModel.setFieldValue(RegisterViewModel.RegisterField.EMAIL, it)
                    },
                    updatePassword = {
                        registerViewModel.setFieldValue(RegisterViewModel.RegisterField.PASSWORD, it)
                    },
                    updateConfirmPassword = {
                        registerViewModel.setFieldValue(RegisterViewModel.RegisterField.CONFIRM_PASSWORD, it)
                    },
                    updatePhone = {
                        registerViewModel.setFieldValue(RegisterViewModel.RegisterField.PHONE, it)
                    },
                    updateLBO = {
                        registerViewModel.setFieldValue(RegisterViewModel.RegisterField.LBO, it)
                    },
                    onRegisterButtonClick = registerViewModel::validateRegisterForm
                )
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
                    modifier = Modifier
                        .clickable {

                        }
                )
            }
        }
    }
}