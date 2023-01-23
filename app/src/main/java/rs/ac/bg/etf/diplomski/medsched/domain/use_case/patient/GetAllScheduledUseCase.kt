package rs.ac.bg.etf.diplomski.medsched.domain.use_case.patient

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.commons.Resource
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Appointment
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Scheduled
import rs.ac.bg.etf.diplomski.medsched.domain.repository.PatientRepository
import javax.inject.Inject

class GetAllScheduledUseCase @Inject constructor(
    private val patientRepository: PatientRepository
) {
    suspend operator fun invoke(): Flow<Resource<List<Scheduled>>> = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(patientRepository.getAllScheduled()))
        } catch (e: HttpException) {
            emit(Resource.Error(R.string.unknown_error))
        } catch (e: IOException) {
            emit(Resource.Error(R.string.no_connection))
        }
    }

    suspend fun fetchScheduledAppointments(): List<Appointment> =
        patientRepository.getAllScheduled().map { it.appointment }
}