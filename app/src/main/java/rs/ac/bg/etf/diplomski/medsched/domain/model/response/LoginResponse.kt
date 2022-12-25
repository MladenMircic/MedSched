package rs.ac.bg.etf.diplomski.medsched.domain.model.response

import rs.ac.bg.etf.diplomski.medsched.domain.model.business.User

data class LoginResponse(
    val hasEmailError: Boolean = false,
    val hasPasswordError: Boolean = false,
    val hasRoleError: Boolean = false,
    val token: String? = null,
    val user: User? = null
)
