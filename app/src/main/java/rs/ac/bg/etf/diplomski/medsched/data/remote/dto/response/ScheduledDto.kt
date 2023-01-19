package rs.ac.bg.etf.diplomski.medsched.data.remote.dto.response

import com.squareup.moshi.JsonClass
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Appointment
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Scheduled

@JsonClass(generateAdapter = true)
data class ScheduledDto(
    val doctorName: String,
    val doctorSpecializationId: Int,
    val appointment: Appointment
) {
    fun toScheduled(): Scheduled {
        return Scheduled(
            doctorName = doctorName,
            doctorSpecializationId = doctorSpecializationId,
            appointment = appointment
        )
    }
}