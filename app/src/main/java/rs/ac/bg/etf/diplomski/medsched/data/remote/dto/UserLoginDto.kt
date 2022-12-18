package rs.ac.bg.etf.diplomski.medsched.data.remote.dto

import com.squareup.moshi.Json
import rs.ac.bg.etf.diplomski.medsched.domain.model.UserLogin

data class UserLoginDto(
    @Json(name = "emailError")
    val hasEmailError: Boolean = false,
    @Json(name = "passwordCorrect")
    val hasPasswordError: Boolean = false
) {
    fun toUserLogin(): UserLogin {
        return UserLogin(
            hasEmailError = hasEmailError,
            hasPasswordError = hasPasswordError
        )
    }
}

