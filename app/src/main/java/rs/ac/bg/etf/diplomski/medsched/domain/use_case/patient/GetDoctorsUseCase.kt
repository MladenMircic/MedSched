package rs.ac.bg.etf.diplomski.medsched.domain.use_case.patient

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.commons.Resource
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.DoctorForPatient
import rs.ac.bg.etf.diplomski.medsched.domain.repository.PatientRepository
import javax.inject.Inject

class GetDoctorsUseCase @Inject constructor(
    private val patientRepository: PatientRepository
) {
    operator fun invoke(category: String) : Flow<Resource<List<DoctorForPatient>>> = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(patientRepository.getDoctors(category)))
        } catch (e: HttpException) {
            emit(Resource.Error(R.string.unknown_error))
        } catch (e: IOException) {
            emit(Resource.Error(R.string.no_connection))
        }
    }
}