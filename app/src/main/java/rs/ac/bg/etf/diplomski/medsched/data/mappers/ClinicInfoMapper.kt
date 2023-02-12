package rs.ac.bg.etf.diplomski.medsched.data.mappers

import rs.ac.bg.etf.diplomski.medsched.data.remote.dto.request.DoctorRegisterRequestDto
import rs.ac.bg.etf.diplomski.medsched.domain.model.request.DoctorRegisterRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClinicInfoMapper @Inject constructor() {

    fun toDoctorRegisterRequestDto(
        doctorRegisterRequest: DoctorRegisterRequest
    ): DoctorRegisterRequestDto {
        return DoctorRegisterRequestDto(
            email = doctorRegisterRequest.email,
            password = doctorRegisterRequest.password,
            firstName = doctorRegisterRequest.firstName,
            lastName = doctorRegisterRequest.lastName,
            phone = doctorRegisterRequest.phone,
            categoryId = doctorRegisterRequest.categoryId,
            specializationId = doctorRegisterRequest.specializationId,
            workDays = doctorRegisterRequest.workDays
        )
    }
}