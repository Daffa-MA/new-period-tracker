package com.example.periodtracker.presentation.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.periodtracker.data.repository.CycleRepositoryImpl
import com.example.periodtracker.data.repository.SettingsRepositoryImpl
import com.example.periodtracker.domain.model.Cycle
import com.example.periodtracker.domain.model.Settings
import com.example.periodtracker.presentation.util.DateUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val cycleRepository: CycleRepositoryImpl,
    private val settingsRepository: SettingsRepositoryImpl
) : ViewModel() {

    private val _currentStep = MutableStateFlow(0)
    val currentStep = _currentStep.asStateFlow()

    private val _lastPeriodDate = MutableStateFlow<Long?>(null)
    val lastPeriodDate = _lastPeriodDate.asStateFlow()

    private val _averageCycleLength = MutableStateFlow(28)
    val averageCycleLength = _averageCycleLength.asStateFlow()

    private val _averagePeriodLength = MutableStateFlow(5)
    val averagePeriodLength = _averagePeriodLength.asStateFlow()

    private val _notificationsEnabled = MutableStateFlow(true)
    val notificationsEnabled = _notificationsEnabled.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    val totalSteps = 5

    fun nextStep() {
        if (_currentStep.value < totalSteps - 1) {
            _currentStep.value++
        }
    }

    fun previousStep() {
        if (_currentStep.value > 0) {
            _currentStep.value--
        }
    }

    fun onLastPeriodDateSelected(date: Long) {
        _lastPeriodDate.value = DateUtils.startOfDay(date)
    }

    fun onAverageCycleLengthChanged(length: Int) {
        _averageCycleLength.value = length.coerceIn(21, 45)
    }

    fun onAveragePeriodLengthChanged(length: Int) {
        _averagePeriodLength.value = length.coerceIn(1, 10)
    }

    fun onNotificationsEnabledChanged(enabled: Boolean) {
        _notificationsEnabled.value = enabled
    }

    suspend fun completeOnboarding(): Boolean {
        _isLoading.value = true
        return try {
            // Save settings
            val settings = Settings(
                averageCycleLength = _averageCycleLength.value,
                averagePeriodLength = _averagePeriodLength.value,
                lastPeriodStart = _lastPeriodDate.value,
                remindersEnabled = _notificationsEnabled.value
            )
            settingsRepository.insert(settings)

            // Create first cycle if last period date is provided
            _lastPeriodDate.value?.let { startDate ->
                val endDate = DateUtils.addDays(startDate, _averagePeriodLength.value - 1)
                val cycle = Cycle(
                    startDate = startDate,
                    endDate = endDate,
                    periodLength = _averagePeriodLength.value,
                    cycleLength = _averageCycleLength.value
                )
                cycleRepository.insert(cycle)
            }

            true
        } catch (e: Exception) {
            false
        } finally {
            _isLoading.value = false
        }
    }

    fun canProceed(): Boolean {
        return when (_currentStep.value) {
            0 -> true // Welcome step always allows proceeding
            1 -> _lastPeriodDate.value != null // Need date selected
            2 -> _averageCycleLength.value >= 21 && _averageCycleLength.value <= 45
            3 -> _averagePeriodLength.value >= 1 && _averagePeriodLength.value <= 10
            else -> true
        }
    }
}