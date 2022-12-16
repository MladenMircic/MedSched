package rs.ac.bg.etf.diplomski.medsched

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint
import rs.ac.bg.etf.diplomski.medsched.presentation.login_register.screens.LoginScreen
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.MedSchedTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MedSchedTheme {
                LoginScreen()
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    MedSchedTheme {
        LoginScreen()
    }
}