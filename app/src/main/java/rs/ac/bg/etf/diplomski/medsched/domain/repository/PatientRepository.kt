package rs.ac.bg.etf.diplomski.medsched.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalTime
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.*
import rs.ac.bg.etf.diplomski.medsched.domain.model.request.AppointmentRequest
import rs.ac.bg.etf.diplomski.medsched.domain.model.request.EmailChangeRequest
import rs.ac.bg.etf.diplomski.medsched.domain.model.request.InfoChangeRequest
import rs.ac.bg.etf.diplomski.medsched.domain.model.request.PasswordChangeRequest
import rs.ac.bg.etf.diplomski.medsched.domain.model.response.PasswordChangeResponse

interface PatientRepository {

    val user: Flow<User?>
    val appointmentForPatientFlow: Flow<List<AppointmentForPatient>>

    suspend fun fetchAllAppointmentsForPatientAndSaveLocal()
    suspend fun getAppointmentsWithDoctorFromRemote(): List<AppointmentForPatient>
    suspend fun getAppointmentsWithDoctorFromLocal(): List<AppointmentForPatient>
    suspend fun getAllCategories(): List<Category>
    suspend fun getDoctors(category: String): List<DoctorForPatient>
    suspend fun getClinics(category: String): List<ClinicForPatient>
    suspend fun getAllAppointmentsForDoctorAtDate(
        appointmentRequest: AppointmentRequest
    ) : List<LocalTime>
    suspend fun getAllServicesForDoctor(doctorId: String): List<Service>
    suspend fun scheduleAppointment(appointment: Appointment)
    suspend fun cancelAppointment(appointmentId: Int)
    suspend fun updateEmail(emailChangeRequest: EmailChangeRequest)
    suspend fun updatePassword(passwordChangeRequest: PasswordChangeRequest) : PasswordChangeResponse
    suspend fun updateInfo(infoChangeRequest: InfoChangeRequest)
}