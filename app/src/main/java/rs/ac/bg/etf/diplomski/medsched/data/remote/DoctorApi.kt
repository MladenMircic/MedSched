package rs.ac.bg.etf.diplomski.medsched.data.remote

import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path
import rs.ac.bg.etf.diplomski.medsched.commons.Constants
import rs.ac.bg.etf.diplomski.medsched.data.remote.dto.response.AppointmentForDoctorDto

interface DoctorApi {

    @GET("/${Constants.DOCTOR_ENDPOINTS}/allAppointmentsForDoctor")
    suspend fun getAllAppointmentsForDoctor(): List<AppointmentForDoctorDto>

    @DELETE("/${Constants.DOCTOR_ENDPOINTS}/cancelAppointment/{appointmentId}")
    suspend fun cancelAppointment(@Path("appointmentId") appointmentId: Int)

}