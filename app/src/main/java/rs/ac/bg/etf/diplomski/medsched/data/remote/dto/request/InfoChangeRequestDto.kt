package rs.ac.bg.etf.diplomski.medsched.data.remote.dto.request

import com.squareup.moshi.JsonClass
import rs.ac.bg.etf.diplomski.medsched.domain.model.request.InfoChangeRequest

@JsonClass(generateAdapter = true)
data class InfoChangeRequestDto(
    val firstName: String,
    val lastName: String,
    val phone: String,
    val ssn: String
) {
    fun toInfoChangeRequest(): InfoChangeRequest {
        return InfoChangeRequest(
            firstName = firstName,
            lastName = lastName,
            phone = phone,
            ssn = ssn
        )
    }
}