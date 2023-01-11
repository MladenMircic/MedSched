package rs.ac.bg.etf.diplomski.medsched.data.remote.dto.request

import com.squareup.moshi.JsonClass
import rs.ac.bg.etf.diplomski.medsched.domain.model.request.EmailChangeRequest

@JsonClass(generateAdapter = true)
data class EmailChangeRequestDto(
    val email: String
) {
    fun toEmailChangeRequest(): EmailChangeRequest {
        return EmailChangeRequest(email = email)
    }
}