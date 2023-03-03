package rs.ac.bg.etf.diplomski.medsched.domain.model.business

sealed interface User {
    val type: Role
    val id: String
    val email: String
    val password: String
    val role: Int
}

enum class Role {
    DOCTOR, PATIENT, CLINIC, EMPTY
}