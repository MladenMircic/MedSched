package rs.ac.bg.etf.diplomski.medsched.domain.model.business

import coil.request.ImageRequest
import java.util.*

data class Scheduled(
    val doctorName: String,
    val doctorSpecializationId: Int,
    var doctorImageRequest: ImageRequest? = null,
    val appointment: Appointment
) {
    fun dateAsString(): String {
        return String.format(
            "%s %d, %d",
            appointment.date.month.name.lowercase()
                .replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                },
            appointment.date.dayOfMonth,
            appointment.date.year
        )
    }

    fun timeAsString(): String {
        return String.format(
            format = "%02d:%02d",
            appointment.time.hour,
            appointment.time.minute
        )
    }
}