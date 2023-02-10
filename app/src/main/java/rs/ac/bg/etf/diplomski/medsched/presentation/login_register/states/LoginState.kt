package rs.ac.bg.etf.diplomski.medsched.presentation.login_register.states

import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Role

data class LoginState(
    val currentSelectedRole: Role? = null,
    val email: String = "",
    val emailError: Int? = null,
    val password: String = "",
    val passwordError: Int? = null,
    val roleError: Int? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean? = null,
    val snackBarMessageId: Int? = null,
    val alreadyLogged: Boolean? = null
)