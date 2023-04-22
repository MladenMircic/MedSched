package rs.ac.bg.etf.diplomski.medsched.domain.use_case.patient

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.commons.Resource
import rs.ac.bg.etf.diplomski.medsched.data.local.dao.PatientDao
import rs.ac.bg.etf.diplomski.medsched.domain.repository.PatientRepository
import javax.inject.Inject

class CancelAppointmentUseCase @Inject constructor(
    private val patientRepository: PatientRepository,
    private val patientDao: PatientDao
) {
    suspend operator fun invoke(appointmentId: Int): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(patientRepository.cancelAppointment(appointmentId)))
        } catch (e: HttpException) {
            emit(Resource.Error(R.string.unknown_error))
        } catch (e: IOException) {
            emit(Resource.Error(R.string.no_connection))
        }
    }

    suspend fun cancelInLocal(appointmentId: Int) {
        patientDao.markAppointmentCancelled(appointmentId)
        patientDao.deleteAppointmentServicePatientEntity(appointmentId)
    }
}