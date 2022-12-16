package rs.ac.bg.etf.diplomski.medsched.data.remote.responses

import com.squareup.moshi.Json

data class MessageResponse(
    @Json(name = "message") val message: String
)