package rs.ac.bg.etf.diplomski.medsched.presentation.clinic

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.ui.graphics.vector.ImageVector
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.presentation.utils.BottomBarNavDestination

// Clinic bottom bar destinations

object ClinicHome: BottomBarNavDestination {
    override val title: Int = R.string.navbar_home
    override val route: String = "clinic_main_screen"
    override val imageUnselected: ImageVector = Icons.Outlined.Home
    override val imageSelected: ImageVector = Icons.Filled.Home
}

object ClinicDoctorEdit: BottomBarNavDestination {
    override val title: Int = R.string.clinic_doctor_register
    override val route: String = "clinic_doctor_register"
    override val imageUnselected: ImageVector = Icons.Outlined.PersonAdd
    override val imageSelected: ImageVector = Icons.Filled.PersonAdd
}

val clinicRoutes = listOf(ClinicHome, ClinicDoctorEdit)