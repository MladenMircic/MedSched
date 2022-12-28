package rs.ac.bg.etf.diplomski.medsched.data.remote

import retrofit2.http.GET
import rs.ac.bg.etf.diplomski.medsched.commons.Constants
import rs.ac.bg.etf.diplomski.medsched.data.remote.dto.response.ServiceDto

interface PatientApi {

    @GET("/${Constants.PATIENT_ENDPOINTS}/allServices")
    suspend fun getAllServices(): List<ServiceDto>
}