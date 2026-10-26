package com.example.periodtracker.domain.usecase.settings

import com.example.periodtracker.data.repository.SettingsRepositoryImpl
import com.example.periodtracker.domain.model.Settings
import kotlinx.coroutines.flow.Flow

class GetSettingsUseCase(private val repository: SettingsRepositoryImpl) {
    operator fun invoke(): Flow<Settings?> {
        return repository.getSettings()
    }
}