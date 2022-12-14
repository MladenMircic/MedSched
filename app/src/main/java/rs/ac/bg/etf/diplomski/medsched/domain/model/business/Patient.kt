package rs.ac.bg.etf.diplomski.medsched.domain.model.business

data class Patient(
    override val id: Int = 0,
    override val email: String,
    override val firstName: String = "",
    override val lastName: String = "",
    override val password: String,
    override val role: Int = -1,
    val phone: String = "",
    val ssn: String = "",
) : User {
    override val type: UserType = UserType.PATIENT

    companion object {
        val EMPTY_PATIENT = Patient(email = "", password = "")
    }
}