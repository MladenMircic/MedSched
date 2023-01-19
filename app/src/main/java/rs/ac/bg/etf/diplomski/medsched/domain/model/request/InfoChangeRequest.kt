package rs.ac.bg.etf.diplomski.medsched.domain.model.request

data class InfoChangeRequest(
    val firstName: String,
    val lastName: String,
    val phone: String,
    val ssn: String
)