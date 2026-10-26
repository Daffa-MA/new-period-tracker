package com.example.periodtracker.domain.notification

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.periodtracker.presentation.util.NotificationHelper

class FertileWindowReminderWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val notificationHelper = NotificationHelper(applicationContext)
            notificationHelper.showFertileWindowReminder()
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}