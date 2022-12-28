package rs.ac.bg.etf.diplomski.medsched.presentation.patient.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.PatientViewModel
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.Quicksand
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.textOnPrimary

@Composable
fun PatientHomeScreen(
    patientViewModel: PatientViewModel = hiltViewModel()
) {
    val user by patientViewModel.userFlow.collectAsState(initial = null)
    val patientState by patientViewModel.patientState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxHeight(0.12f)
                .padding(horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                user?.let {
                    Text(
                        text = "${stringResource(id = R.string.welcome)},",
                        fontFamily = Quicksand,
                        fontSize = 22.sp,
                        color = MaterialTheme.colors.textOnPrimary
                    )
                    Text(
                        text = "${user!!.firstName} ${user!!.lastName}",
                        fontFamily = Quicksand,
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp,
                        color = MaterialTheme.colors.textOnPrimary,
                        modifier = Modifier.offset(y = (-8).dp)
                    )
                }
            }
            Image(
                painter = painterResource(id = R.drawable.caduceus),
                contentDescription = "",
                colorFilter = ColorFilter.tint(color = MaterialTheme.colors.textOnPrimary),
                contentScale = ContentScale.Fit
            )
        }
        LazyRow {
            items(items = patientState.serviceList) { service ->
                AsyncImage(
                    model = service.imageRequest,
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(color = Color.White)
                )
            }
        }

    }
}