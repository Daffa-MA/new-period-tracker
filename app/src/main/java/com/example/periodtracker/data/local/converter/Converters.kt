package com.example.periodtracker.data.local.converter

import androidx.room.TypeConverter
import com.example.periodtracker.domain.model.FlowLevel
import com.example.periodtracker.domain.model.Mood
import com.example.periodtracker.domain.model.Symptom
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromFlowLevel(value: String): FlowLevel = FlowLevel.valueOf(value)

    @TypeConverter
    fun toFlowLevel(value: FlowLevel): String = value.name

    @TypeConverter
    fun fromSymptomList(value: String): List<Symptom> {
        return json.decodeFromString<List<Symptom>>(value)
    }

    @TypeConverter
    fun toSymptomList(value: List<Symptom>): String {
        return json.encodeToString(value)
    }

    @TypeConverter
    fun fromMoodList(value: String): List<Mood> {
        return json.decodeFromString<List<Mood>>(value)
    }

    @TypeConverter
    fun toMoodList(value: List<Mood>): String {
        return json.encodeToString(value)
    }
}