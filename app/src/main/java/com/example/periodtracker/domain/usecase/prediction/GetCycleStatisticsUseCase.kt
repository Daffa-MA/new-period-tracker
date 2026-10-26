package com.example.periodtracker.domain.usecase.prediction

import com.example.periodtracker.data.repository.CycleRepositoryImpl
import com.example.periodtracker.data.repository.SettingsRepositoryImpl
import com.example.periodtracker.domain.model.CycleStatistics
import com.example.periodtracker.domain.model.Cycle
import com.example.periodtracker.domain.model.Settings
import com.example.periodtracker.presentation.util.CycleCalculator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class GetCycleStatisticsUseCase(
    private val cycleRepository: CycleRepositoryImpl,
    private val settingsRepository: SettingsRepositoryImpl
) {
    operator fun invoke(): Flow<CycleStatistics> {
        return combine(
            cycleRepository.getCompletedCycles(),
            settingsRepository.getSettings()
        ) { cycles, settings ->
            CycleCalculator.calculateCycleStatistics(cycles)
        }
    }
}