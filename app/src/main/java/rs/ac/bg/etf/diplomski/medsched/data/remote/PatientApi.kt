package rs.ac.bg.etf.diplomski.medsched.data.remote

import retrofit2.http.*
import rs.ac.bg.etf.diplomski.medsched.commons.Constants
import rs.ac.bg.etf.diplomski.medsched.data.remote.dto.AppointmentDto
import rs.ac.bg.etf.diplomski.medsched.data.remote.dto.request.AppointmentsRequestDto
import rs.ac.bg.etf.diplomski.medsched.data.remote.dto.response.CategoryDto
import rs.ac.bg.etf.diplomski.medsched.data.remote.dto.response.DoctorForPatientDto
import rs.ac.bg.etf.diplomski.medsched.data.remote.dto.response.ServiceDto

interface PatientApi {

    @GET("/${Constants.PATIENT_ENDPOINTS}/allCategories")
    suspend fun getAllServices(): List<CategoryDto>

    @GET("/${Constants.PATIENT_ENDPOINTS}/getDoctors")
    suspend fun getDoctors(@Query("category") category: String): List<DoctorForPatientDto>

    @GET("/${Constants.PATIENT_ENDPOINTS}/getServicesForDoctor/{doctorId}")
    suspend fun getServicesForDoctor(@Path("doctorId") doctorId: Int): List<ServiceDto>

    @POST("/${Constants.PATIENT_ENDPOINTS}/scheduledAppointmentsForDoctor")
    suspend fun getScheduledAppointments(
        @Body appointmentsRequestDto: AppointmentsRequestDto
    ): List<AppointmentDto>

    @POST("/${Constants.PATIENT_ENDPOINTS}/scheduleAppointment")
    suspend fun scheduleAppointment(@Body appointmentDto: AppointmentDto)
}