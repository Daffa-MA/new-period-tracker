package com.example.periodtracker.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.periodtracker.data.local.entity.CycleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CycleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cycle: CycleEntity): Long

    @Update
    suspend fun update(cycle: CycleEntity): Int

    @Query("DELETE FROM cycles WHERE id = :id")
    suspend fun delete(id: Long): Int

    @Query("SELECT * FROM cycles ORDER BY startDate DESC")
    fun getAllCycles(): Flow<List<CycleEntity>>

    @Query("SELECT * FROM cycles WHERE id = :id")
    fun getCycleById(id: Long): Flow<CycleEntity?>

    @Query("SELECT * FROM cycles WHERE startDate <= :date AND (endDate IS NULL OR endDate >= :date) ORDER BY startDate DESC LIMIT 1")
    fun getCurrentCycle(date: Long): Flow<CycleEntity?>

    @Query("SELECT * FROM cycles WHERE startDate < :date ORDER BY startDate DESC LIMIT 1")
    fun getLastCompletedCycle(date: Long): Flow<CycleEntity?>

    @Query("SELECT * FROM cycles WHERE endDate IS NOT NULL ORDER BY startDate DESC")
    fun getCompletedCycles(): Flow<List<CycleEntity>>

    @Query("SELECT COUNT(*) FROM cycles")
    suspend fun getCycleCount(): Int
}