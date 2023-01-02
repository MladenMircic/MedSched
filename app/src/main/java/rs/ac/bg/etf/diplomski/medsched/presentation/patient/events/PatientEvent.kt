package rs.ac.bg.etf.diplomski.medsched.presentation.patient.events

sealed class PatientEvent {
    data class SelectService(val index: Int): PatientEvent()
    data class SearchTextChange(val text: String): PatientEvent()
    data class SelectDoctor(val index: Int): PatientEvent()
    object SearchForDoctor: PatientEvent()
    object GetAllServices: PatientEvent()
}
