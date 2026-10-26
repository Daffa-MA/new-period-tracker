package com.example.periodtracker.di

import android.content.Context
import com.example.periodtracker.data.local.PeriodDatabase
import com.example.periodtracker.data.local.dao.CycleDao
import com.example.periodtracker.data.local.dao.DailyLogDao
import com.example.periodtracker.data.local.dao.SettingsDao

object DatabaseModule {
    fun provideDatabase(context: Context): PeriodDatabase {
        return PeriodDatabase.getInstance(context)
    }

    fun provideCycleDao(database: PeriodDatabase): CycleDao {
        return database.cycleDao()
    }

    fun provideDailyLogDao(database: PeriodDatabase): DailyLogDao {
        return database.dailyLogDao()
    }

    fun provideSettingsDao(database: PeriodDatabase): SettingsDao {
        return database.settingsDao()
    }
}