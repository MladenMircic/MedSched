package rs.ac.bg.etf.diplomski.medsched.domain.model.request

data class PasswordChangeRequest(
    val oldPassword: String,
    val newPassword: String
)