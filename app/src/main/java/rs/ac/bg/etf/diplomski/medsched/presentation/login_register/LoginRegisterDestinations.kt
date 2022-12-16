package rs.ac.bg.etf.diplomski.medsched.presentation.login_register

// Login destinations
interface LoginDestinations {
    val route: String
}

object RoleSelectDestination: LoginDestinations {
    override val route: String = "role_select"
}

object LoginFormDestination: LoginDestinations {
    override val route: String = "login_form"
}

// Register destinations