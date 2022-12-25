package rs.ac.bg.etf.diplomski.medsched.presentation.login_register.events

sealed class LoginEvent {
    data class EmailChange(val email: String): LoginEvent()
    data class PasswordChange(val password: String): LoginEvent()
    data class RoleChange(val role: String): LoginEvent()
    object Submit: LoginEvent()
}