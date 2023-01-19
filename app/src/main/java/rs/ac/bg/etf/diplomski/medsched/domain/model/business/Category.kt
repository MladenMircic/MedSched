package rs.ac.bg.etf.diplomski.medsched.domain.model.business

import coil.request.ImageRequest
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Category(
    val id: Int,
    val name: String,
    var imageRequest: ImageRequest? = null
)