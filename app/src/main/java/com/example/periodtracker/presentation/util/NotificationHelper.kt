package com.example.periodtracker.presentation.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat

class NotificationHelper(private val ctx: Context) {

    private val notificationManager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val channelId = "period_tracker_channel"

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Period Tracker Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply { description = "Notifications for period tracking" }
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showPeriodReminder() {
        showNotification(1001, "Period Starts Soon", "Your period is expected to start soon")
    }

    fun showOvulationReminder() {
        showNotification(1002, "Ovulation Day", "Your ovulation day is approaching")
    }

    fun showFertileWindowReminder() {
        showNotification(1003, "Fertile Window", "Your fertile window is starting soon")
    }

    fun showReminder(title: String, message: String) {
        showNotification(1004, title, message)
    }

    private fun showNotification(id: Int, title: String, message: String) {
        val notification = NotificationCompat.Builder(ctx, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()
        notificationManager.notify(id, notification)
    }
}
