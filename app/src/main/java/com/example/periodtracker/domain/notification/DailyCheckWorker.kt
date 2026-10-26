package com.example.periodtracker.domain.notification

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.periodtracker.data.local.PeriodDatabase
import com.example.periodtracker.data.repository.CycleRepositoryImpl
import com.example.periodtracker.data.repository.DailyLogRepositoryImpl
import com.example.periodtracker.data.repository.SettingsRepositoryImpl
import com.example.periodtracker.domain.model.Settings
import com.example.periodtracker.domain.model.PredictionResult
import com.example.periodtracker.presentation.util.CycleCalculator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DailyCheckWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val db = PeriodDatabase.getInstance(applicationContext)
            val cycleRepo = CycleRepositoryImpl(db.cycleDao())
            val logRepo = DailyLogRepositoryImpl(db.dailyLogDao())
            val settingsRepo = SettingsRepositoryImpl(db.settingsDao())

            val settings = settingsRepo.getSettingsSync() ?: Settings()
            val lastCycle = cycleRepo.getLastCompletedCycleSync()
            val completedCycles = cycleRepo.getCompletedCyclesSync()

            val prediction = CycleCalculator.calculatePredictions(
                lastCycle?.startDate ?: settings.lastPeriodStart,
                settings,
                completedCycles
            )

            if (prediction != null) {
                val scheduler = NotificationScheduler(applicationContext)
                scheduler.schedulePeriodReminders(settings, prediction)
            }

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}