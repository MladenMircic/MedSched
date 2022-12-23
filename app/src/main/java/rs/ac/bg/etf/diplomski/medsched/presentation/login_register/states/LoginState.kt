package rs.ac.bg.etf.diplomski.medsched.presentation.login_register.states

data class LoginState(
    val currentSelectedRole: String? = null,
    val email: String = "",
    val emailError: Int? = null,
    val password: String = "",
    val passwordError: Int? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean? = null,
    val snackBarMessageId: Int? = null,
    val alreadyLogged: Boolean? = null
)