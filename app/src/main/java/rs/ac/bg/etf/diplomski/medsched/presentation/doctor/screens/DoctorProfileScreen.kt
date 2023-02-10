package rs.ac.bg.etf.diplomski.medsched.presentation.doctor.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Doctor
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.User
import rs.ac.bg.etf.diplomski.medsched.presentation.composables.AccountInfoPartCard
import rs.ac.bg.etf.diplomski.medsched.presentation.doctor.stateholders.DoctorProfileViewModel
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.Quicksand
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.RoundedShape60
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.textOnPrimary
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.textOnSecondary

@Composable
fun DoctorProfileScreen(
    doctorProfileViewModel: DoctorProfileViewModel = hiltViewModel()
) {
    val user: User? by doctorProfileViewModel.userFlow.collectAsState(initial = null)
    val doctor = user as Doctor?

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.secondary)
    ) {
        Surface(
            color = MaterialTheme.colors.secondary,
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.1f)
        ) {
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp)
            ) {
                TextButton(onClick = doctorProfileViewModel::logout) {
                    Text(
                        text = stringResource(id = R.string.logout),
                        fontFamily = Quicksand,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colors.textOnSecondary,
                        modifier = Modifier.padding(bottom = 3.dp, end = 8.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.Logout,
                        contentDescription = "Logout button icon",
                        tint = MaterialTheme.colors.textOnSecondary
                    )
                }
            }
        }

        Surface(
            color = MaterialTheme.colors.primary,
            shape = RoundedShape60.copy(bottomStart = ZeroCornerSize, bottomEnd = ZeroCornerSize),
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.9f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp, vertical = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = stringResource(id = R.string.account_info),
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    color = MaterialTheme.colors.textOnPrimary
                )

                doctor?.let {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(18.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        AccountInfoPartCard(
                            icon = Icons.Default.Email,
                            headerText = stringResource(id = R.string.email),
                            contentText = it.email
                        )
                        AccountInfoPartCard(
                            icon = Icons.Default.Person,
                            headerText = stringResource(id = R.string.first_name),
                            contentText = it.firstName
                        )
                        AccountInfoPartCard(
                            icon = Icons.Default.Person,
                            headerText = stringResource(id = R.string.last_name),
                            contentText = it.lastName
                        )
                        AccountInfoPartCard(
                            icon = Icons.Default.Phone,
                            headerText = stringResource(id = R.string.phone),
                            contentText = it.phone
                        )
                    }
                }

            }
        }
    }
}