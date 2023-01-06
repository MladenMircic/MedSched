package rs.ac.bg.etf.diplomski.medsched.data.mappers

import rs.ac.bg.etf.diplomski.medsched.data.remote.dto.request.AppointmentsRequestDto
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
}