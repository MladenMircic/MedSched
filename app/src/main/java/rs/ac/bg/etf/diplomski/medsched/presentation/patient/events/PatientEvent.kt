package rs.ac.bg.etf.diplomski.medsched.presentation.patient.events

import kotlinx.datetime.LocalDate

sealed class PatientEvent {
    data class SelectService(val index: Int): PatientEvent()
    data class SearchTextChange(val text: String): PatientEvent()
    data class SelectDoctor(val index: Int?): PatientEvent()
    data class SetAppointmentDate(val date: LocalDate?): PatientEvent()
    data class SetAppointmentExamName(val name: String): PatientEvent()
    data class SetAppointmentTime(val timeIndex: Int): PatientEvent()
    object ScheduleAppointment: PatientEvent()
    object SearchForDoctor: PatientEvent()
    object GetAllServices: PatientEvent()
}
