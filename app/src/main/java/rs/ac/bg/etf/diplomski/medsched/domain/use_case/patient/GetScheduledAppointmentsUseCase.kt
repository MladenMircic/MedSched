package rs.ac.bg.etf.diplomski.medsched.domain.use_case.patient

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.commons.Resource
import rs.ac.bg.etf.diplomski.medsched.domain.model.request.AvailableTimesRequest
import rs.ac.bg.etf.diplomski.medsched.domain.model.response.AvailableTimesResponse
import rs.ac.bg.etf.diplomski.medsched.domain.repository.PatientRepository
import javax.inject.Inject

class GetScheduledAppointmentsUseCase @Inject constructor(
    private val patientRepository: PatientRepository
) {

    operator fun invoke(
        availableTimesRequest: AvailableTimesRequest
    ): Flow<Resource<AvailableTimesResponse>> = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(
                patientRepository.getAvailableTimesForDoctor(availableTimesRequest)
            ))
        } catch (e: HttpException) {
            emit(Resource.Error(R.string.unknown_error))
        } catch (e: IOException) {
            emit(Resource.Error(R.string.no_connection))
        }
    }
}