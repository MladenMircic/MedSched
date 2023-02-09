package rs.ac.bg.etf.diplomski.medsched.commons

import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import rs.ac.bg.etf.diplomski.medsched.R

class NotificationUtil {

    companion object {

        fun createNotificationChannel(
            context: Context,
            channelId: String,
            channelName: String,
            channelDescription: String
        ) {
            val notificationChannel =
                NotificationChannelCompat.Builder(channelId, NotificationManagerCompat.IMPORTANCE_HIGH)
                    .setName(channelName)
                    .setDescription(channelDescription).build()
            NotificationManagerCompat.from(context).createNotificationChannel(notificationChannel)
        }

        fun createNotification(
            context: Context,
            pendingIntent: PendingIntent,
            channelId: String,
            notificationId: Int,
            title: String,
            message: String
        ) {
            val notification = NotificationCompat.Builder(context, channelId)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.caduceus)
                .setContentTitle(title)
                .setContentText(message).setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(LongArray(0))
                .setAutoCancel(true)
                .build()
            NotificationManagerCompat.from(context).notify(notificationId, notification)
        }
    }
}