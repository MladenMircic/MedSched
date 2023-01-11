package rs.ac.bg.etf.diplomski.medsched.domain.use_case.patient

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.commons.Resource
import rs.ac.bg.etf.diplomski.medsched.domain.model.request.EmailChangeRequest
import rs.ac.bg.etf.diplomski.medsched.domain.repository.PatientRepository
import javax.inject.Inject

class UpdateEmailUseCase @Inject constructor(
    private val patientRepository: PatientRepository
) {

    suspend operator fun invoke(
        emailChangeRequest: EmailChangeRequest
    ): Flow<Resource<Int>> = flow {
        emit(Resource.Loading())
        try {
            patientRepository.updateEmail(emailChangeRequest)
            emit(Resource.Success(R.string.email_update_success))
        } catch (e: HttpException) {
            emit(Resource.Error(R.string.email_update_failure))
        } catch (e: IOException) {
            emit(Resource.Error(R.string.no_connection))
        }
    }
}