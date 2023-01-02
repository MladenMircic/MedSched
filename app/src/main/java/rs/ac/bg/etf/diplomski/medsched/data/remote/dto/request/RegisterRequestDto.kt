package rs.ac.bg.etf.diplomski.medsched.data.remote.dto.request

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegisterRequestDto(
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val password: String = "",
    val role: Int = -1,
    val phone: String = "",
    val ssn: String = ""
)