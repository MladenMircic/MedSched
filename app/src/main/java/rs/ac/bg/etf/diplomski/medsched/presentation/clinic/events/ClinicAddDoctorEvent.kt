package rs.ac.bg.etf.diplomski.medsched.presentation.clinic.events

sealed class ClinicAddDoctorEvent {
    data class SetEmailField(val text: String): ClinicAddDoctorEvent()
    data class SetFirstNameField(val text: String): ClinicAddDoctorEvent()
    data class SetLastNameField(val text: String): ClinicAddDoctorEvent()
    data class SetPasswordField(val text: String): ClinicAddDoctorEvent()
    data class SetConfirmPasswordField(val text: String): ClinicAddDoctorEvent()
    data class SetPhoneField(val text: String): ClinicAddDoctorEvent()
    data class SetCategoryField(val index: Int): ClinicAddDoctorEvent()
    data class SetSpecializationField(val index: Int): ClinicAddDoctorEvent()
    data class SetSelectedWorkDay(val index: Int): ClinicAddDoctorEvent()
    data class SetHasError(val hasError: Boolean): ClinicAddDoctorEvent()
    object RegisterDoctor: ClinicAddDoctorEvent()
}