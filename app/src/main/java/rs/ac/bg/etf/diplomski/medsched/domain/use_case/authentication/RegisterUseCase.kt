package rs.ac.bg.etf.diplomski.medsched.domain.use_case.authentication

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.commons.Resource
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.User
import rs.ac.bg.etf.diplomski.medsched.domain.model.response.RegisterResponse
import rs.ac.bg.etf.diplomski.medsched.domain.repository.LoginRegisterRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val loginRegisterRepository: LoginRegisterRepository
) {
    suspend operator fun invoke(user: User): Flow<Resource<RegisterResponse>> = flow {
        emit(Resource.Loading())
        try {
            val response = loginRegisterRepository.registerUser(user)
            when (response.success) {
                true -> emit(Resource.Success(response))
                false -> {
                    when (response.accountExists) {
                        true -> {
                            emit(Resource.Error(R.string.account_already_exists))
                        }
                        false -> emit(Resource.Error(R.string.unknown_error))
                    }
                }
            }
        } catch (e: HttpException) {
            emit(Resource.Error(R.string.unknown_error))
        } catch (e: IOException) {
            emit(Resource.Error(R.string.no_connection))
        }
    }
}