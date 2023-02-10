package rs.ac.bg.etf.diplomski.medsched.presentation.doctor

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.navDeepLink
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.commons.Constants
import rs.ac.bg.etf.diplomski.medsched.presentation.utils.BottomBarNavDestination

// Doctor bottom bar destinations

object DoctorHome: BottomBarNavDestination {
    override val title: Int = R.string.navbar_home
    override val route: String = "doctor_main_screen"
    override val imageUnselected: ImageVector = Icons.Outlined.Home
    override val imageSelected: ImageVector = Icons.Filled.Home
    val deepLinks = listOf(navDeepLink {
        uriPattern = Constants.DOCTOR_SCHEDULED_DEEPLINK
    })
}

object DoctorAccount: BottomBarNavDestination {
    override val title: Int = R.string.navbar_account
    override val route: String = "doctor_account"
    override val imageUnselected: ImageVector = Icons.Outlined.Person
    override val imageSelected: ImageVector = Icons.Filled.Person
}

val doctorRoutes = listOf(DoctorHome, DoctorAccount)