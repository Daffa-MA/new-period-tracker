package com.example.periodtracker.data.repository

import com.example.periodtracker.data.local.dao.SettingsDao
import com.example.periodtracker.data.local.entity.SettingsEntity
import com.example.periodtracker.domain.model.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepositoryImpl(private val dao: SettingsDao) {
    suspend fun insert(settings: Settings): Long {
        val entity = toEntity(settings)
        return dao.insert(entity)
    }

    suspend fun update(settings: Settings): Int {
        val entity = toEntity(settings)
        return dao.update(entity)
    }

    fun getSettings(): Flow<Settings?> {
        return dao.getSettings().map { it?.let { toDomain(it) } }
    }

    suspend fun getSettingsSync(): Settings? {
        return dao.getSettingsSync().let { it?.let { toDomain(it) } }
    }

    private fun toEntity(settings: Settings): SettingsEntity {
        return SettingsEntity(
            key = settings.key,
            averageCycleLength = settings.averageCycleLength,
            averagePeriodLength = settings.averagePeriodLength,
            lastPeriodStart = settings.lastPeriodStart,
            remindersEnabled = settings.remindersEnabled,
            periodReminderDaysBefore = settings.periodReminderDaysBefore,
            ovulationReminderEnabled = settings.ovulationReminderEnabled,
            fertileWindowReminderEnabled = settings.fertileWindowReminderEnabled,
            themeMode = settings.themeMode,
            language = settings.language,
            dataExportEnabled = settings.dataExportEnabled
        )
    }

    private fun toDomain(entity: SettingsEntity): Settings {
        return Settings(
            key = entity.key,
            averageCycleLength = entity.averageCycleLength,
            averagePeriodLength = entity.averagePeriodLength,
            lastPeriodStart = entity.lastPeriodStart,
            remindersEnabled = entity.remindersEnabled,
            periodReminderDaysBefore = entity.periodReminderDaysBefore,
            ovulationReminderEnabled = entity.ovulationReminderEnabled,
            fertileWindowReminderEnabled = entity.fertileWindowReminderEnabled,
            themeMode = entity.themeMode,
            language = entity.language,
            dataExportEnabled = entity.dataExportEnabled
        )
    }
}