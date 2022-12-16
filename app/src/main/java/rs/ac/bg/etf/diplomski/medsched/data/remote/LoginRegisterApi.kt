package rs.ac.bg.etf.diplomski.medsched.data.remote

import retrofit2.http.Body
import retrofit2.http.POST
import rs.ac.bg.etf.diplomski.medsched.data.remote.dto.UserDto
import rs.ac.bg.etf.diplomski.medsched.data.remote.dto.UserLoginDto

interface LoginRegisterApi {

    @POST("/login")
    suspend fun loginUser(@Body userDto: UserDto): UserLoginDto
}