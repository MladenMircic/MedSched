package rs.ac.bg.etf.diplomski.medsched.domain.model.response

data class RegisterResponse(
    val success: Boolean = false,
    val accountExists: Boolean = false
)