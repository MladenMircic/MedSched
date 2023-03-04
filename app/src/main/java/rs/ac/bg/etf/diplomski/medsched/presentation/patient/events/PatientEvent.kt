package rs.ac.bg.etf.diplomski.medsched.presentation.patient.events

import kotlinx.datetime.LocalDate

sealed class PatientEvent {
    data class SelectCategory(val index: Int): PatientEvent()
    data class SearchTextChange(val text: String): PatientEvent()
    data class SelectDoctor(val index: Int?): PatientEvent()
    data class SetAppointmentDate(val date: LocalDate?): PatientEvent()
    data class SetAppointmentExamNameId(val nameId: Int): PatientEvent()
    data class SetAppointmentTime(val timeIndex: Int): PatientEvent()
    data class UpdateNotificationsRead(val indices: List<Int>): PatientEvent()
    object ClearAvailableHours: PatientEvent()
    object ScheduleAppointment: PatientEvent()
    object SetScheduleMessageNull: PatientEvent()
    object SearchForDoctor: PatientEvent()
    object GetAllCategories: PatientEvent()
    object ToggleDoctorsClinics: PatientEvent()
    object ClearCurrentShowingList: PatientEvent()
}
