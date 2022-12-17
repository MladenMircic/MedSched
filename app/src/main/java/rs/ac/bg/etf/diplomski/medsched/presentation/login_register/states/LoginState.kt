package rs.ac.bg.etf.diplomski.medsched.presentation.login_register.states

data class LoginState(
    val currentSelectedRole: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null
)