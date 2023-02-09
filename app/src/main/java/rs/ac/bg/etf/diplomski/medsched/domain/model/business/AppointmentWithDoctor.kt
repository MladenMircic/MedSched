package rs.ac.bg.etf.diplomski.medsched.domain.model.business

import coil.request.ImageRequest
import rs.ac.bg.etf.diplomski.medsched.domain.model.entities.AppointmentEntity
import java.util.*

data class AppointmentWithDoctor(
    val doctorName: String,
    val doctorSpecializationId: Int,
    var doctorImageRequest: ImageRequest? = null,
    val appointment: Appointment
) {
    fun dateAsString(): String {
        return String.format(
            "%s %d, %d",
            appointment.date.month.name.lowercase()
                .replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                }
                .substring(0, 3),
            appointment.date.dayOfMonth,
            appointment.date.year
        )
    }

    fun timeAsString(): String {
        return String.format(
            format = "%02d:%02d",
            appointment.time.hour,
            appointment.time.minute
        )
    }

    fun toAppointmentEntity(): AppointmentEntity {
        return AppointmentEntity(
            date = appointment.date,
            time = appointment.time,
            appointmentId = appointment.id,
            doctorId = appointment.doctorId,
            doctorName = doctorName,
            doctorSpecializationId = doctorSpecializationId,
            patientId = appointment.patientId,
            examId = appointment.examId,
            confirmed = appointment.confirmed
        )
    }
}