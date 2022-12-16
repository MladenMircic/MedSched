package rs.ac.bg.etf.diplomski.medsched.data.remote

import retrofit2.http.Body
import retrofit2.http.POST
import rs.ac.bg.etf.diplomski.medsched.data.remote.entities.UserEntity
import rs.ac.bg.etf.diplomski.medsched.data.remote.responses.MessageResponse

interface LoginApiService {

    @POST("/login")
    suspend fun loginUser(@Body userEntity: UserEntity): MessageResponse
}