package rs.ac.bg.etf.diplomski.medsched.domain.model.business

sealed interface User {
    val type: UserType
    val email: String
    val firstName: String
    val lastName: String
    val password: String
    val role: Int
}

enum class UserType {
    PATIENT, DOCTOR, CLINIC
}