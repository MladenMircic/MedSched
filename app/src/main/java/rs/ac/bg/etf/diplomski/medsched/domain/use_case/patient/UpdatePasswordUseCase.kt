package rs.ac.bg.etf.diplomski.medsched.domain.use_case.patient

import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import okio.IOException
import retrofit2.HttpException
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.commons.Resource
import rs.ac.bg.etf.diplomski.medsched.domain.model.request.PasswordChangeRequest
import rs.ac.bg.etf.diplomski.medsched.domain.model.response.PasswordChangeResponse
import rs.ac.bg.etf.diplomski.medsched.domain.repository.PatientRepository
import javax.inject.Inject

class UpdatePasswordUseCase @Inject constructor(
    private val patientRepository: PatientRepository,
    private val moshi: Moshi
) {
    suspend operator fun invoke(
        passwordChangeRequest: PasswordChangeRequest
    ): Flow<Resource<PasswordChangeResponse>> = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(patientRepository.updatePassword(passwordChangeRequest)))
        } catch (e: HttpException) {
            val errorBodyReader = e.response()?.errorBody()?.charStream()
            errorBodyReader?.let { reader ->
                val passwordChangeResponse = moshi.adapter(PasswordChangeResponse::class.java)
                    .fromJson(reader.readText())!!
                if (!passwordChangeResponse.oldPasswordCorrect) {
                    emit(Resource.Error(R.string.password_error_login))
                } else {
                    emit(Resource.Error(R.string.password_update_failure))
                }
            }
            withContext(Dispatchers.IO) { errorBodyReader?.close() }
        } catch (e: IOException) {
            emit(Resource.Error(R.string.no_connection))
        }
    }
}