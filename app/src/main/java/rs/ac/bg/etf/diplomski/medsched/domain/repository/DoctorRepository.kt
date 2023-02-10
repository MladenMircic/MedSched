package rs.ac.bg.etf.diplomski.medsched.domain.repository

import kotlinx.coroutines.flow.Flow
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.AppointmentForDoctor

interface DoctorRepository {

    val appointmentsForDoctorFlow: Flow<List<AppointmentForDoctor>>

    suspend fun getAllAppointmentsForDoctorFromRemote(): List<AppointmentForDoctor>
    suspend fun getAllAppointmentsForDoctorFromLocal(): List<AppointmentForDoctor>
    suspend fun fetchAllAppointmentsForDoctorAndSaveLocal()
}