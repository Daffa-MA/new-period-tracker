package com.example.periodtracker.presentation.ui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.periodtracker.data.repository.CycleRepositoryImpl
import com.example.periodtracker.data.repository.DailyLogRepositoryImpl
import com.example.periodtracker.domain.model.Cycle
import com.example.periodtracker.domain.model.CycleStatistics
import com.example.periodtracker.domain.model.DailyLog
import com.example.periodtracker.domain.model.FlowLevel
import com.example.periodtracker.domain.model.Mood
import com.example.periodtracker.domain.model.Symptom
import com.example.periodtracker.domain.usecase.prediction.GetCycleStatisticsUseCase
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted

class StatisticsViewModel(
    private val getCycleStatisticsUseCase: GetCycleStatisticsUseCase,
    private val cycleRepository: CycleRepositoryImpl,
    private val dailyLogRepository: DailyLogRepositoryImpl
) : ViewModel() {

    val statistics = getCycleStatisticsUseCase().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(),
        com.example.periodtracker.domain.model.CycleStatistics(0.0, 0.0, null, null, 0, 0.0)
    )

    val cycles = cycleRepository.getCompletedCycles().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(), emptyList()
    )

    val symptomFrequency = combine(
        dailyLogRepository.getAllLogs(),
        cycles
    ) { logs, cycles ->
        val symptomCounts = mutableMapOf<Symptom, Int>()
        val moodCounts = mutableMapOf<Mood, Int>()
        val flowCounts = mutableMapOf<FlowLevel, Int>()

        for (log in logs) {
            for (symptom in log.symptoms) {
                symptomCounts[symptom] = symptomCounts.getOrDefault(symptom, 0) + 1
            }
            for (mood in log.moods) {
                moodCounts[mood] = moodCounts.getOrDefault(mood, 0) + 1
            }
            if (log.flow != FlowLevel.NONE) {
                flowCounts[log.flow] = flowCounts.getOrDefault(log.flow, 0) + 1
            }
        }

        StatisticsData(
            symptomCounts = symptomCounts.toMap(),
            moodCounts = moodCounts.toMap(),
            flowCounts = flowCounts.toMap()
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), StatisticsData(emptyMap(), emptyMap(), emptyMap()))

    data class StatisticsData(
        val symptomCounts: Map<Symptom, Int>,
        val moodCounts: Map<Mood, Int>,
        val flowCounts: Map<FlowLevel, Int>
    )
}