package rs.ac.bg.etf.diplomski.medsched.presentation.patient

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.commons.Constants.PATIENT_SCHEDULED_DEEPLINK
import rs.ac.bg.etf.diplomski.medsched.presentation.utils.BottomBarNavDestination

// Patient home screen destinations

interface PatientHomeDestinations {
    val route: String
}

object PatientMainPages: PatientHomeDestinations {
    override val route: String = "main_pages"
}

object PatientHomeStart: PatientHomeDestinations {
    override val route: String = "patient_home_start"
}

object Notifications: PatientHomeDestinations {
    override val route: String = "notifications"
}

object Search: PatientHomeDestinations {
    override val route: String = "search"
    val arguments = listOf(
        navArgument("categoryIds") {
            type = NavType.StringType
        }
    )
}

object DoctorDetails: PatientHomeDestinations {
    override val route: String = "doctor_details"
}

// Search screen destinations

object SearchHome: PatientHomeDestinations {
    override val route: String = "search_home"
}

object SearchToSchedule: PatientHomeDestinations {
    override val route: String = "search_to_schedule"
}

// Patient bottom bar destinations

object PatientHome: BottomBarNavDestination {
    override val title: Int = R.string.navbar_home
    override val route: String = "patient_main_screen"
    override val imageUnselected: ImageVector = Icons.Outlined.Home
    override val imageSelected: ImageVector = Icons.Filled.Home
}

object PatientScheduled: BottomBarNavDestination {
    override val title: Int = R.string.navbar_scheduled
    override val route: String = "patient_scheduled"
    override val imageUnselected: ImageVector = Icons.Outlined.CalendarMonth
    override val imageSelected: ImageVector = Icons.Filled.CalendarMonth
    val deepLinks = listOf(navDeepLink {
        uriPattern = PATIENT_SCHEDULED_DEEPLINK
    })
}

object PatientAccount: BottomBarNavDestination {
    override val title: Int = R.string.navbar_account
    override val route: String = "patient_account"
    override val imageUnselected: ImageVector = Icons.Outlined.Person
    override val imageSelected: ImageVector = Icons.Filled.Person
}

val patientRoutes = listOf(PatientHome, PatientScheduled, PatientAccount)

// Patient profile destinations

interface PatientProfileDestinations {
    val route: String
}

object MainProfile: PatientProfileDestinations {
    override val route: String = "main_profile"
}

object EditProfile: PatientProfileDestinations {
    override val route: String = "edit_profile"
    val arguments = listOf(
        navArgument("editType") {
            type = NavType.StringType
        }
    )
}

// Profile change destinations

object ChangeEmail: PatientProfileDestinations {
    override val route: String = "change_email"
}

object ChangePassword: PatientProfileDestinations {
    override val route: String = "change_password"
}

object ChangeInfo: PatientProfileDestinations {
    override val route: String = "change_info"
}