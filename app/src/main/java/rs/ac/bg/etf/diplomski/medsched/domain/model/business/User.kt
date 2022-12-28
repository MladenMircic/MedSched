package rs.ac.bg.etf.diplomski.medsched.domain.model.business

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    val email: String,
    val firstName: String = "",
    val lastName: String = "",
    val password: String,
    val role: Int = 0,
    val phone: String = "",
    val ssn: String = ""
) {
    companion object {
        val EMPTY_USER = User(email = "", password = "")
    }
}