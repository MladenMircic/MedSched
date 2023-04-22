package rs.ac.bg.etf.diplomski.medsched.data.remote.dto.response

import com.squareup.moshi.JsonClass
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Appointment
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.DoctorWorkTime
import rs.ac.bg.etf.diplomski.medsched.domain.model.response.AvailableTimesResponse

@JsonClass(generateAdapter = true)
data class AvailableTimesResponseDto(
    val doctorWorkTimes: List<DoctorWorkTime>,
    val scheduledAppointments: List<Appointment>
) {
    fun toAvailableTimesResponse(): AvailableTimesResponse {
        return AvailableTimesResponse(
            doctorWorkTimes = doctorWorkTimes,
            scheduledAppointments = scheduledAppointments
        )
    }
}