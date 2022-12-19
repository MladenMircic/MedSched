package rs.ac.bg.etf.diplomski.medsched.presentation.login_register

// Login and register destinations
sealed class LoginRegisterDestinations(val route: String) {
    object LoginDestination: LoginRegisterDestinations("login")
    object RegisterDestination: LoginRegisterDestinations("register")
}


// Login destinations
sealed class LoginDestinations(val route: String) {
    object RoleSelectDestination: LoginDestinations("role_select")
    object LoginFormDestination: LoginDestinations("login_form")
}

// Register destinations