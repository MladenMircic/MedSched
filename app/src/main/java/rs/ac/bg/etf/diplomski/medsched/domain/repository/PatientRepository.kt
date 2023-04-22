package rs.ac.bg.etf.diplomski.medsched.domain.repository

import kotlinx.coroutines.flow.Flow
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.*
import rs.ac.bg.etf.diplomski.medsched.domain.model.entities.NotificationPatientEntity
import rs.ac.bg.etf.diplomski.medsched.domain.model.request.AvailableTimesRequest
import rs.ac.bg.etf.diplomski.medsched.domain.model.request.EmailChangeRequest
import rs.ac.bg.etf.diplomski.medsched.domain.model.request.InfoChangeRequest
import rs.ac.bg.etf.diplomski.medsched.domain.model.request.PasswordChangeRequest
import rs.ac.bg.etf.diplomski.medsched.domain.model.response.AvailableTimesResponse
import rs.ac.bg.etf.diplomski.medsched.domain.model.response.PasswordChangeResponse

interface PatientRepository {

    val user: Flow<User?>
    val appointmentForPatientFlow: Flow<List<AppointmentForPatient>>
    val notifications: Flow<List<NotificationPatientEntity>>

    suspend fun fetchAllAppointmentsForPatientAndSaveLocal()
    suspend fun getAppointmentsWithDoctorFromRemote(): List<AppointmentForPatient>
    suspend fun getAppointmentsWithDoctorFromLocal(): List<AppointmentForPatient>
    suspend fun getAllCategories(): List<Category>
    suspend fun getDoctors(doctorName: String, categories: String): List<DoctorForPatient>
    suspend fun getClinics(category: String): List<ClinicForPatient>
    suspend fun getAvailableTimesForDoctor(
        availableTimesRequest: AvailableTimesRequest
    ) : AvailableTimesResponse
    suspend fun getAllServicesForDoctor(doctorId: String): List<Service>
    suspend fun scheduleAppointments(appointments: List<Appointment>): List<Int>
    suspend fun cancelAppointment(appointmentId: Int)
    suspend fun updateEmail(emailChangeRequest: EmailChangeRequest)
    suspend fun updatePassword(passwordChangeRequest: PasswordChangeRequest) : PasswordChangeResponse
    suspend fun updateInfo(infoChangeRequest: InfoChangeRequest)

    suspend fun insertNotification(notificationPatientEntity: NotificationPatientEntity)
    suspend fun updateNotification(notificationPatientEntity: NotificationPatientEntity)
}