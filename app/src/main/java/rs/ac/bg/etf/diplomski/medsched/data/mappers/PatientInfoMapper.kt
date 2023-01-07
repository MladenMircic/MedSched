package rs.ac.bg.etf.diplomski.medsched.data.mappers

import rs.ac.bg.etf.diplomski.medsched.data.remote.dto.AppointmentDto
import rs.ac.bg.etf.diplomski.medsched.data.remote.dto.request.AppointmentsRequestDto
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Appointment
import rs.ac.bg.etf.diplomski.medsched.domain.model.request.AppointmentRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PatientInfoMapper @Inject constructor() {

    fun toAppointmentRequestDto(appointmentRequest: AppointmentRequest): AppointmentsRequestDto {
        return AppointmentsRequestDto(
            appointmentRequest.doctorId,
            appointmentRequest.date
        )
    }

    fun toAppointmentDto(appointment: Appointment): AppointmentDto {
        return AppointmentDto(
            id = appointment.id,
            date = appointment.date,
            time = appointment.time,
            doctorId = appointment.doctorId,
            patientId = appointment.patientId,
            examName = appointment.examName
        )
    }
}