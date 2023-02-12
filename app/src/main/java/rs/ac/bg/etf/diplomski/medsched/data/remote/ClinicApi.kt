package rs.ac.bg.etf.diplomski.medsched.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import rs.ac.bg.etf.diplomski.medsched.commons.Constants
import rs.ac.bg.etf.diplomski.medsched.data.remote.dto.request.DoctorRegisterRequestDto
import rs.ac.bg.etf.diplomski.medsched.data.remote.dto.response.CategoryDto

interface ClinicApi {

    @GET("/${Constants.CLINIC_ENDPOINTS}/allCategories")
    suspend fun getAllCategories(): List<CategoryDto>

    @POST("/${Constants.CLINIC_ENDPOINTS}/registerDoctor")
    suspend fun registerDoctor(
        @Body doctorRegisterRequestDto: DoctorRegisterRequestDto
    )
}