package rs.ac.bg.etf.diplomski.medsched.domain.repository

import kotlinx.coroutines.flow.Flow
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.*
import rs.ac.bg.etf.diplomski.medsched.domain.model.request.AppointmentRequest

interface PatientRepository {

    val user: Flow<User?>

    suspend fun getAllScheduled(): List<Scheduled>
    suspend fun getAllServices(): List<Category>
    suspend fun getDoctors(category: String): List<DoctorForPatient>
    suspend fun getAllAppointmentsForDoctorAtDate(
        appointmentRequest: AppointmentRequest
    ) : List<Appointment>
    suspend fun getAllServicesForDoctor(doctorId: Int): List<Service>
    suspend fun scheduleAppointment(appointment: Appointment)
    suspend fun cancelAppointment(appointmentId: Int)
}