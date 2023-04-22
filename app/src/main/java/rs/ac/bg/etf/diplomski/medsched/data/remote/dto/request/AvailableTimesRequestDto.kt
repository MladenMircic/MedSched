package rs.ac.bg.etf.diplomski.medsched.data.remote.dto.request

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AvailableTimesRequestDto(
    val doctorIds: List<String>,
    val patientId: String
)