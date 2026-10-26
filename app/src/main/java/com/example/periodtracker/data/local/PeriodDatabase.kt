package com.example.periodtracker.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.periodtracker.data.local.converter.Converters
import com.example.periodtracker.data.local.dao.CycleDao
import com.example.periodtracker.data.local.dao.DailyLogDao
import com.example.periodtracker.data.local.dao.SettingsDao
import com.example.periodtracker.data.local.entity.CycleEntity
import com.example.periodtracker.data.local.entity.DailyLogEntity
import com.example.periodtracker.data.local.entity.SettingsEntity

@Database(
    entities = [CycleEntity::class, DailyLogEntity::class, SettingsEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class PeriodDatabase : RoomDatabase() {
    abstract fun cycleDao(): CycleDao
    abstract fun dailyLogDao(): DailyLogDao
    abstract fun settingsDao(): SettingsDao

    companion object {
        @Volatile private var INSTANCE: PeriodDatabase? = null

        fun getInstance(context: Context): PeriodDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PeriodDatabase::class.java,
                    "period_tracker_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}