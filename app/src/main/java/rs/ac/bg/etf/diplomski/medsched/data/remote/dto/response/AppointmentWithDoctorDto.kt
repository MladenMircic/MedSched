package rs.ac.bg.etf.diplomski.medsched.data.remote.dto.response

import com.squareup.moshi.JsonClass
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Appointment
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.AppointmentWithDoctor

@JsonClass(generateAdapter = true)
data class AppointmentWithDoctorDto(
    val doctorName: String,
    val doctorSpecializationId: Int,
    val appointment: Appointment
) {
    fun toAppointmentWithDoctor(): AppointmentWithDoctor {
        return AppointmentWithDoctor(
            doctorName = doctorName,
            doctorSpecializationId = doctorSpecializationId,
            appointment = appointment
        )
    }
}