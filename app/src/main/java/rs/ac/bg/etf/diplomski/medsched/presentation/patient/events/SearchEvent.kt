package rs.ac.bg.etf.diplomski.medsched.presentation.patient.events

sealed class SearchEvent {
    data class UpdateSearchTextAndTriggerSearch(val text: String): SearchEvent()
    data class SetCategoryFilter(val categoryId: Int): SearchEvent()
    data class SetSelectedPage(val page: Int): SearchEvent()
    data class SelectDoctorForSchedule(val index: Int?): SearchEvent()
    data class SetSelectingMode(val value: Boolean): SearchEvent()
    data class ToggleMultipleDoctorSelected(val index: Int): SearchEvent()
    object ClearSelection: SearchEvent()
    object ApplyFilter: SearchEvent()
    object ClearFilter: SearchEvent()
}