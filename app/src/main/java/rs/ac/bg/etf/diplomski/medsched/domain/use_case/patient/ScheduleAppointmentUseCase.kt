package rs.ac.bg.etf.diplomski.medsched.domain.use_case.patient

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.commons.Resource
import rs.ac.bg.etf.diplomski.medsched.data.local.dao.PatientDao
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Appointment
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Service
import rs.ac.bg.etf.diplomski.medsched.domain.model.entities.AppointmentForPatientEntity
import rs.ac.bg.etf.diplomski.medsched.domain.repository.PatientRepository
import javax.inject.Inject

class ScheduleAppointmentUseCase @Inject constructor(
    private val patientRepository: PatientRepository,
    private val patientDao: PatientDao
) {
    suspend operator fun invoke(appointments: List<Appointment>): Flow<Resource<List<Int>>> = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(patientRepository.scheduleAppointments(appointments)))
        } catch (e: HttpException) {
            emit(Resource.Error(R.string.schedule_failure))
        } catch (e: IOException) {
            emit(Resource.Error(R.string.no_connection))
        }
    }

    suspend fun inLocal(
        appointmentEntities: List<AppointmentForPatientEntity>,
        services: List<List<Service>>
    ) {
        appointmentEntities.forEachIndexed { index, appointmentForPatientEntity ->
            val appointmentServices = services[index].map {
                it.toAppointmentServicePatientEntity(appointmentForPatientEntity.appointmentId)
            }
            patientDao.insertAppointmentForPatientEntity(appointmentForPatientEntity)
            patientDao.insertServicesForAppointment(*appointmentServices.toTypedArray())
        }
    }

}

