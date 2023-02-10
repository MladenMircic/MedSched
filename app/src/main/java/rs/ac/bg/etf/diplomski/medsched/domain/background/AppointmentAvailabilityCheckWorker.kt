package rs.ac.bg.etf.diplomski.medsched.domain.background

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import rs.ac.bg.etf.diplomski.medsched.MainActivity
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.commons.Constants.APPOINTMENT_CANCEL_CHANNEL_ID
import rs.ac.bg.etf.diplomski.medsched.commons.Constants.APPOINTMENT_CANCEL_NOTIFICATION_ID
import rs.ac.bg.etf.diplomski.medsched.commons.Constants.PATIENT_SCHEDULED_DEEPLINK
import rs.ac.bg.etf.diplomski.medsched.commons.NotificationUtil
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.patient.GetAllAppointmentsForPatientUseCase


@HiltWorker
class AppointmentAvailabilityCheckWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val getAllAppointmentsForPatientUseCase: GetAllAppointmentsForPatientUseCase
): CoroutineWorker(context, workerParameters) {

    @RequiresApi(Build.VERSION_CODES.M)
    override suspend fun doWork(): Result {
        val remoteAppointments = getAllAppointmentsForPatientUseCase.getAllAppointmentsFromRemote()
        val localAppointments = getAllAppointmentsForPatientUseCase.getAllAppointmentsWithDoctorFromLocal()
        for (remoteAppointment in remoteAppointments) {
            val remoteAppointmentInLocal = localAppointments.firstOrNull {
                it.appointment.id == remoteAppointment.appointment.id
            }
            if (remoteAppointmentInLocal?.appointment?.confirmed == true &&
                !remoteAppointment.appointment.confirmed) {
                val activityIntent = Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    data = PATIENT_SCHEDULED_DEEPLINK.toUri()
                }
                val pending: PendingIntent = TaskStackBuilder.create(context).run {
                    addNextIntentWithParentStack(activityIntent)
                    getPendingIntent(1, PendingIntent.FLAG_IMMUTABLE)
                }
                NotificationUtil.createNotification(
                    context,
                    pending,
                    APPOINTMENT_CANCEL_CHANNEL_ID,
                    APPOINTMENT_CANCEL_NOTIFICATION_ID,
                    context.getString(R.string.cancelled_appointments_notification_title),
                    context.getString(R.string.cancelled_appointments_notification_text)
                )
                break
            }
        }
        getAllAppointmentsForPatientUseCase.fetchAllAppointmentsAndSaveInLocal()
        return Result.success()
    }
}