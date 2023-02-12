package rs.ac.bg.etf.diplomski.medsched.domain.use_case.clinic

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.commons.Resource
import rs.ac.bg.etf.diplomski.medsched.domain.model.request.DoctorRegisterRequest
import rs.ac.bg.etf.diplomski.medsched.domain.repository.ClinicRepository
import javax.inject.Inject

class RegisterDoctorUseCase @Inject constructor(
    private val clinicRepository: ClinicRepository
) {
    suspend operator fun invoke(
        doctorRegisterRequest: DoctorRegisterRequest
    ): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        try {
            clinicRepository.registerDoctor(doctorRegisterRequest)
            emit(Resource.Success(Unit))
        } catch (e: HttpException) {
            emit(Resource.Error(R.string.unknown_error))
        } catch (e: IOException) {
            emit(Resource.Error(R.string.no_connection))
        }
    }
}