package rs.ac.bg.etf.diplomski.medsched.login_register_module.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import rs.ac.bg.etf.diplomski.medsched.ui.theme.Blue15
import rs.ac.bg.etf.diplomski.medsched.ui.theme.RoundedShape60
import rs.ac.bg.etf.diplomski.medsched.utils.FORM_SURFACE_HEIGHT
import rs.ac.bg.etf.diplomski.medsched.R

@Composable
fun LoginScreen() {
    Box {
        Surface(
            color = Blue15,
            modifier = Modifier.fillMaxSize()
        ) {

        }
        Surface(
            modifier = Modifier
                .height(FORM_SURFACE_HEIGHT)
                .fillMaxWidth(),
            shape = RoundedShape60.copy(topStart = ZeroCornerSize, topEnd = ZeroCornerSize)
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
            }
        }
    }
}