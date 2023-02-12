package rs.ac.bg.etf.diplomski.medsched.domain.use_case.patient

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.LocalTime
import okio.IOException
import retrofit2.HttpException
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.commons.Resource
import rs.ac.bg.etf.diplomski.medsched.domain.model.request.AppointmentRequest
import rs.ac.bg.etf.diplomski.medsched.domain.repository.PatientRepository
import javax.inject.Inject

class GetScheduledAppointmentsUseCase @Inject constructor(
    private val patientRepository: PatientRepository
) {

    operator fun invoke(
        appointmentRequest: AppointmentRequest
    ): Flow<Resource<List<LocalTime>>> = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(
                patientRepository.getAllAppointmentsForDoctorAtDate(appointmentRequest)
            ))
        } catch (e: HttpException) {
            emit(Resource.Error(R.string.unknown_error))
        } catch (e: IOException) {
            emit(Resource.Error(R.string.no_connection))
        }
    }
}