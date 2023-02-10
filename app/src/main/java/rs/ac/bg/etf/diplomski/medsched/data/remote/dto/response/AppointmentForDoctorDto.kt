package rs.ac.bg.etf.diplomski.medsched.data.remote.dto.response

import com.squareup.moshi.JsonClass
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Appointment
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.AppointmentForDoctor

@JsonClass(generateAdapter = true)
data class AppointmentForDoctorDto(
    val patientName: String,
    val appointment: Appointment
) {
    fun toAppointmentForDoctor(): AppointmentForDoctor {
        return AppointmentForDoctor(
            patientName = patientName,
            appointment = appointment
        )
    }
}