package rs.ac.bg.etf.diplomski.medsched.domain.model.business

import kotlinx.datetime.LocalTime

data class Clinic(
    override val id: Int = 0,
    override val email: String = "",
    override val password: String = "",
    override val role: Int = Role.CLINIC.ordinal,
    val name: String,
    val openingTime: LocalTime,
    val workHours: Int
): User {
    override val type: Role = Role.CLINIC
}