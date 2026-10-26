package com.example.periodtracker.presentation.ui.dailylog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.periodtracker.data.repository.CycleRepositoryImpl
import com.example.periodtracker.data.repository.DailyLogRepositoryImpl
import com.example.periodtracker.domain.model.DailyLog
import com.example.periodtracker.domain.model.FlowLevel
import com.example.periodtracker.domain.model.Mood
import com.example.periodtracker.domain.model.Symptom
import com.example.periodtracker.domain.usecase.log.GetDailyLogUseCase
import com.example.periodtracker.presentation.util.DateUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DailyLogViewModel(
    private val getDailyLogUseCase: GetDailyLogUseCase,
    private val dailyLogRepository: DailyLogRepositoryImpl,
    private val cycleRepository: CycleRepositoryImpl
) : ViewModel() {

    private val _initialDate = MutableStateFlow<Long>(System.currentTimeMillis())
    val initialDate = _initialDate.asStateFlow()

    private val _flowLevel = MutableStateFlow<FlowLevel>(FlowLevel.NONE)
    val flow = _flowLevel.asStateFlow()

    private val _symptoms = MutableStateFlow<MutableSet<Symptom>>(mutableSetOf())
    val symptoms = _symptoms.asStateFlow()

    private val _moods = MutableStateFlow<MutableSet<Mood>>(mutableSetOf())
    val moods = _moods.asStateFlow()

    private val _notes = MutableStateFlow<String>("")
    val notes = _notes.asStateFlow()

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading = _isLoading.asStateFlow()

    private val _currentCycleId = MutableStateFlow<Long?>(null)
    val currentCycleId = _currentCycleId.asStateFlow()

    val existingLog = MutableStateFlow<DailyLog?>(null)

    fun initialize(date: Long) {
        _initialDate.value = date
        loadLog(date)
    }

    private fun loadLog(date: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            val log = getDailyLogUseCase(date).first()
            existingLog.value = log
            if (log != null) {
                _flowLevel.value = log.flow
                _symptoms.value = log.symptoms.toMutableSet()
                _moods.value = log.moods.toMutableSet()
                _notes.value = log.notes ?: ""
            } else {
                _flowLevel.value = FlowLevel.NONE
                _symptoms.value = mutableSetOf()
                _moods.value = mutableSetOf()
                _notes.value = ""
            }
            _isLoading.value = false
        }
    }

    fun onFlowSelected(flowLevel: FlowLevel) {
        _flowLevel.value = flowLevel
    }

    fun onSymptomToggle(symptom: Symptom) {
        val current = _symptoms.value.toMutableSet()
        if (current.contains(symptom)) current.remove(symptom) else current.add(symptom)
        _symptoms.value = current
    }

    fun onMoodToggle(mood: Mood) {
        val current = _moods.value.toMutableSet()
        if (current.contains(mood)) current.remove(mood) else current.add(mood)
        _moods.value = current
    }

    fun onNotesChanged(text: String) {
        _notes.value = text
    }

    fun setCurrentCycleId(cycleId: Long?) {
        _currentCycleId.value = cycleId
    }

    fun saveLog() {
        viewModelScope.launch {
            val date = _initialDate.value
            val startOfDay = DateUtils.startOfDay(date)

            val log = DailyLog(
                id = existingLog.value?.id ?: 0,
                date = startOfDay,
                flow = _flowLevel.value,
                symptoms = _symptoms.value.toList(),
                moods = _moods.value.toList(),
                notes = _notes.value.ifBlank { null },
                isPeriodDay = _flowLevel.value != FlowLevel.NONE,
                cycleId = _currentCycleId.value
            )

            try {
                if (existingLog.value != null) {
                    dailyLogRepository.update(log)
                } else {
                    dailyLogRepository.insert(log)
                }
            } catch (_: Exception) {
            }
        }
    }

    fun deleteLog() {
        viewModelScope.launch {
            existingLog.value?.let { log ->
                try {
                    dailyLogRepository.delete(log.id)
                } catch (_: Exception) {
                }
            }
        }
    }

    fun hasData(): Boolean {
        return _flowLevel.value != FlowLevel.NONE ||
               _symptoms.value.isNotEmpty() ||
               _moods.value.isNotEmpty() ||
               _notes.value.isNotBlank()
    }
}
