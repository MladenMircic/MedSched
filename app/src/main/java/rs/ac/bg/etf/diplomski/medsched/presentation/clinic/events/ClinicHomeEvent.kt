package rs.ac.bg.etf.diplomski.medsched.presentation.clinic.events

sealed class ClinicHomeEvent {
    data class SearchTextChange(val text: String): ClinicHomeEvent()
    data class CategorySelect(val index: Int): ClinicHomeEvent()
    object SearchForDoctor: ClinicHomeEvent()
}
