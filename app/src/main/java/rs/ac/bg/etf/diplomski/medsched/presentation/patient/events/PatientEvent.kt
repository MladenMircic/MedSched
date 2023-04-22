package rs.ac.bg.etf.diplomski.medsched.presentation.patient.events

sealed class PatientEvent {
    data class SelectCategory(val index: Int): PatientEvent()
    data class SearchTextChange(val text: String): PatientEvent()
    data class SelectDoctor(val index: Int?): PatientEvent()
    data class UpdateNotificationsRead(val indices: List<Int>): PatientEvent()
    object SearchForDoctor: PatientEvent()
    object GetAllCategories: PatientEvent()
    object ToggleDoctorsClinics: PatientEvent()
    object ClearCurrentShowingList: PatientEvent()
}
