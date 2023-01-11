package rs.ac.bg.etf.diplomski.medsched.data.remote.dto.request

import com.squareup.moshi.JsonClass
import rs.ac.bg.etf.diplomski.medsched.domain.model.request.PasswordChangeRequest

@JsonClass(generateAdapter = true)
data class PasswordChangeRequestDto(
    val oldPassword: String,
    val newPassword: String
) {
    fun toPasswordChangeRequest(): PasswordChangeRequest {
        return PasswordChangeRequest(
            oldPassword = oldPassword,
            newPassword = newPassword
        )
    }
}