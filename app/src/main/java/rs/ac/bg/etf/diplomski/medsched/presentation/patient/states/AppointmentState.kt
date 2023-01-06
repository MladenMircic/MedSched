package rs.ac.bg.etf.diplomski.medsched.presentation.patient.states

import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import kotlinx.datetime.LocalDate

data class AppointmentState(
    val selectedDate: LocalDate = LocalDate.now(),
    val selectedTime: String? = null
)