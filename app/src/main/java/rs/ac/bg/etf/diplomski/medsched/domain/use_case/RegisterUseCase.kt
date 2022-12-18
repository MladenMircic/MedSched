package rs.ac.bg.etf.diplomski.medsched.domain.use_case

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
    suspend operator fun invoke(user: User): Resource<RegisterResponse> {
        return try {
            val result = loginRegisterRepository.registerUser(user)
            when (result.success) {
                true -> Resource.Success(result)
                false -> {
                    when (result.accountExists) {
                        true -> Resource.Error(R.string.account_already_exists)
                        false -> Resource.Error(R.string.unknown_error)
                    }
                }
            }
        } catch (e: HttpException) {
            Resource.Error(R.string.unknown_error)
        } catch (e: IOException) {
            Resource.Error(R.string.no_connection)
        }
    }
}