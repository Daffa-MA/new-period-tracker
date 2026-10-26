package com.example.periodtracker.di

import android.content.Context
import com.example.periodtracker.data.local.PeriodDatabase
import com.example.periodtracker.data.local.dao.CycleDao
import com.example.periodtracker.data.local.dao.DailyLogDao
import com.example.periodtracker.data.local.dao.SettingsDao
import com.example.periodtracker.data.repository.CycleRepositoryImpl
import com.example.periodtracker.data.repository.DailyLogRepositoryImpl
import com.example.periodtracker.data.repository.SettingsRepositoryImpl
import com.example.periodtracker.domain.notification.NotificationScheduler
import com.example.periodtracker.domain.usecase.cycle.EndPeriodUseCase
import com.example.periodtracker.domain.usecase.cycle.GetCyclesUseCase
import com.example.periodtracker.domain.usecase.cycle.PredictNextPeriodUseCase
import com.example.periodtracker.domain.usecase.cycle.StartPeriodUseCase
import com.example.periodtracker.domain.usecase.log.GetDailyLogUseCase
import com.example.periodtracker.domain.usecase.log.SaveDailyLogUseCase
import com.example.periodtracker.domain.usecase.prediction.GetCycleStatisticsUseCase
import com.example.periodtracker.domain.usecase.settings.GetSettingsUseCase

class AppContainer(private val context: Context) {
    private val database: PeriodDatabase = PeriodDatabase.getInstance(context)
    
    // DAOs
    private val cycleDao: CycleDao = database.cycleDao()
    private val dailyLogDao: DailyLogDao = database.dailyLogDao()
    private val settingsDao: SettingsDao = database.settingsDao()
    
    // Repositories
    private val cycleRepository: CycleRepositoryImpl = CycleRepositoryImpl(cycleDao)
    private val dailyLogRepository: DailyLogRepositoryImpl = DailyLogRepositoryImpl(dailyLogDao)
    private val settingsRepository: SettingsRepositoryImpl = SettingsRepositoryImpl(settingsDao)
    
    // UseCases
    val getCyclesUseCase: GetCyclesUseCase = GetCyclesUseCase(cycleRepository)
    val startPeriodUseCase: StartPeriodUseCase = StartPeriodUseCase(cycleRepository)
    val endPeriodUseCase: EndPeriodUseCase = EndPeriodUseCase(cycleRepository)
    val predictNextPeriodUseCase: PredictNextPeriodUseCase = PredictNextPeriodUseCase(cycleRepository, settingsRepository)
    val getDailyLogUseCase: GetDailyLogUseCase = GetDailyLogUseCase(dailyLogRepository)
    val saveDailyLogUseCase: SaveDailyLogUseCase = SaveDailyLogUseCase(dailyLogRepository)
    val getCycleStatisticsUseCase: GetCycleStatisticsUseCase = GetCycleStatisticsUseCase(cycleRepository, settingsRepository)
    val getSettingsUseCase: GetSettingsUseCase = GetSettingsUseCase(settingsRepository)
    val notificationScheduler: NotificationScheduler = NotificationScheduler(context)
    
    // ViewModels - created on demand
    fun createCalendarViewModel() = com.example.periodtracker.presentation.ui.calendar.CalendarViewModel(
        getCyclesUseCase = getCyclesUseCase,
        predictNextPeriodUseCase = predictNextPeriodUseCase,
        cycleRepository = cycleRepository,
        dailyLogRepository = dailyLogRepository
    )
    
    fun createDailyLogViewModel() = com.example.periodtracker.presentation.ui.dailylog.DailyLogViewModel(
        getDailyLogUseCase = getDailyLogUseCase,
        dailyLogRepository = dailyLogRepository,
        cycleRepository = cycleRepository
    )
    
    fun createPredictionsViewModel() = com.example.periodtracker.presentation.ui.predictions.PredictionsViewModel(
        predictNextPeriodUseCase = predictNextPeriodUseCase,
        getCycleStatisticsUseCase = getCycleStatisticsUseCase
    )
    
    fun createStatisticsViewModel() = com.example.periodtracker.presentation.ui.statistics.StatisticsViewModel(
        getCycleStatisticsUseCase = getCycleStatisticsUseCase,
        cycleRepository = cycleRepository,
        dailyLogRepository = dailyLogRepository
    )
    
    fun createSettingsViewModel() = com.example.periodtracker.presentation.ui.settings.SettingsViewModel(
        getSettingsUseCase = getSettingsUseCase,
        settingsRepository = settingsRepository
    )
    
    fun createOnboardingViewModel() = com.example.periodtracker.presentation.ui.onboarding.OnboardingViewModel(
        cycleRepository = cycleRepository,
        settingsRepository = settingsRepository
    )
}

class AppContainerProvider {
    companion object {
        @Volatile private var INSTANCE: AppContainer? = null
        
        fun initialize(context: Context) {
            INSTANCE = AppContainer(context.applicationContext)
        }
        
        fun get(): AppContainer {
            return INSTANCE ?: throw IllegalStateException("AppContainer not initialized. Call initialize() first.")
        }
    }
}