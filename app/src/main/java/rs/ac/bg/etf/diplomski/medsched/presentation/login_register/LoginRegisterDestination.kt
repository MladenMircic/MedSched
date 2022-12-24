package rs.ac.bg.etf.diplomski.medsched.presentation.login_register

// Login and register destinations
interface LoginRegisterDestination {
    val route: String
}

object Authentication: LoginRegisterDestination {
    override val route: String = "authentication"
}

object Login: LoginRegisterDestination {
    override val route: String = "login"

}

object Register: LoginRegisterDestination {
    override val route: String = "register"
}


// Login form destinations
interface LoginDestination {
    val route: String
}

object RoleSelect: LoginDestination {
    override val route: String = "role_select"

}

object InfoForm: LoginDestination {
    override val route: String = "login_form"
}