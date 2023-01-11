package rs.ac.bg.etf.diplomski.medsched.presentation.patient.events

sealed class EditProfileEvent {
    data class ChangeEmailText(val text: String): EditProfileEvent()
    object ConfirmNewEmail: EditProfileEvent()
    object ResetUpdateEmailMessage: EditProfileEvent()

    data class ChangeOldPasswordText(val text: String): EditProfileEvent()
    data class ChangeNewPasswordText(val text: String): EditProfileEvent()
    data class ChangeConfirmNewPasswordText(val text: String): EditProfileEvent()
    object ConfirmNewPassword: EditProfileEvent()
}