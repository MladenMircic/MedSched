package rs.ac.bg.etf.diplomski.medsched.data.remote.dto.request

data class RegisterRequestDto(
    val email: String,
    val password: String,
    val role: Int,
    val phone: String,
    val ssn: String
)