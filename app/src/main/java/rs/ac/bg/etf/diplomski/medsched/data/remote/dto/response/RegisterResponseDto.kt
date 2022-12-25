package rs.ac.bg.etf.diplomski.medsched.data.remote.dto.response

import com.squareup.moshi.JsonClass
import rs.ac.bg.etf.diplomski.medsched.domain.model.response.RegisterResponse

@JsonClass(generateAdapter = true)
data class RegisterResponseDto(
    val success: Boolean = false,
    val accountExists: Boolean = false
) {
    fun toRegisterResponse(): RegisterResponse {
        return RegisterResponse(
            success = success,
            accountExists = accountExists
        )
    }

}