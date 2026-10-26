package com.example.periodtracker.presentation.ui.predictions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.periodtracker.domain.model.CycleStatistics
import com.example.periodtracker.domain.model.PredictionResult
import com.example.periodtracker.domain.usecase.cycle.PredictNextPeriodUseCase
import com.example.periodtracker.domain.usecase.prediction.GetCycleStatisticsUseCase
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted

class PredictionsViewModel(
    private val predictNextPeriodUseCase: PredictNextPeriodUseCase,
    private val getCycleStatisticsUseCase: GetCycleStatisticsUseCase
) : ViewModel() {

    val prediction = predictNextPeriodUseCase().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(), null
    )

    val statistics = getCycleStatisticsUseCase().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(),
        com.example.periodtracker.domain.model.CycleStatistics(0.0, 0.0, null, null, 0, 0.0)
    )
}