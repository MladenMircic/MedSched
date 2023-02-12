package rs.ac.bg.etf.diplomski.medsched.domain.model.business

class EmptyUser private constructor(
    override val id: Int = 0,
    override val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    override val password: String = "",
    override val role: Int = -1
) : User {
    override val type: Role = Role.EMPTY
    companion object {
        val instance = EmptyUser()
    }
}