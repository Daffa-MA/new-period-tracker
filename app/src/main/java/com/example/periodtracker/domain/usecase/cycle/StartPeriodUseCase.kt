package com.example.periodtracker.domain.usecase.cycle

import com.example.periodtracker.data.repository.CycleRepositoryImpl
import com.example.periodtracker.domain.model.Cycle

class StartPeriodUseCase(private val repository: CycleRepositoryImpl) {
    operator fun invoke(startDate: Long = System.currentTimeMillis(), notes: String? = null): Cycle {
        val cycle = Cycle(
            startDate = startDate,
            endDate = null,
            cycleLength = null,
            periodLength = null,
            notes = notes
        )
        // Note: Insert is done in ViewModel since it's suspend
        return cycle
    }
}