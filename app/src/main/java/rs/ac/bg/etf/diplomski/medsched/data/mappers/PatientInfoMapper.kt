package rs.ac.bg.etf.diplomski.medsched.data.mappers

import rs.ac.bg.etf.diplomski.medsched.data.remote.dto.request.AppointmentsRequestDto
import rs.ac.bg.etf.diplomski.medsched.data.remote.dto.request.EmailChangeRequestDto
import rs.ac.bg.etf.diplomski.medsched.data.remote.dto.request.InfoChangeRequestDto
import rs.ac.bg.etf.diplomski.medsched.data.remote.dto.request.PasswordChangeRequestDto
import rs.ac.bg.etf.diplomski.medsched.data.remote.dto.response.AppointmentDto
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Appointment
import rs.ac.bg.etf.diplomski.medsched.domain.model.request.AppointmentRequest
import rs.ac.bg.etf.diplomski.medsched.domain.model.request.EmailChangeRequest
import rs.ac.bg.etf.diplomski.medsched.domain.model.request.InfoChangeRequest
import rs.ac.bg.etf.diplomski.medsched.domain.model.request.PasswordChangeRequest
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
            examId = appointment.examId,
            confirmed = appointment.confirmed,
            cancelledBy = appointment.cancelledBy
        )
    }

    fun toEmailChangeRequestDto(
        emailChangeRequest: EmailChangeRequest
    ): EmailChangeRequestDto {
        return EmailChangeRequestDto(
            email = emailChangeRequest.email
        )
    }

    fun toPasswordChangeRequestDto(
        passwordChangeRequest: PasswordChangeRequest
    ): PasswordChangeRequestDto {
        return PasswordChangeRequestDto(
            oldPassword = passwordChangeRequest.oldPassword,
            newPassword = passwordChangeRequest.newPassword
        )
    }

    fun toInfoChangeRequestDto(infoChangeRequest: InfoChangeRequest): InfoChangeRequestDto {
        return InfoChangeRequestDto(
            firstName = infoChangeRequest.firstName,
            lastName = infoChangeRequest.lastName,
            phone = infoChangeRequest.phone,
            ssn = infoChangeRequest.ssn
        )
    }
}