package rs.ac.bg.etf.diplomski.medsched.commons

data class LoginUIState(
    val currentSelectedRole: String? = null,
    val email: String = "",
    val password: String = "",
    val isRolePicked: Boolean = false,
    val loginMessage: String? = null
)