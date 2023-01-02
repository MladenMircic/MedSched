package rs.ac.bg.etf.diplomski.medsched.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import rs.ac.bg.etf.diplomski.medsched.commons.Constants
import rs.ac.bg.etf.diplomski.medsched.data.remote.dto.request.LoginRequestDto
import rs.ac.bg.etf.diplomski.medsched.data.remote.dto.request.RegisterRequestDto
import rs.ac.bg.etf.diplomski.medsched.data.remote.dto.response.LoginResponseDto
import rs.ac.bg.etf.diplomski.medsched.data.remote.dto.response.RegisterResponseDto

interface LoginRegisterApi {

    @POST("/${Constants.AUTHENTICATION_ENDPOINTS}/login")
    suspend fun loginUser(@Body loginRequestDto: LoginRequestDto): LoginResponseDto

    @POST("/${Constants.AUTHENTICATION_ENDPOINTS}/register")
    suspend fun registerUser(@Body registerRequestDto: RegisterRequestDto): RegisterResponseDto

    @GET("/${Constants.AUTHENTICATION_ENDPOINTS}/authenticate")
    suspend fun authenticateUser()
}