package rs.ac.bg.etf.diplomski.medsched.domain.model.response

data class LoginResponse(
    val hasEmailError: Boolean = false,
    val hasPasswordError: Boolean = false
)
