package rs.ac.bg.etf.diplomski.medsched.domain.model.business

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalTime

data class DoctorWorkTime(
    val doctorId: String,
    val clinicId: String,
    val dayOfWeek: DayOfWeek,
    val time: LocalTime
)