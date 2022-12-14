package rs.ac.bg.etf.diplomski.medsched.login_register_module.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.login_register_module.composables.UserRoleCard
import rs.ac.bg.etf.diplomski.medsched.login_register_module.models.roles
import rs.ac.bg.etf.diplomski.medsched.login_register_module.stateholders.LoginViewModel
import rs.ac.bg.etf.diplomski.medsched.ui.theme.Blue15
import rs.ac.bg.etf.diplomski.medsched.ui.theme.RoundedShape60
import rs.ac.bg.etf.diplomski.medsched.utils.FORM_SURFACE_HEIGHT

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
                            onRoleSelect = {
                                loginViewModel.setSelectedRole(it)
                            }
                        )
                    }
                }
            }
        }
    }
}