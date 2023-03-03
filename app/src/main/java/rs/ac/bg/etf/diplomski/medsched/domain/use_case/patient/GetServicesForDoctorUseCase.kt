package rs.ac.bg.etf.diplomski.medsched.domain.use_case.patient

import okio.IOException
import retrofit2.HttpException
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.commons.Resource
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Service
import rs.ac.bg.etf.diplomski.medsched.domain.repository.PatientRepository
import javax.inject.Inject

class GetServicesForDoctorUseCase @Inject constructor(
    private val patientRepository: PatientRepository
) {
    suspend operator fun invoke(doctorId: String): Resource<List<Service>> =
        try {
            Resource.Success(patientRepository.getAllServicesForDoctor(doctorId))
        } catch (e: HttpException) {
            Resource.Error(R.string.unknown_error)
        } catch (e: IOException) {
            Resource.Error(R.string.no_connection)
        }
}