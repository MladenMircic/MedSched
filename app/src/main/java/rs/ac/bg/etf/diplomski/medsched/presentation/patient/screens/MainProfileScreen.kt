package rs.ac.bg.etf.diplomski.medsched.presentation.patient.screens

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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Patient
import rs.ac.bg.etf.diplomski.medsched.presentation.composables.AccountInfoPartCard
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.Quicksand
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.RoundedShape60
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.textOnPrimary
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.textOnSecondary

@Composable
fun MainProfileScreen(
    patient: Patient?,
    onLogoutClick: () -> Unit
) {
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
                TextButton(onClick = onLogoutClick) {
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

                patient?.let {
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
                        AccountInfoPartCard(
                            icon = Icons.Default.Badge,
                            headerText = stringResource(id = R.string.social_security_number),
                            contentText = it.ssn
                        )
                    }
                }

            }
        }
    }
}