package rs.ac.bg.etf.diplomski.medsched.domain.model.business

data class DoctorForPatient(
    val email: String,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val service: String
)