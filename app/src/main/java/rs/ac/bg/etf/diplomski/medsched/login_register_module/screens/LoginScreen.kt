package rs.ac.bg.etf.diplomski.medsched.login_register_module.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import rs.ac.bg.etf.diplomski.medsched.ui.theme.Blue15
import rs.ac.bg.etf.diplomski.medsched.ui.theme.RoundedShape60
import rs.ac.bg.etf.diplomski.medsched.utils.FORM_SURFACE_HEIGHT
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.login_register_module.composables.UserRoleCard

@Composable
fun LoginScreen() {
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
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 36.dp)
                ) {
                    UserRoleCard(
                        roleName = stringResource(id = R.string.doctor_role),
                        roleImage = R.drawable.doctor_icon,
                        roleImageDescription = stringResource(id = R.string.doctor_role_image_desc)
                    )
                    Spacer(modifier = Modifier.padding(20.dp))
                    UserRoleCard(
                        roleName = stringResource(id = R.string.patient_role),
                        roleImage = R.drawable.patient_icon,
                        roleImageDescription = stringResource(id = R.string.patient_role_image_desc)
                    )
                }
            }
        }
    }
}