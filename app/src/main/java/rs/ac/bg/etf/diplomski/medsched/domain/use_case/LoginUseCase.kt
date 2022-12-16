package rs.ac.bg.etf.diplomski.medsched.domain.use_case

import okio.IOException
import retrofit2.HttpException
import rs.ac.bg.etf.diplomski.medsched.commons.Resource
import rs.ac.bg.etf.diplomski.medsched.domain.model.User
import rs.ac.bg.etf.diplomski.medsched.domain.model.UserLogin
import rs.ac.bg.etf.diplomski.medsched.domain.repository.LoginRegisterRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val loginRegisterRepository: LoginRegisterRepository
) {
    suspend operator fun invoke(user: User): Resource<UserLogin> {
        return try {
            Resource.Success(loginRegisterRepository.loginUser(user))
        } catch (e: HttpException) {
            Resource.Error("User not found")
        } catch (e: IOException) {
            Resource.Error("No connection to the server")
        }
    }
}