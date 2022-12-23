package rs.ac.bg.etf.diplomski.medsched.presentation.login_register.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import rs.ac.bg.etf.diplomski.medsched.presentation.login_register.LoginRegisterDestinations
import rs.ac.bg.etf.diplomski.medsched.presentation.login_register.LoginViewModel

@Composable
fun SplashScreen(
    loginViewModel: LoginViewModel = hiltViewModel(),
    onJumpDestination: (String) -> Unit
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator()
    }

    LaunchedEffect(key1 = loginViewModel.alreadyLogged) {
        loginViewModel.alreadyLogged?.let { logged ->
            onJumpDestination(
                if (!logged)
                    LoginRegisterDestinations.Login.route
                else
                    "proba"
            )
        }
    }
}