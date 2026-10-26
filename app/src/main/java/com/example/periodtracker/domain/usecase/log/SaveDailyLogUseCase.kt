package com.example.periodtracker.domain.usecase.log

import com.example.periodtracker.data.repository.DailyLogRepositoryImpl
import com.example.periodtracker.domain.model.DailyLog

class SaveDailyLogUseCase(private val repository: DailyLogRepositoryImpl) {
    operator fun invoke(log: DailyLog): DailyLog {
        // The actual insert is done in ViewModel since it's suspend
        return log
    }
}