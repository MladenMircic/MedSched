package rs.ac.bg.etf.diplomski.medsched.domain.repository

import kotlinx.coroutines.flow.Flow
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.*
import rs.ac.bg.etf.diplomski.medsched.domain.model.request.AppointmentRequest
import rs.ac.bg.etf.diplomski.medsched.domain.model.request.EmailChangeRequest
import rs.ac.bg.etf.diplomski.medsched.domain.model.request.InfoChangeRequest
import rs.ac.bg.etf.diplomski.medsched.domain.model.request.PasswordChangeRequest
import rs.ac.bg.etf.diplomski.medsched.domain.model.response.PasswordChangeResponse

interface PatientRepository {

    val user: Flow<User?>
    val appointmentWithDoctorFlow: Flow<List<AppointmentWithDoctor>>

    suspend fun fetchAndInsertIntoDbAppointmentsWithDoctor()
    suspend fun getAppointmentsWithDoctorFromRemote(): List<AppointmentWithDoctor>
    suspend fun getAppointmentsWithDoctorFromLocal(): List<AppointmentWithDoctor>
    suspend fun getAllServices(): List<Category>
    suspend fun getDoctors(category: String): List<DoctorForPatient>
    suspend fun getAllAppointmentsForDoctorAtDate(
        appointmentRequest: AppointmentRequest
    ) : List<Appointment>
    suspend fun getAllServicesForDoctor(doctorId: Int): List<Service>
    suspend fun scheduleAppointment(appointment: Appointment)
    suspend fun cancelAppointment(appointmentId: Int)
    suspend fun updateEmail(emailChangeRequest: EmailChangeRequest)
    suspend fun updatePassword(passwordChangeRequest: PasswordChangeRequest) : PasswordChangeResponse
    suspend fun updateInfo(infoChangeRequest: InfoChangeRequest)
}