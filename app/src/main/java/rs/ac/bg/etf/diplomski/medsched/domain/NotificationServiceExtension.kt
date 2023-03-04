package rs.ac.bg.etf.diplomski.medsched.domain

import android.content.Context
import android.util.Log
import com.onesignal.OSNotificationReceivedEvent
import com.onesignal.OneSignal.OSRemoteNotificationReceivedHandler
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Role
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.GetUserUseCase
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.NotificationsUseCase

@SuppressWarnings("unused")
class NotificationServiceExtension : OSRemoteNotificationReceivedHandler {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface NotificationEntryPoint {
        fun getUserUseCase(): GetUserUseCase
        fun saveNotificationUseCase(): NotificationsUseCase
    }

    override fun remoteNotificationReceived(
        context: Context,
        notificationReceivedEvent: OSNotificationReceivedEvent
    ) {
        val notificationEntryPoint: NotificationEntryPoint =
            EntryPoints.get(context, NotificationEntryPoint::class.java)
        val getUserUseCase = notificationEntryPoint.getUserUseCase()
        CoroutineScope(Job()).launch {
            val user = getUserUseCase.userFlow.first()!!
            when (user.type) {
                Role.DOCTOR -> {
                    Log.d("TESTIRANJE", user.email)
                }
                Role.PATIENT -> {
                    Log.d("TESTIRANJE", user.email)
                }
                Role.CLINIC -> {
                    Log.d("TESTIRANJE", user.email)
                }
                else -> {}
            }
        }
        notificationReceivedEvent.complete(notificationReceivedEvent.notification)
    }
}