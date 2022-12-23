package rs.ac.bg.etf.diplomski.medsched.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import rs.ac.bg.etf.diplomski.medsched.data.remote.dto.request.LoginRequestDto
import rs.ac.bg.etf.diplomski.medsched.data.remote.dto.request.RegisterRequestDto
import rs.ac.bg.etf.diplomski.medsched.data.remote.dto.response.LoginResponseDto
import rs.ac.bg.etf.diplomski.medsched.data.remote.dto.response.RegisterResponseDto

interface LoginRegisterApi {

    @POST("/login")
    suspend fun loginUser(@Body loginRequestDto: LoginRequestDto): LoginResponseDto

    @POST("/register")
    suspend fun registerUser(@Body registerRequestDto: RegisterRequestDto): RegisterResponseDto

    @GET("/authenticate")
    suspend fun authenticateUser(@Header("Authorization") bearerToken: String)
}