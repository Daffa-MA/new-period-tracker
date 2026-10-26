package com.example.periodtracker.di

import com.example.periodtracker.data.repository.CycleRepositoryImpl
import com.example.periodtracker.data.repository.DailyLogRepositoryImpl
import com.example.periodtracker.data.repository.SettingsRepositoryImpl

object RepositoryModule {
    fun provideCycleRepository(impl: CycleRepositoryImpl): CycleRepositoryImpl = impl

    fun provideDailyLogRepository(impl: DailyLogRepositoryImpl): DailyLogRepositoryImpl = impl

    fun provideSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepositoryImpl = impl
}