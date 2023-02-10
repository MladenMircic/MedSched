package rs.ac.bg.etf.diplomski.medsched.domain.model.business

sealed interface User {
    val type: Role
    val id: Int
    val email: String
    val firstName: String
    val lastName: String
    val password: String
    val role: Int
}

enum class Role {
    DOCTOR, PATIENT, CLINIC, EMPTY
}