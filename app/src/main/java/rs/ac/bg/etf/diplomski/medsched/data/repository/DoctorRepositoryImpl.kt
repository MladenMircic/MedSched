package rs.ac.bg.etf.diplomski.medsched.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import rs.ac.bg.etf.diplomski.medsched.data.local.dao.DoctorDao
import rs.ac.bg.etf.diplomski.medsched.data.remote.DoctorApi
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.AppointmentForDoctor
import rs.ac.bg.etf.diplomski.medsched.domain.repository.DoctorRepository
import javax.inject.Inject

class DoctorRepositoryImpl @Inject constructor(
    private val doctorApi: DoctorApi,
    private val doctorDao: DoctorDao
): DoctorRepository {

    override val appointmentsForDoctorFlow: Flow<List<AppointmentForDoctor>> =
        doctorDao.getAllAppointmentForDoctorEntitiesFlow().map { appointmentDoctorList ->
            appointmentDoctorList.map { it.toAppointmentForDoctor() }
        }

    override suspend fun getAllAppointmentsForDoctorFromRemote(): List<AppointmentForDoctor> =
        doctorApi.getAllAppointmentsForDoctor().map { it.toAppointmentForDoctor() }

    override suspend fun getAllAppointmentsForDoctorFromLocal(): List<AppointmentForDoctor> =
        doctorDao.getAllAppointmentForDoctorEntities().map { it.toAppointmentForDoctor() }

    override suspend fun fetchAllAppointmentsForDoctorAndSaveLocal() {
        doctorDao.deleteAllAppointmentForDoctorEntities()
        val appointmentForDoctorList =
            doctorApi.getAllAppointmentsForDoctor().map { it.toAppointmentForDoctor() }
        for (appointmentForDoctor in appointmentForDoctorList) {
            doctorDao.insertAppointmentForDoctorEntity(
                appointmentForDoctor.toAppointmentForDoctorEntity()
            )
        }
    }
}