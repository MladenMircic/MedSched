package rs.ac.bg.etf.diplomski.medsched.presentation.patient.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Science
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PatientMainScreen(navController: NavHostController = rememberAnimatedNavController()) {
    Scaffold(
        topBar = {
            Row(modifier = Modifier.fillMaxWidth()) {
                Image(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Main app menu",
                )
                Image(
                    imageVector = Icons.Filled.Science,
                    contentDescription = "Proba",
                    modifier = Modifier.weight(2f)
                )
            }
        },
        backgroundColor = MaterialTheme.colors.primary
    ) {
        Text(text = "RADI", modifier = Modifier.padding(it))
    }
}