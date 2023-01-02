package rs.ac.bg.etf.diplomski.medsched.data.remote.dto.response

import com.squareup.moshi.JsonClass
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.User
import rs.ac.bg.etf.diplomski.medsched.domain.model.response.LoginResponse

@JsonClass(generateAdapter = true)
data class LoginResponseDto(
    val hasEmailError: Boolean = false,
    val hasPasswordError: Boolean = false,
    val hasRoleError: Boolean = false,
    val token: String? = null,
    val user: User? = null
) {

    fun toLoginResponse(): LoginResponse {
        return LoginResponse(
            hasEmailError = hasEmailError,
            hasPasswordError = hasPasswordError,
            hasRoleError = hasRoleError,
            token = token,
            user = user
        )
    }
}