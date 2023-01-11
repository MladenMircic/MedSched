package rs.ac.bg.etf.diplomski.medsched.domain.model.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PasswordChangeResponse(
    val oldPasswordCorrect: Boolean = true,
    val passwordUpdateSuccess: Boolean = false
)