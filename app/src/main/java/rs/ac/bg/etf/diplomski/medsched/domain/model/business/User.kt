package rs.ac.bg.etf.diplomski.medsched.domain.model.business

data class User(
    val email: String,
    val password: String,
    val role: Int = 0,
    val phone: String = "",
    val ssn: String = ""
)