package rs.ac.bg.etf.diplomski.medsched.presentation.patient.states

data class EditProfileState(
    val newEmailText: String = "",
    val newEmailErrorId: Int? = null,
    val oldPasswordText: String = "",
    val oldPasswordErrorId: Int? = null,
    val newPasswordText: String = "",
    val newPasswordErrorId: Int? = null,
    val confirmNewPasswordText: String = "",
    val confirmNewPasswordErrorId: Int? = null,
    val firstNameText: String = "",
    val firstNameErrorId: Int? = null,
    val lastNameText: String = "",
    val lastNameErrorId: Int? = null,
    val phoneText: String = "",
    val phoneErrorId: Int? = null,
    val ssnText: String = "",
    val ssnErrorId: Int? = null,
    val isInProgress: Boolean = false,
    val updateMessageId: Int? = null,
)