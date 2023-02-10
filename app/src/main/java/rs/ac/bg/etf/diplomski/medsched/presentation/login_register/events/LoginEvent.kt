package rs.ac.bg.etf.diplomski.medsched.presentation.login_register.events

import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Role

sealed class LoginEvent {
    data class EmailChange(val email: String): LoginEvent()
    data class PasswordChange(val password: String): LoginEvent()
    data class RoleChange(val role: Role): LoginEvent()
    object Submit: LoginEvent()
}