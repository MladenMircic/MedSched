package rs.ac.bg.etf.diplomski.medsched.data.remote.dto.response

import com.squareup.moshi.JsonClass
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Appointment
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.AppointmentForPatient

@JsonClass(generateAdapter = true)
data class AppointmentForPatientDto(
    val doctorName: String,
    val doctorSpecializationId: Int,
    val appointment: Appointment
) {
    fun toAppointmentForPatient(): AppointmentForPatient {
        return AppointmentForPatient(
            doctorName = doctorName,
            doctorSpecializationId = doctorSpecializationId,
            appointment = appointment
        )
    }
}