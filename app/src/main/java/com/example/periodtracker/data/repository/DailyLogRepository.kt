package com.example.periodtracker.data.repository

import com.example.periodtracker.data.local.dao.DailyLogDao
import com.example.periodtracker.data.local.entity.DailyLogEntity
import com.example.periodtracker.domain.model.DailyLog
import com.example.periodtracker.domain.model.FlowLevel
import com.example.periodtracker.domain.model.Mood
import com.example.periodtracker.domain.model.Symptom
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DailyLogRepositoryImpl(private val dao: DailyLogDao) {
    suspend fun insert(log: DailyLog): Long {
        val entity = toEntity(log)
        return dao.insert(entity)
    }

    suspend fun insertAll(logs: List<DailyLog>): List<Long> {
        val entities = logs.map { toEntity(it) }
        return dao.insertAll(entities)
    }

    suspend fun update(log: DailyLog): Int {
        val entity = toEntity(log)
        return dao.update(entity)
    }

    suspend fun delete(id: Long): Int {
        return dao.delete(id)
    }

    suspend fun deleteByDate(date: Long): Int {
        return dao.deleteByDate(date)
    }

    fun getLogByDate(date: Long): Flow<DailyLog?> {
        return dao.getLogByDate(date).map { it?.let { toDomain(it) } }
    }

    fun getLogsByCycleId(cycleId: Long): Flow<List<DailyLog>> {
        return dao.getLogsByCycleId(cycleId).map { entities ->
            entities.map { toDomain(it) }
        }
    }

    fun getLogsByDateRange(startDate: Long, endDate: Long): Flow<List<DailyLog>> {
        return dao.getLogsByDateRange(startDate, endDate).map { entities ->
            entities.map { toDomain(it) }
        }
    }

    fun getAllLogs(): Flow<List<DailyLog>> {
        return dao.getAllLogs().map { entities ->
            entities.map { toDomain(it) }
        }
    }

    fun getPeriodLogs(): Flow<List<DailyLog>> {
        return dao.getPeriodLogs().map { entities ->
            entities.map { toDomain(it) }
        }
    }

    fun getFlowHistory(): Flow<Map<Long, FlowLevel>> {
        return dao.getFlowHistory().map { pairs ->
            pairs.associate { (date, flowStr) ->
                date to FlowLevel.valueOf(flowStr)
            }
        }
    }

    private fun toEntity(log: DailyLog): DailyLogEntity {
        return DailyLogEntity(
            id = log.id,
            date = log.date,
            flow = log.flow.name,
            symptoms = log.symptoms.joinToString(",") { it.name },
            moods = log.moods.joinToString(",") { it.name },
            notes = log.notes,
            isPeriodDay = log.isPeriodDay,
            cycleId = log.cycleId
        )
    }

    private fun toDomain(entity: DailyLogEntity): DailyLog {
        return DailyLog(
            id = entity.id,
            date = entity.date,
            flow = FlowLevel.valueOf(entity.flow),
            symptoms = entity.symptoms.split(",").filter { it.isNotBlank() }.map { Symptom.valueOf(it) },
            moods = entity.moods.split(",").filter { it.isNotBlank() }.map { Mood.valueOf(it) },
            notes = entity.notes,
            isPeriodDay = entity.isPeriodDay,
            cycleId = entity.cycleId
        )
    }
}