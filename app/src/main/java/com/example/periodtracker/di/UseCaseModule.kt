package com.example.periodtracker.di

import android.content.Context
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

object UseCaseModule {
    fun provideGetCyclesUseCase(repository: CycleRepositoryImpl): GetCyclesUseCase = GetCyclesUseCase(repository)

    fun provideStartPeriodUseCase(repository: CycleRepositoryImpl): StartPeriodUseCase = StartPeriodUseCase(repository)

    fun provideEndPeriodUseCase(repository: CycleRepositoryImpl): EndPeriodUseCase = EndPeriodUseCase(repository)

    fun providePredictNextPeriodUseCase(
        cycleRepository: CycleRepositoryImpl,
        settingsRepository: SettingsRepositoryImpl
    ): PredictNextPeriodUseCase = PredictNextPeriodUseCase(cycleRepository, settingsRepository)

    fun provideGetDailyLogUseCase(repository: DailyLogRepositoryImpl): GetDailyLogUseCase = GetDailyLogUseCase(repository)

    fun provideSaveDailyLogUseCase(repository: DailyLogRepositoryImpl): SaveDailyLogUseCase = SaveDailyLogUseCase(repository)

    fun provideGetCycleStatisticsUseCase(
        cycleRepository: CycleRepositoryImpl,
        settingsRepository: SettingsRepositoryImpl
    ): GetCycleStatisticsUseCase = GetCycleStatisticsUseCase(cycleRepository, settingsRepository)

    fun provideGetSettingsUseCase(repository: SettingsRepositoryImpl): GetSettingsUseCase = GetSettingsUseCase(repository)

    fun provideNotificationScheduler(context: Context): NotificationScheduler = NotificationScheduler(context)
}