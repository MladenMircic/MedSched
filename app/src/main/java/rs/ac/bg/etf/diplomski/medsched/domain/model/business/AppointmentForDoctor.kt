package rs.ac.bg.etf.diplomski.medsched.domain.model.business

import rs.ac.bg.etf.diplomski.medsched.domain.model.entities.AppointmentForDoctorEntity

data class AppointmentForDoctor(
    val patientName: String,
    val appointment: Appointment
) {
    fun toAppointmentForDoctorEntity(): AppointmentForDoctorEntity {
        return AppointmentForDoctorEntity(
            date = appointment.date,
            time = appointment.time,
            appointmentId = appointment.id,
            doctorId = appointment.doctorId,
            patientName = patientName,
            patientId = appointment.patientId,
            examId = appointment.examId,
            confirmed = appointment.confirmed,
            cancelledBy = appointment.cancelledBy
        )
    }
}