package rs.ac.bg.etf.diplomski.medsched.domain.model.business

data class Doctor(
    override val email: String,
    override val firstName: String = "",
    override val lastName: String = "",
    override val password: String,
    override val role: Int = 0,
    val phone: String = ""
): User {
    override val type: UserType = UserType.DOCTOR
}