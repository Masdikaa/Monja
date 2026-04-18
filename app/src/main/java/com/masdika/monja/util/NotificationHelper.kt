package com.masdika.monja.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.masdika.monja.R

class NotificationHelper(private val context: Context) {
    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    companion object {
        const val CHANNEL_ID = "evacuation_alerts_channel"
        const val NOTIFICATION_ID = 1001
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Evacuation Alert",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Critical alerts for immediate evacuation"
        }
        notificationManager.createNotificationChannel(channel)
    }

    fun showEvacuationAlert(macAddress: String) {
        val titleText = UiText.StringResource(R.string.notification_content_title)
        val contentText = UiText.StringResource(
            R.string.notification_content_text,
            macAddress
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.monja_icon)
            .setContentTitle(titleText.asString(context))
            .setContentText(contentText.asString(context))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

}