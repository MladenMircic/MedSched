package rs.ac.bg.etf.diplomski.medsched.presentation.login_register

// Login and register destinations
sealed class LoginRegisterDestinations(val route: String) {
    object SplashScreen: LoginRegisterDestinations("splash_screen")
    object Login: LoginRegisterDestinations("login")
    object Register: LoginRegisterDestinations("register")
}


// Login destinations
sealed class LoginDestinations(val route: String) {
    object RoleSelect: LoginDestinations("role_select")
    object LoginForm: LoginDestinations("login_form")
}

// Register destinations