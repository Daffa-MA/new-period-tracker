package com.example.periodtracker.data.repository

import com.example.periodtracker.data.local.dao.CycleDao
import com.example.periodtracker.data.local.entity.CycleEntity
import com.example.periodtracker.domain.model.Cycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class CycleRepositoryImpl(private val dao: CycleDao) {
    suspend fun insert(cycle: Cycle): Long {
        val entity = toEntity(cycle)
        return dao.insert(entity)
    }

    suspend fun update(cycle: Cycle): Int {
        val entity = toEntity(cycle)
        return dao.update(entity)
    }

    suspend fun delete(id: Long): Int {
        return dao.delete(id)
    }

    fun getAllCycles(): Flow<List<Cycle>> {
        return dao.getAllCycles().map { entities ->
            entities.map { toDomain(it) }
        }
    }

    fun getCycleById(id: Long): Flow<Cycle?> {
        return dao.getCycleById(id).map { it?.let { toDomain(it) } }
    }

    fun getCurrentCycle(date: Long = System.currentTimeMillis()): Flow<Cycle?> {
        return dao.getCurrentCycle(date).map { it?.let { toDomain(it) } }
    }

    fun getLastCompletedCycle(date: Long = System.currentTimeMillis()): Flow<Cycle?> {
        return dao.getLastCompletedCycle(date).map { it?.let { toDomain(it) } }
    }

    suspend fun getLastCompletedCycleSync(date: Long = System.currentTimeMillis()): Cycle? {
        return dao.getLastCompletedCycle(date).first()?.let { toDomain(it) }
    }

    fun getCompletedCycles(): Flow<List<Cycle>> {
        return dao.getCompletedCycles().map { entities ->
            entities.map { toDomain(it) }
        }
    }

    suspend fun getCompletedCyclesSync(): List<Cycle> {
        return dao.getCompletedCycles().first().map { toDomain(it) }
    }

    suspend fun getCycleCount(): Int {
        return dao.getCycleCount()
    }

    private fun toEntity(cycle: Cycle): CycleEntity {
        return CycleEntity(
            id = cycle.id,
            startDate = cycle.startDate,
            endDate = cycle.endDate,
            cycleLength = cycle.cycleLength,
            periodLength = cycle.periodLength,
            notes = cycle.notes,
            createdAt = cycle.createdAt
        )
    }

    private fun toDomain(entity: CycleEntity): Cycle {
        return Cycle(
            id = entity.id,
            startDate = entity.startDate,
            endDate = entity.endDate,
            cycleLength = entity.cycleLength,
            periodLength = entity.periodLength,
            notes = entity.notes,
            createdAt = entity.createdAt
        )
    }
}