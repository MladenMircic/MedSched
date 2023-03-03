package rs.ac.bg.etf.diplomski.medsched.domain.model.business

data class Patient(
    override val id: String = "",
    override val email: String,
    val firstName: String = "",
    val lastName: String = "",
    override val password: String,
    override val role: Int = 1,
    val phone: String = "",
    val ssn: String = "",
): User {
    override val type: Role = Role.PATIENT
}