package rs.ac.bg.etf.diplomski.medsched.presentation.patient

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.presentation.utils.BottomBarNavDestination

object PatientMain: BottomBarNavDestination {
    override val title: Int = R.string.navbar_home
    override val route: String = "patient_main_screen"
    override val imageUnselected: ImageVector = Icons.Outlined.Home
    override val imageSelected: ImageVector = Icons.Filled.Home
}

object PatientScheduled: BottomBarNavDestination {
    override val title: Int = R.string.navbar_scheduled
    override val route: String = "patient_scheduled"
    override val imageUnselected: ImageVector = Icons.Outlined.CalendarToday
    override val imageSelected: ImageVector = Icons.Filled.CalendarToday
}

object PatientInfo: BottomBarNavDestination {
    override val title: Int = R.string.navbar_account
    override val route: String = "patient_info"
    override val imageUnselected: ImageVector = Icons.Outlined.Person
    override val imageSelected: ImageVector = Icons.Filled.Person
}

val patientRoutes = listOf(PatientMain, PatientScheduled, PatientInfo)