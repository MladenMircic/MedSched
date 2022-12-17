package rs.ac.bg.etf.diplomski.medsched.data.remote.dto

import com.squareup.moshi.Json
import rs.ac.bg.etf.diplomski.medsched.domain.model.UserLogin

data class UserLoginDto(
    @Json(name = "emailError")
    val emailError: String? = null,
    @Json(name = "passwordCorrect")
    val passwordError: String? = null
)

fun UserLoginDto.toUserLogin(): UserLogin {
    return UserLogin(
        emailError = emailError,
        passwordError = passwordError
    )
}