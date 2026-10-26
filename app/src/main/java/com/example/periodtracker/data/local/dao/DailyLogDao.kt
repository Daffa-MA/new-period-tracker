package com.example.periodtracker.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.periodtracker.data.local.entity.DailyLogEntity
import kotlinx.coroutines.flow.Flow

data class FlowHistoryItem(
    val date: Long,
    val flow: String
)

@Dao
interface DailyLogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(log: DailyLogEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(logs: List<DailyLogEntity>): List<Long>

    @Update
    suspend fun update(log: DailyLogEntity): Int

    @Query("DELETE FROM daily_logs WHERE id = :id")
    suspend fun delete(id: Long): Int

    @Query("DELETE FROM daily_logs WHERE date = :date")
    suspend fun deleteByDate(date: Long): Int

    @Query("SELECT * FROM daily_logs WHERE date = :date")
    fun getLogByDate(date: Long): Flow<DailyLogEntity?>

    @Query("SELECT * FROM daily_logs WHERE cycleId = :cycleId ORDER BY date ASC")
    fun getLogsByCycleId(cycleId: Long): Flow<List<DailyLogEntity>>

    @Query("SELECT * FROM daily_logs WHERE date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    fun getLogsByDateRange(startDate: Long, endDate: Long): Flow<List<DailyLogEntity>>

    @Query("SELECT * FROM daily_logs ORDER BY date DESC")
    fun getAllLogs(): Flow<List<DailyLogEntity>>

    @Query("SELECT * FROM daily_logs WHERE isPeriodDay = 1 ORDER BY date DESC")
    fun getPeriodLogs(): Flow<List<DailyLogEntity>>

    @Query("SELECT date, flow FROM daily_logs WHERE flow != 'NONE' ORDER BY date DESC")
    fun getFlowHistory(): Flow<List<FlowHistoryItem>>
}