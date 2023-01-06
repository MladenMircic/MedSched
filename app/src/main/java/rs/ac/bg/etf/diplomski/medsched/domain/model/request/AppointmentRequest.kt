package rs.ac.bg.etf.diplomski.medsched.domain.model.request

import kotlinx.datetime.LocalDate

data class AppointmentRequest(
    val doctorId: Int,
    val date: LocalDate
)