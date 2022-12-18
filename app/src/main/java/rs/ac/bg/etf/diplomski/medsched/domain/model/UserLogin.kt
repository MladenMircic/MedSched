package rs.ac.bg.etf.diplomski.medsched.domain.model

data class UserLogin(
    val hasEmailError: Boolean = false,
    val hasPasswordError: Boolean = false
)
