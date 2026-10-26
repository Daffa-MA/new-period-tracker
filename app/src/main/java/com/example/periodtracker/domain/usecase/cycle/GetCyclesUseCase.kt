package com.example.periodtracker.domain.usecase.cycle

import com.example.periodtracker.data.repository.CycleRepositoryImpl
import com.example.periodtracker.domain.model.Cycle
import kotlinx.coroutines.flow.Flow

class GetCyclesUseCase(private val repository: CycleRepositoryImpl) {
    operator fun invoke(): Flow<List<Cycle>> {
        return repository.getAllCycles()
    }
}