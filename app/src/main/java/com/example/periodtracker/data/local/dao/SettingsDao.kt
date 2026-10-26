package com.example.periodtracker.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.periodtracker.data.local.entity.SettingsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(settings: SettingsEntity): Long

    @Update
    suspend fun update(settings: SettingsEntity): Int

    @Query("SELECT * FROM settings WHERE key = :key")
    fun getSettings(key: String = "user_settings"): Flow<SettingsEntity?>

    @Query("SELECT * FROM settings WHERE key = :key")
    suspend fun getSettingsSync(key: String = "user_settings"): SettingsEntity?
}