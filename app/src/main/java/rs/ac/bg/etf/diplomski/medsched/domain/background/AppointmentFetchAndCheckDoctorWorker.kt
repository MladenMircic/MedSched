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
import rs.ac.bg.etf.diplomski.medsched.commons.Constants
import rs.ac.bg.etf.diplomski.medsched.commons.NotificationUtil
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.doctor.GetAllAppointmentsForDoctorUseCase

@HiltWorker
class AppointmentFetchAndCheckDoctorWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val getAllAppointmentsForDoctorUseCase: GetAllAppointmentsForDoctorUseCase
): CoroutineWorker(context, workerParameters) {

    @RequiresApi(Build.VERSION_CODES.M)
    override suspend fun doWork(): Result {
        var isChanged = false
        val remoteAppointments = getAllAppointmentsForDoctorUseCase
            .getAllAppointmentsForDoctorFromRemote()
        val localAppointments = getAllAppointmentsForDoctorUseCase
            .getAllAppointmentsForDoctorFromLocal()
        if (localAppointments.size != remoteAppointments.size) {
            val activityIntent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                data = Constants.DOCTOR_SCHEDULED_DEEPLINK.toUri()
            }
            val pending: PendingIntent = TaskStackBuilder.create(context).run {
                addNextIntentWithParentStack(activityIntent)
                getPendingIntent(1, PendingIntent.FLAG_IMMUTABLE)
            }
            NotificationUtil.createNotification(
                context,
                pending,
                Constants.APPOINTMENT_CANCEL_CHANNEL_ID,
                Constants.APPOINTMENT_CANCEL_NOTIFICATION_ID,
                context.getString(R.string.cancelled_or_new_appointments_notification_title),
                context.getString(R.string.cancelled_or_new_appointments_notification_text)
            )
            isChanged = true
        } else {
            for (remoteAppointment in remoteAppointments) {
                val remoteAppointmentInLocal = localAppointments.firstOrNull {
                    it.appointment.id == remoteAppointment.appointment.id
                }
                if (remoteAppointmentInLocal?.appointment?.confirmed == true &&
                    !remoteAppointment.appointment.confirmed) {
                    val activityIntent = Intent(context, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        data = Constants.DOCTOR_SCHEDULED_DEEPLINK.toUri()
                    }
                    val pending: PendingIntent = TaskStackBuilder.create(context).run {
                        addNextIntentWithParentStack(activityIntent)
                        getPendingIntent(1, PendingIntent.FLAG_IMMUTABLE)
                    }
                    NotificationUtil.createNotification(
                        context,
                        pending,
                        Constants.APPOINTMENT_CANCEL_CHANNEL_ID,
                        Constants.APPOINTMENT_CANCEL_NOTIFICATION_ID,
                        context.getString(R.string.cancelled_or_new_appointments_notification_title),
                        context.getString(R.string.cancelled_or_new_appointments_notification_text)
                    )
                    isChanged = true
                    break
                }
            }
        }
        if (isChanged) {
            getAllAppointmentsForDoctorUseCase.fetchAllAppointmentsForDoctorAndSaveLocal()
        }
        return Result.success()
    }
}