package rs.ac.bg.etf.diplomski.medsched.data.remote.dto.response

import com.squareup.moshi.JsonClass
import rs.ac.bg.etf.diplomski.medsched.domain.model.response.PasswordChangeResponse

@JsonClass(generateAdapter = true)
data class PasswordChangeResponseDto(
    val oldPasswordCorrect: Boolean,
    val passwordUpdateSuccess: Boolean
) {
    fun toPasswordChangeResponse(): PasswordChangeResponse {
        return PasswordChangeResponse(
            oldPasswordCorrect = oldPasswordCorrect,
            passwordUpdateSuccess = passwordUpdateSuccess
        )
    }
}