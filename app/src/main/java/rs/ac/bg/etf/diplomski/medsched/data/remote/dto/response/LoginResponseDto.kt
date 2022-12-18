package rs.ac.bg.etf.diplomski.medsched.data.remote.dto.response

import com.squareup.moshi.Json
import rs.ac.bg.etf.diplomski.medsched.domain.model.response.LoginResponse

data class LoginResponseDto(
    @Json(name = "hasEmailError")
    val hasEmailError: Boolean = false,
    @Json(name = "hasPasswordCorrect")
    val hasPasswordError: Boolean = false
) {
    fun toUserLogin(): LoginResponse {
        return LoginResponse(
            hasEmailError = hasEmailError,
            hasPasswordError = hasPasswordError
        )
    }
}

