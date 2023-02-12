package rs.ac.bg.etf.diplomski.medsched.domain.model.business

data class Doctor(
    override val id: Int = 0,
    override val email: String,
    val firstName: String = "",
    val lastName: String = "",
    override val password: String = "",
    override val role: Int = Role.DOCTOR.ordinal,
    val phone: String = ""
): User {
    override val type: Role = Role.DOCTOR
}