package com.example.periodtracker.presentation.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.periodtracker.data.repository.SettingsRepositoryImpl
import com.example.periodtracker.domain.model.Settings
import com.example.periodtracker.domain.usecase.settings.GetSettingsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val getSettingsUseCase: GetSettingsUseCase,
    private val settingsRepository: SettingsRepositoryImpl
) : ViewModel() {

    val settings = getSettingsUseCase().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(), null
    )

    private val _averageCycleLength = MutableStateFlow(28)
    val averageCycleLength = _averageCycleLength.asStateFlow()

    private val _averagePeriodLength = MutableStateFlow(5)
    val averagePeriodLength = _averagePeriodLength.asStateFlow()

    private val _remindersEnabled = MutableStateFlow(true)
    val remindersEnabled = _remindersEnabled.asStateFlow()

    private val _periodReminderDaysBefore = MutableStateFlow(2)
    val periodReminderDaysBefore = _periodReminderDaysBefore.asStateFlow()

    private val _ovulationReminderEnabled = MutableStateFlow(true)
    val ovulationReminderEnabled = _ovulationReminderEnabled.asStateFlow()

    private val _fertileWindowReminderEnabled = MutableStateFlow(false)
    val fertileWindowReminderEnabled = _fertileWindowReminderEnabled.asStateFlow()

    private val _themeMode = MutableStateFlow(0)
    val themeMode = _themeMode.asStateFlow()

    private val _language = MutableStateFlow("id")
    val language = _language.asStateFlow()

    init {
        observeSettings()
    }

    private fun observeSettings() {
        viewModelScope.launch {
            getSettingsUseCase().collect { settings ->
                settings?.let {
                    _averageCycleLength.value = it.averageCycleLength
                    _averagePeriodLength.value = it.averagePeriodLength
                    _remindersEnabled.value = it.remindersEnabled
                    _periodReminderDaysBefore.value = it.periodReminderDaysBefore
                    _ovulationReminderEnabled.value = it.ovulationReminderEnabled
                    _fertileWindowReminderEnabled.value = it.fertileWindowReminderEnabled
                    _themeMode.value = it.themeMode
                    _language.value = it.language
                }
            }
        }
    }

    fun onAverageCycleLengthChanged(value: Int) {
        _averageCycleLength.value = value.coerceIn(21, 45)
    }

    fun onAveragePeriodLengthChanged(value: Int) {
        _averagePeriodLength.value = value.coerceIn(1, 10)
    }

    fun onRemindersEnabledChanged(enabled: Boolean) {
        _remindersEnabled.value = enabled
    }

    fun onPeriodReminderDaysBeforeChanged(days: Int) {
        _periodReminderDaysBefore.value = days.coerceIn(0, 7)
    }

    fun onOvulationReminderEnabledChanged(enabled: Boolean) {
        _ovulationReminderEnabled.value = enabled
    }

    fun onFertileWindowReminderEnabledChanged(enabled: Boolean) {
        _fertileWindowReminderEnabled.value = enabled
    }

    fun onThemeModeChanged(mode: Int) {
        _themeMode.value = mode
    }

    fun onLanguageChanged(lang: String) {
        _language.value = lang
    }

    suspend fun saveSettings() {
        val currentSettings = settings.value ?: Settings()
        val updatedSettings = currentSettings.copy(
            averageCycleLength = _averageCycleLength.value,
            averagePeriodLength = _averagePeriodLength.value,
            remindersEnabled = _remindersEnabled.value,
            periodReminderDaysBefore = _periodReminderDaysBefore.value,
            ovulationReminderEnabled = _ovulationReminderEnabled.value,
            fertileWindowReminderEnabled = _fertileWindowReminderEnabled.value,
            themeMode = _themeMode.value,
            language = _language.value
        )
        settingsRepository.update(updatedSettings)
    }

    suspend fun exportData(): String {
        // TODO: Implement CSV export
        return ""
    }

    suspend fun importData(csv: String): Boolean {
        // TODO: Implement CSV import
        return false
    }
}