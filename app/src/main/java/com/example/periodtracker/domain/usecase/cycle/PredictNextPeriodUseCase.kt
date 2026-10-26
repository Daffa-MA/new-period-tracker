package com.example.periodtracker.domain.usecase.cycle

import com.example.periodtracker.data.repository.CycleRepositoryImpl
import com.example.periodtracker.data.repository.SettingsRepositoryImpl
import com.example.periodtracker.domain.model.Cycle
import com.example.periodtracker.domain.model.PredictionResult
import com.example.periodtracker.domain.model.Settings
import com.example.periodtracker.presentation.util.CycleCalculator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class PredictNextPeriodUseCase(
    private val cycleRepository: CycleRepositoryImpl,
    private val settingsRepository: SettingsRepositoryImpl
) {
    operator fun invoke(): Flow<PredictionResult?> {
        return combine(
            cycleRepository.getLastCompletedCycle(),
            settingsRepository.getSettings(),
            cycleRepository.getCompletedCycles()
        ) { lastCycle, settings, completedCycles ->
            val lastPeriodStart = lastCycle?.startDate ?: settings?.lastPeriodStart
            val userSettings = settings ?: Settings()
            CycleCalculator.calculatePredictions(lastPeriodStart, userSettings, completedCycles)
        }
    }
}