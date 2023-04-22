package rs.ac.bg.etf.diplomski.medsched.presentation.patient.events

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.DoctorForPatient
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Service

sealed class DoctorScheduleEvent {
    class SetSelectedDoctorIndex(val index: Int): DoctorScheduleEvent()
    data class ToggleSelectedService(val service: Service): DoctorScheduleEvent()
    data class SetCurrentMonth(val month: Month): DoctorScheduleEvent()
    data class SetAppointmentExamNameId(val nameId: Int): DoctorScheduleEvent()
    data class SetCurrentDate(val date: LocalDate?): DoctorScheduleEvent()
    data class SetAppointmentTime(val time: LocalTime): DoctorScheduleEvent()
    class ScheduleAppointment(val doctors: List<DoctorForPatient>) : DoctorScheduleEvent()
    object SetScheduleMessageNull: DoctorScheduleEvent()
    object SetServicesLoading: DoctorScheduleEvent()
}