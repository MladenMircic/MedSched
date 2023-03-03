package rs.ac.bg.etf.diplomski.medsched.data.remote

import kotlinx.datetime.LocalTime
import retrofit2.http.*
import rs.ac.bg.etf.diplomski.medsched.commons.Constants
import rs.ac.bg.etf.diplomski.medsched.data.remote.dto.request.AppointmentsRequestDto
import rs.ac.bg.etf.diplomski.medsched.data.remote.dto.request.EmailChangeRequestDto
import rs.ac.bg.etf.diplomski.medsched.data.remote.dto.request.InfoChangeRequestDto
import rs.ac.bg.etf.diplomski.medsched.data.remote.dto.request.PasswordChangeRequestDto
import rs.ac.bg.etf.diplomski.medsched.data.remote.dto.response.*

interface PatientApi {

    @GET("/${Constants.PATIENT_ENDPOINTS}/allAppointmentsForPatient")
    suspend fun getAllAppointmentsForPatient(): List<AppointmentForPatientDto>

    @GET("/${Constants.PATIENT_ENDPOINTS}/allCategories")
    suspend fun getAllCategories(): List<CategoryDto>

    @GET("/${Constants.PATIENT_ENDPOINTS}/getDoctors")
    suspend fun getDoctors(@Query("category") category: String): List<DoctorForPatientDto>

    @GET("/${Constants.PATIENT_ENDPOINTS}/getClinics")
    suspend fun getClinics(@Query("category") category: String): List<ClinicForPatientDto>

    @GET("/${Constants.PATIENT_ENDPOINTS}/getServicesForDoctor/{doctorId}")
    suspend fun getServicesForDoctor(@Path("doctorId") doctorId: String): List<ServiceDto>

    @POST("/${Constants.PATIENT_ENDPOINTS}/scheduledAppointmentsForDoctor")
    suspend fun getScheduledAppointments(
        @Body appointmentsRequestDto: AppointmentsRequestDto
    ): List<LocalTime>

    @POST("/${Constants.PATIENT_ENDPOINTS}/scheduleAppointment")
    suspend fun scheduleAppointment(@Body appointmentDto: AppointmentDto): AppointmentForPatientDto

    @DELETE("/${Constants.PATIENT_ENDPOINTS}/cancelAppointment/{appointmentId}")
    suspend fun cancelAppointment(@Path("appointmentId") appointmentId: Int)

    @POST("/${Constants.PATIENT_ENDPOINTS}/updateEmail")
    suspend fun updateEmail(@Body emailChangeRequestDto: EmailChangeRequestDto)

    @POST("/${Constants.PATIENT_ENDPOINTS}/updatePassword")
    suspend fun updatePassword(
        @Body passwordChangeRequestDto: PasswordChangeRequestDto
    ) : PasswordChangeResponseDto

    @POST("/${Constants.PATIENT_ENDPOINTS}/updateInfo")
    suspend fun updateInfo(
        @Body infoChangeRequestDto: InfoChangeRequestDto
    )
}