package rs.ac.bg.etf.diplomski.medsched.domain.model.business

import coil.request.ImageRequest
import rs.ac.bg.etf.diplomski.medsched.domain.model.entities.AppointmentForPatientEntity

data class AppointmentForPatient(
    val doctorName: String,
    val doctorSpecializationId: Int,
    var doctorImageRequest: ImageRequest? = null,
    val appointment: Appointment
) {
    fun toAppointmentForPatientEntity(): AppointmentForPatientEntity {
        return AppointmentForPatientEntity(
            date = appointment.date,
            time = appointment.time,
            appointmentId = appointment.id,
            doctorId = appointment.doctorId,
            doctorName = doctorName,
            doctorSpecializationId = doctorSpecializationId,
            patientId = appointment.patientId,
            examId = appointment.examId,
            confirmed = appointment.confirmed,
            cancelledBy = appointment.cancelledBy
        )
    }
}