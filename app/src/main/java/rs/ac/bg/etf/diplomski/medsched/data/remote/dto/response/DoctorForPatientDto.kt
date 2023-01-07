package rs.ac.bg.etf.diplomski.medsched.data.remote.dto.response

import rs.ac.bg.etf.diplomski.medsched.domain.model.business.DoctorForPatient

data class DoctorForPatientDto(
    val id: Int,
    val email: String,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val service: String
) {
    fun toDoctorForPatient(): DoctorForPatient {
        return DoctorForPatient(
            id = id,
            email = email,
            firstName = firstName,
            lastName = lastName,
            phone = phone,
            service = service
        )
    }
}