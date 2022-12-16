package rs.ac.bg.etf.diplomski.medsched.data.remote.entities

import com.squareup.moshi.Json

data class UserEntity(
    @Json(name = "email") val email: String,
    @Json(name = "password") val password: String
)
