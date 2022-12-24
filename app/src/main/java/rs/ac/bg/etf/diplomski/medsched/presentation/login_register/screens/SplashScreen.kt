package rs.ac.bg.etf.diplomski.medsched.presentation.login_register.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.presentation.graphs.Graph
import rs.ac.bg.etf.diplomski.medsched.presentation.login_register.LoginRegisterDestinations
import rs.ac.bg.etf.diplomski.medsched.presentation.login_register.LoginViewModel
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.Quicksand
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.logo

@Composable
fun SplashScreen(
    loginViewModel: LoginViewModel = hiltViewModel(),
    onJumpDestination: (String) -> Unit
) {

    val rotation by rememberInfiniteTransition().animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1500,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Restart
        )
    )
    Surface(
        color = MaterialTheme.colors.primary
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.caduceus),
                contentDescription = "",
                colorFilter = ColorFilter.tint(color = MaterialTheme.colors.logo),
                modifier = Modifier.graphicsLayer {
                    rotationY = rotation
                }
            )
            Text(
                text = "MedSched",
                fontFamily =  Quicksand,
                fontWeight = FontWeight.Bold,
                fontSize = 50.sp,
                color = MaterialTheme.colors.logo
            )
        }
    }

    LaunchedEffect(key1 = loginViewModel.alreadyLogged) {
        loginViewModel.alreadyLogged?.let { logged ->
            onJumpDestination(
                if (!logged)
                    LoginRegisterDestinations.Login.route
                else
                    Graph.PATIENT
            )
        }
    }
}