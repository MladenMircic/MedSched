package rs.ac.bg.etf.diplomski.medsched.data.remote.dto.response

import com.squareup.moshi.JsonClass
import kotlinx.datetime.LocalTime
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.ClinicForPatient

@JsonClass(generateAdapter = true)
data class ClinicForPatientDto(
    val id: String,
    val email: String,
    val name: String,
    val openingTime: LocalTime,
    val workHours: Int,
    var doctors: List<DoctorForPatientDto>
) {
    fun toClinicForPatient(): ClinicForPatient {
        return ClinicForPatient(
            id = id,
            email = email,
            name = name,
            openingTime = openingTime,
            workHours = workHours,
            doctors = doctors.map { it.toDoctorForPatient() }
        )
    }
}