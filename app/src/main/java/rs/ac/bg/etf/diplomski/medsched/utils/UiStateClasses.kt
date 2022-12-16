package rs.ac.bg.etf.diplomski.medsched.utils

data class LoginUIState(
    val currentSelectedRole: String? = null,
    val email: String = "",
    val password: String = "",
    val loginMessage: String? = null
)