package com.example.periodtracker.domain.notification

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.ListenableWorker
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.periodtracker.domain.model.PredictionResult
import com.example.periodtracker.domain.model.Settings
import java.util.concurrent.TimeUnit

class NotificationScheduler(private val context: Context) {
    private val workManager = WorkManager.getInstance(context)

    fun schedulePeriodReminders(settings: Settings, prediction: PredictionResult?) {
        if (!settings.remindersEnabled || prediction == null) return

        if (settings.periodReminderDaysBefore > 0) {
            val reminderTime = prediction.nextPeriodStart - (settings.periodReminderDaysBefore * 24 * 60 * 60 * 1000L)
            if (reminderTime > System.currentTimeMillis()) {
                scheduleOneTimeWork(
                    "period_reminder_${prediction.nextPeriodStart}",
                    PeriodReminderWorker::class.java,
                    reminderTime
                )
            }
        }

        if (settings.ovulationReminderEnabled) {
            val reminderTime = prediction.ovulationDay - (24 * 60 * 60 * 1000L)
            if (reminderTime > System.currentTimeMillis()) {
                scheduleOneTimeWork(
                    "ovulation_reminder_${prediction.ovulationDay}",
                    OvulationReminderWorker::class.java,
                    reminderTime
                )
            }
        }

        if (settings.fertileWindowReminderEnabled) {
            val reminderTime = prediction.fertileWindowStart - (24 * 60 * 60 * 1000L)
            if (reminderTime > System.currentTimeMillis()) {
                scheduleOneTimeWork(
                    "fertile_reminder_${prediction.fertileWindowStart}",
                    FertileWindowReminderWorker::class.java,
                    reminderTime
                )
            }
        }
    }

    fun cancelAllReminders() {
        workManager.cancelAllWork()
    }

    private fun scheduleOneTimeWork(workName: String, workerClass: Class<out ListenableWorker>, triggerTimeMillis: Long) {
        val delay = triggerTimeMillis - System.currentTimeMillis()
        if (delay <= 0) return

        val request = OneTimeWorkRequest.Builder(workerClass)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .build()

        workManager.enqueueUniqueWork(workName, ExistingWorkPolicy.REPLACE, request)
    }

    fun scheduleDailyCheck() {
        val request = PeriodicWorkRequest.Builder(DailyCheckWorker::class.java, 24, TimeUnit.HOURS)
            .build()
        workManager.enqueueUniquePeriodicWork(
            "daily_check",
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }
}
