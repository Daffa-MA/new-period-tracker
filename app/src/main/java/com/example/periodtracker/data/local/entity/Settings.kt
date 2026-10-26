package com.example.periodtracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class SettingsEntity(
    @PrimaryKey val key: String = "user_settings",
    val averageCycleLength: Int = 28,
    val averagePeriodLength: Int = 5,
    val lastPeriodStart: Long? = null,
    val remindersEnabled: Boolean = true,
    val periodReminderDaysBefore: Int = 2,
    val ovulationReminderEnabled: Boolean = true,
    val fertileWindowReminderEnabled: Boolean = false,
    val themeMode: Int = 0,
    val language: String = "id",
    val dataExportEnabled: Boolean = true
)