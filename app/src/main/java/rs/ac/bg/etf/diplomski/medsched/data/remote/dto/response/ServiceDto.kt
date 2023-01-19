package rs.ac.bg.etf.diplomski.medsched.data.remote.dto.response

import com.squareup.moshi.JsonClass
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Service

@JsonClass(generateAdapter = true)
data class ServiceDto(
    val id: Int,
    val name: String,
    val categoryId: Int
) {
    fun toService(): Service {
        return Service(
            id = id,
            name = name,
            categoryId = categoryId
        )
    }
}