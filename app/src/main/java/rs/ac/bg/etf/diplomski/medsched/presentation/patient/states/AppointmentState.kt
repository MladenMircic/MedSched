package rs.ac.bg.etf.diplomski.medsched.presentation.patient.states

import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Appointment
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.AppointmentInfo
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.DoctorWorkTime
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Service

data class AppointmentState(
    val doctorIds: List<String> = listOf(),
    val selectedDoctorIndex: Int = 0,
    val availableServicesList: List<List<Service>> = listOf(),
    val selectedServicesIndex: Int = 0,
    val servicesLoading: Boolean = false,
    val examNameIdList: List<Int> = listOf(),
    val currentExamNameId: Int? = null,
    val currentMonth: Month = LocalDate.now().month,
    val scheduledAppointments: List<Appointment> = listOf(),
    val availableDates: List<LocalDate> = listOf(),
    val allAvailableTimesList: List<DoctorWorkTime> = listOf(),
    val currentAvailableTimesList: List<LocalTime> = listOf(),
    val appointmentInfoList: List<AppointmentInfo> = listOf(),
    val scheduledMessageId: Int? = null
)