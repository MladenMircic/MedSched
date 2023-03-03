package rs.ac.bg.etf.diplomski.medsched.data.remote.dto.response

import com.squareup.moshi.JsonClass
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Appointment

@JsonClass(generateAdapter = true)
data class AppointmentDto(
    val id: Int,
    val date: LocalDate,
    val time: LocalTime,
    val doctorId: String,
    val patientId: String,
    val examId: Int,
    val confirmed: Boolean,
    val cancelledBy: Int
) {

    fun toAppointment(): Appointment {
        return Appointment(
            id = id,
            date = date,
            time = time,
            doctorId = doctorId,
            patientId = patientId,
            examId = examId,
            confirmed = confirmed,
            cancelledBy = cancelledBy
        )
    }
}