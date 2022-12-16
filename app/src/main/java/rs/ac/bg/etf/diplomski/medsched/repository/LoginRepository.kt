package rs.ac.bg.etf.diplomski.medsched.repository

import okio.IOException
import retrofit2.HttpException
import rs.ac.bg.etf.diplomski.medsched.data.remote.LoginApiService
import rs.ac.bg.etf.diplomski.medsched.data.remote.entities.UserEntity
import rs.ac.bg.etf.diplomski.medsched.data.remote.responses.MessageResponse
import rs.ac.bg.etf.diplomski.medsched.utils.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepository @Inject constructor(private val loginApiService: LoginApiService) {

    suspend fun loginUser(userEntity: UserEntity): Resource<MessageResponse> {
        val response = try {
            loginApiService.loginUser(userEntity)
        } catch (e: HttpException) {
            return Resource.Error(e.message())
        } catch (e: IOException) {
            return Resource.Error("No connection to server!")
        }
        return Resource.Success(response)
    }
}