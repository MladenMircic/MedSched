package rs.ac.bg.etf.diplomski.medsched.login_register_module

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