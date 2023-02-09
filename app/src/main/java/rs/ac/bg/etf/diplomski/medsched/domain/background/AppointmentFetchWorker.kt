package rs.ac.bg.etf.diplomski.medsched.domain.background

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.patient.GetAllAppointmentsUseCase


@HiltWorker
class AppointmentFetchWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val getAllAppointmentsUseCase: GetAllAppointmentsUseCase
): CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
//        val appointmentsJsonList = dataStore.data
//            .map { preferences -> preferences[APPOINTMENT_FETCH_KEY] }.first()
//        val appointmentList = moshi.toList<Appointment>(appointmentsJsonList!!)
//        val newAppointmentList = getAllScheduledUseCase.fetchScheduledAppointments()
//        if (appointmentList?.containsAll(newAppointmentList) == true) {
//            NotificationUtil.createNotification(
//                context,
//                Constants.APPOINTMENT_CANCEL_CHANNEL_ID,
//                Constants.APPOINTMENT_CANCEL_NOTIFICATION_ID,
//                context.getString(R.string.cancelled_appointments_title),
//                context.getString(R.string.cancelled_appointments_text)
//            )
//        }
        return Result.success()
    }
}