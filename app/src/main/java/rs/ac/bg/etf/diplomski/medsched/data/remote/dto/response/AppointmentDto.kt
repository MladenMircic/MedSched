package rs.ac.bg.etf.diplomski.medsched.data.remote.dto.response

import com.squareup.moshi.JsonClass
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Appointment
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Service

@JsonClass(generateAdapter = true)
data class AppointmentDto(
    val id: Int,
    val date: LocalDate,
    val time: LocalTime,
    val doctorId: String,
    val patientId: String,
    val confirmed: Boolean,
    val services: List<Service>,
    val cancelledBy: Int
) {

    fun toAppointment(): Appointment {
        return Appointment(
            id = id,
            date = date,
            time = time,
            doctorId = doctorId,
            patientId = patientId,
            confirmed = confirmed,
            services = services,
            cancelledBy = cancelledBy
        )
    }
}