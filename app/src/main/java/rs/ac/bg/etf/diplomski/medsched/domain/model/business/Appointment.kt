package rs.ac.bg.etf.diplomski.medsched.domain.model.business

import com.squareup.moshi.JsonClass
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import kotlinx.datetime.*

@JsonClass(generateAdapter = true)
data class Appointment(
    val id: Int = 0,
    val date: LocalDate = LocalDate.now(),
    val time: LocalTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).time,
    val doctorId: String,
    val patientId: String,
    val examId: Int,
    val confirmed: Boolean,
    val cancelledBy: Int
)