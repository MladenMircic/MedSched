package rs.ac.bg.etf.diplomski.medsched.domain.use_case.patient

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.commons.Resource
import rs.ac.bg.etf.diplomski.medsched.domain.repository.PatientRepository
import javax.inject.Inject

class GetAllAppointmentsForPatientUseCase @Inject constructor(
    private val patientRepository: PatientRepository
) {
    val appointmentsForPatientFlow = patientRepository.appointmentForPatientFlow

    suspend fun getAllAppointmentsFromLocal() =
        patientRepository.getAppointmentsWithDoctorFromLocal()

    suspend fun getAllAppointmentsFromRemote() =
        patientRepository.getAppointmentsWithDoctorFromRemote()

    suspend fun fetchAllAppointmentsAndSaveInLocal(): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        try {
            patientRepository.fetchAllAppointmentsForPatientAndSaveLocal()
            emit(Resource.Success(Unit))
        } catch (e: HttpException) {
            emit(Resource.Error(R.string.unknown_error))
        } catch (e: IOException) {
            emit(Resource.Error(R.string.no_connection))
        }

    }
}