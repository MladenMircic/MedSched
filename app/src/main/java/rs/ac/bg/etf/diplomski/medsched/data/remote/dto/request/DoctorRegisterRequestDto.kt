package rs.ac.bg.etf.diplomski.medsched.data.remote.dto.request

import com.squareup.moshi.JsonClass
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.WorkDay

@JsonClass(generateAdapter = true)
data class DoctorRegisterRequestDto(
    val email: String,
    var password: String,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val categoryId: Int,
    val specializationId: Int,
    val workDays: List<WorkDay>
)