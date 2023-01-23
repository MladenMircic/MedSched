package rs.ac.bg.etf.diplomski.medsched.domain.background

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.squareup.moshi.Moshi
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.map
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.commons.Constants.APPOINTMENT_CANCEL_CHANNEL_ID
import rs.ac.bg.etf.diplomski.medsched.commons.Constants.APPOINTMENT_CANCEL_NOTIFICATION_ID
import rs.ac.bg.etf.diplomski.medsched.commons.NotificationUtil
import rs.ac.bg.etf.diplomski.medsched.commons.PreferenceKeys.Companion.APPOINTMENT_FETCH_KEY
import rs.ac.bg.etf.diplomski.medsched.di.json_adapters.toList
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Appointment
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.patient.GetAllScheduledUseCase


@HiltWorker
class AppointmentFetchWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val getAllScheduledUseCase: GetAllScheduledUseCase,
    private val dataStore: DataStore<Preferences>,
    private val moshi: Moshi
): CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        dataStore.data
            .map { preferences -> preferences[APPOINTMENT_FETCH_KEY] }
            .collect { appointmentsJsonList ->
                val appointmentList = moshi.toList<Appointment>(appointmentsJsonList!!)
                val newAppointmentList = getAllScheduledUseCase.fetchScheduledAppointments()
                Log.d("TESTIRANJE", "RADI")
                if (appointmentList?.containsAll(newAppointmentList) == true) {
                    NotificationUtil.createNotification(
                        context,
                        APPOINTMENT_CANCEL_CHANNEL_ID,
                        APPOINTMENT_CANCEL_NOTIFICATION_ID,
                        context.getString(R.string.cancelled_appointments_title),
                        context.getString(R.string.cancelled_appointments_text)
                    )
                }
            }
        return Result.success()
    }
}