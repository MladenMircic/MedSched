package rs.ac.bg.etf.diplomski.medsched.presentation.patient.states

import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Service

data class AppointmentState(
    val serviceList: List<Service> = listOf(),
    val examNameIdList: List<Int> = listOf(),
    val currentExamNameId: Int? = null,
    val selectedDate: LocalDate = LocalDate.now(),
    val availableTimes: List<LocalTime> = listOf(),
    val selectedTime: Int = -1,
    val scheduledMessageId: Int? = null
)