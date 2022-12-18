package rs.ac.bg.etf.diplomski.medsched.data.remote.dto.response

import com.squareup.moshi.Json
import rs.ac.bg.etf.diplomski.medsched.domain.model.response.RegisterResponse

data class RegisterResponseDto(
    @Json(name = "success")
    val success: Boolean = false,
    @Json(name = "accountExists")
    val accountExists: Boolean = false
) {
    fun toRegisterResponse(): RegisterResponse {
        return RegisterResponse(
            success = success,
            accountExists = accountExists
        )
    }

}