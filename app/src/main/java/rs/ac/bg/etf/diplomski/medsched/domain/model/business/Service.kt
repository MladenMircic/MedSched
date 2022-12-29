package rs.ac.bg.etf.diplomski.medsched.domain.model.business

import coil.request.ImageRequest
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Service(
    val name: String,
    var imageRequestBuilder: ImageRequest.Builder? = null
)