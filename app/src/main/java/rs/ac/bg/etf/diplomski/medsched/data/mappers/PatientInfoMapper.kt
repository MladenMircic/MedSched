package rs.ac.bg.etf.diplomski.medsched.data.mappers

import rs.ac.bg.etf.diplomski.medsched.data.remote.dto.request.AvailableTimesRequestDto
import rs.ac.bg.etf.diplomski.medsched.data.remote.dto.request.EmailChangeRequestDto
import rs.ac.bg.etf.diplomski.medsched.data.remote.dto.request.InfoChangeRequestDto
import rs.ac.bg.etf.diplomski.medsched.data.remote.dto.request.PasswordChangeRequestDto
import rs.ac.bg.etf.diplomski.medsched.data.remote.dto.response.AppointmentDto
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Appointment
import rs.ac.bg.etf.diplomski.medsched.domain.model.request.AvailableTimesRequest
import rs.ac.bg.etf.diplomski.medsched.domain.model.request.EmailChangeRequest
import rs.ac.bg.etf.diplomski.medsched.domain.model.request.InfoChangeRequest
import rs.ac.bg.etf.diplomski.medsched.domain.model.request.PasswordChangeRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PatientInfoMapper @Inject constructor() {

    fun toAvailableTimesRequestDto(
        availableTimesRequest: AvailableTimesRequest
    ): AvailableTimesRequestDto {
        return AvailableTimesRequestDto(
            doctorIds = availableTimesRequest.doctorIds,
            patientId = availableTimesRequest.patientId
        )
    }

    fun toAppointmentDto(appointment: Appointment): AppointmentDto {
        return AppointmentDto(
            id = appointment.id,
            date = appointment.date,
            time = appointment.time,
            doctorId = appointment.doctorId,
            patientId = appointment.patientId,
            confirmed = appointment.confirmed,
            services = appointment.services,
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