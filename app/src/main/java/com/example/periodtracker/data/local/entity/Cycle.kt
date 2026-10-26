package com.example.periodtracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cycles")
data class CycleEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val startDate: Long,
    val endDate: Long?,
    val cycleLength: Int?,
    val periodLength: Int?,
    val notes: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)