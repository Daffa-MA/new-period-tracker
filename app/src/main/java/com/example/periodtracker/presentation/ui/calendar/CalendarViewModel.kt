package com.example.periodtracker.presentation.ui.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.periodtracker.data.repository.CycleRepositoryImpl
import com.example.periodtracker.data.repository.DailyLogRepositoryImpl
import com.example.periodtracker.domain.model.Cycle
import com.example.periodtracker.domain.model.DayStatus
import com.example.periodtracker.domain.model.PredictionResult
import com.example.periodtracker.domain.usecase.cycle.GetCyclesUseCase
import com.example.periodtracker.domain.usecase.cycle.PredictNextPeriodUseCase
import com.example.periodtracker.presentation.util.CycleCalculator
import com.example.periodtracker.presentation.util.DateUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch

class CalendarViewModel(
    private val getCyclesUseCase: GetCyclesUseCase,
    private val predictNextPeriodUseCase: PredictNextPeriodUseCase,
    private val cycleRepository: CycleRepositoryImpl,
    private val dailyLogRepository: DailyLogRepositoryImpl
) : ViewModel() {

    private val _currentMonth = MutableStateFlow(DateUtils.toLocalDate(System.currentTimeMillis()).withDayOfMonth(1))
    val currentMonth = _currentMonth.asStateFlow()

    private val _selectedDate = MutableStateFlow<Long?>(null)
    val selectedDate = _selectedDate.asStateFlow()

    val prediction = predictNextPeriodUseCase().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(), null
    )

    val dayStatuses = combine(
        dailyLogRepository.getFlowHistory(),
        prediction
    ) { flowMap, pred ->
        val month = _currentMonth.value
        CycleCalculator.getDayStatusesForMonth(month.year, month.monthValue, pred, flowMap)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val cycles = getCyclesUseCase().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(), emptyList()
    )

    fun onMonthChanged(year: Int, month: Int) {
        _currentMonth.value = java.time.LocalDate.of(year, month, 1)
    }

    fun onDayClick(date: Long) {
        _selectedDate.value = date
    }

    fun clearSelectedDate() {
        _selectedDate.value = null
    }

    fun getSelectedDate(): Long? {
        return _selectedDate.value
    }

    fun getCurrentMonth(): java.time.LocalDate {
        return _currentMonth.value
    }

    suspend fun startPeriod(date: Long = System.currentTimeMillis(), notes: String? = null) {
        cycleRepository.insert(
            Cycle(
                startDate = date,
                endDate = null,
                cycleLength = null,
                periodLength = null,
                notes = notes
            )
        )
    }

    suspend fun endPeriod(cycleId: Long, endDate: Long = System.currentTimeMillis()) {
        cycleRepository.getCycleById(cycleId).first()?.let { cycle ->
            val periodLength = DateUtils.daysBetween(cycle.startDate, endDate) + 1
            val updatedCycle = cycle.copy(
                endDate = endDate,
                periodLength = periodLength
            )
            cycleRepository.update(updatedCycle)
        }
    }
}