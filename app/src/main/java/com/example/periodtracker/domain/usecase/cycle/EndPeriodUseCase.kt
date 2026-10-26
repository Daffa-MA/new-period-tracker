package com.example.periodtracker.domain.usecase.cycle

import com.example.periodtracker.data.repository.CycleRepositoryImpl
import com.example.periodtracker.domain.model.Cycle
import com.example.periodtracker.presentation.util.DateUtils

class EndPeriodUseCase(private val repository: CycleRepositoryImpl) {
    operator fun invoke(cycle: Cycle, endDate: Long = System.currentTimeMillis()): Cycle {
        val periodLength = DateUtils.daysBetween(cycle.startDate, endDate) + 1
        return cycle.copy(
            endDate = endDate,
            periodLength = periodLength,
            cycleLength = cycle.cycleLength
        )
    }
}