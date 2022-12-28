package rs.ac.bg.etf.diplomski.medsched.data.remote.dto.response

import com.squareup.moshi.JsonClass
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Service

@JsonClass(generateAdapter = true)
data class ServiceDto(
    val name: String
) {
    fun toService(): Service {
        return Service(
            name = name
        )
    }
}