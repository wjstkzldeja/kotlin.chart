package com.osl.base.project.main.utils.terra

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.osl.base.project.main.views.container.main.MainActivity
import com.osl.base.project.osl.utils.CHANNEL_ID
import com.osl.base.project.osl.utils.CHANNEL_NAME
import com.osl.base.project.osl.utils.NOTIFICATION_ID

class NotificationHelper(private val context: Context) {

    private fun getNotificationManager() =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun displayNotification() {
        val resultIntent = Intent(context, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_IMMUTABLE)
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info).setContentTitle("Libre sensor expired")
            .setContentText("Replace new sensor")
            .setStyle(NotificationCompat.BigTextStyle().bigText("Replace new sensor"))
            .setAutoCancel(true).setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)

        getNotificationManager().notify(NOTIFICATION_ID, builder.build())
    }

    fun createNotificationChannel() {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
        getNotificationManager().createNotificationChannel(channel)
    }
}