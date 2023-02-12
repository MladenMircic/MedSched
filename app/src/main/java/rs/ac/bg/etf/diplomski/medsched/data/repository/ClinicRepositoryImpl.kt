package rs.ac.bg.etf.diplomski.medsched.data.repository

import rs.ac.bg.etf.diplomski.medsched.data.mappers.ClinicInfoMapper
import rs.ac.bg.etf.diplomski.medsched.data.remote.ClinicApi
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Category
import rs.ac.bg.etf.diplomski.medsched.domain.model.request.DoctorRegisterRequest
import rs.ac.bg.etf.diplomski.medsched.domain.repository.ClinicRepository

class ClinicRepositoryImpl(
    private val clinicApi: ClinicApi,
    private val clinicInfoMapper: ClinicInfoMapper
): ClinicRepository {

    override suspend fun getAllCategories(): List<Category> =
        clinicApi.getAllCategories().map { it.toService() }

    override suspend fun registerDoctor(doctorRegisterRequest: DoctorRegisterRequest) =
        clinicApi.registerDoctor(
            clinicInfoMapper.toDoctorRegisterRequestDto(doctorRegisterRequest)
        )

}