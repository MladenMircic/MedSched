package rs.ac.bg.etf.diplomski.medsched.data.remote.dto

import com.squareup.moshi.Json
import rs.ac.bg.etf.diplomski.medsched.domain.model.UserLogin

data class UserLoginDto(
    @Json(name = "userFound")
    val userFound: Boolean
)

fun UserLoginDto.toUserLogin(): UserLogin {
    return UserLogin(
        userFound = userFound
    )
}