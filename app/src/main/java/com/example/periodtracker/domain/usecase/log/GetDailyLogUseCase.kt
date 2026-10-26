package com.example.periodtracker.domain.usecase.log

import com.example.periodtracker.data.repository.DailyLogRepositoryImpl
import com.example.periodtracker.domain.model.DailyLog
import kotlinx.coroutines.flow.Flow

class GetDailyLogUseCase(private val repository: DailyLogRepositoryImpl) {
    operator fun invoke(date: Long): Flow<DailyLog?> {
        return repository.getLogByDate(date)
    }
}