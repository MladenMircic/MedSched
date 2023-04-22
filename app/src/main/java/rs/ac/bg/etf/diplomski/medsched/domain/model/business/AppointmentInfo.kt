package rs.ac.bg.etf.diplomski.medsched.domain.model.business

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month

data class AppointmentInfo(
    val allMonthsDates: Map<Month, List<LocalDate>> = mapOf(),
    val selectedServicesList: List<Service> = listOf(),
    val selectedDate: LocalDate? = null,
    val selectedTime: LocalTime? = null,
    val isOnSelectedMonth: Boolean = true,
)