package rs.ac.bg.etf.diplomski.medsched.domain.use_case

import okio.IOException
import retrofit2.HttpException
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.commons.Resource
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.User
import rs.ac.bg.etf.diplomski.medsched.domain.model.response.LoginResponse
import rs.ac.bg.etf.diplomski.medsched.domain.repository.LoginRegisterRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val loginRegisterRepository: LoginRegisterRepository
) {
    suspend operator fun invoke(user: User): Resource<LoginResponse> {
        return try {
            Resource.Success(loginRegisterRepository.loginUser(user))
        } catch (e: HttpException) {
            Resource.Error(R.string.unknown_error)
        } catch (e: IOException) {
            Resource.Error(R.string.no_connection)
        }
    }
}