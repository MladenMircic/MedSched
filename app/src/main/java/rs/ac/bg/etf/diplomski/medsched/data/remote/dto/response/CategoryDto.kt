package rs.ac.bg.etf.diplomski.medsched.data.remote.dto.response

import com.squareup.moshi.JsonClass
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Category

@JsonClass(generateAdapter = true)
data class CategoryDto(
    val id: Int,
    val name: String
) {
    fun toService(): Category {
        return Category(
            id = id,
            name = name
        )
    }
}