package com.android.cedecsi.ui.schedule

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.android.cedecsi.R

class TrackingNotification(private val context: Context) {

    companion object {
        const val CHANNEL_ID = "channel_id"
    }

    private var notifyManager: NotificationManager? = null

    private fun createNotificationChannel() {
        notifyManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                CHANNEL_ID,
                "Tracking service",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
                description = "Notificaciones del tracking"
            }.also {
                notifyManager?.createNotificationChannel(it)
            }
        }
    }

    fun trackingIntent(): PendingIntent {
        return PendingIntent.getActivity(
            context,
            0,
            Intent(context, ScheduleActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    fun showNotification(intent: PendingIntent? = null) {
        createNotificationChannel()

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Tracking")
            .setContentText("Se ha iniciado con el tracking")
            .setContentIntent(intent)
            .setSmallIcon(R.drawable.ic_tracking)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(false)

        notifyManager?.notify(0, builder.build())
    }

    fun cancelNotifications() {
        notifyManager?.cancel(0)
    }

}