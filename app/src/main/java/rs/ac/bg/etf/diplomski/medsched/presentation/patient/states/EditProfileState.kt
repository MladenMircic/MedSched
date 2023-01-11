package rs.ac.bg.etf.diplomski.medsched.presentation.patient.states

data class EditProfileState(
    val newEmail: String = "",
    val newEmailErrorId: Int? = null,
    val oldPassword: String = "",
    val oldPasswordErrorId: Int? = null,
    val newPassword: String = "",
    val newPasswordErrorId: Int? = null,
    val confirmNewPassword: String = "",
    val confirmNewPasswordErrorId: Int? = null,
    val isInProgress: Boolean = false,
    val updateMessageId: Int? = null,
)