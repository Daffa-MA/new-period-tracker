package com.example.periodtracker.domain.model

import com.example.periodtracker.presentation.util.DateUtils

enum class FlowLevel {
    NONE, SPOTTING, LIGHT, MEDIUM, HEAVY
}

enum class Symptom {
    CRAMPS, HEADACHE, BLOATING, ACNE, BREAST_TENDERNESS,
    FATIGUE, NAUSEA, BACK_PAIN, MOOD_SWINGS, FOOD_CRAVINGS
}

enum class Mood {
    HAPPY, SAD, ANXIOUS, IRRITABLE, CALM, ENERGETIC, TIRED, EMOTIONAL
}

fun FlowLevel.getDisplayName(): String = when (this) {
    FlowLevel.NONE -> "None"
    FlowLevel.SPOTTING -> "Spotting"
    FlowLevel.LIGHT -> "Light"
    FlowLevel.MEDIUM -> "Medium"
    FlowLevel.HEAVY -> "Heavy"
}

fun Symptom.getDisplayName(): String = when (this) {
    Symptom.CRAMPS -> "Cramps"
    Symptom.HEADACHE -> "Headache"
    Symptom.BLOATING -> "Bloating"
    Symptom.ACNE -> "Acne"
    Symptom.BREAST_TENDERNESS -> "Breast Tenderness"
    Symptom.FATIGUE -> "Fatigue"
    Symptom.NAUSEA -> "Nausea"
    Symptom.BACK_PAIN -> "Back Pain"
    Symptom.MOOD_SWINGS -> "Mood Swings"
    Symptom.FOOD_CRAVINGS -> "Food Cravings"
}

fun Mood.getDisplayName(): String = when (this) {
    Mood.HAPPY -> "Happy"
    Mood.SAD -> "Sad"
    Mood.ANXIOUS -> "Anxious"
    Mood.IRRITABLE -> "Irritable"
    Mood.CALM -> "Calm"
    Mood.ENERGETIC -> "Energetic"
    Mood.TIRED -> "Tired"
    Mood.EMOTIONAL -> "Emotional"
}

data class Cycle(
    val id: Long = 0,
    val startDate: Long,
    val endDate: Long?,
    val cycleLength: Int?,
    val periodLength: Int?,
    val notes: String? = null,
    val createdAt: Long = System.currentTimeMillis()
) {
    val isOngoing: Boolean
        get() = endDate == null

    val startDateFormatted: String
        get() = DateUtils.formatDate(startDate)

    val endDateFormatted: String?
        get() = endDate?.let { DateUtils.formatDate(it) }
}

data class DailyLog(
    val id: Long = 0,
    val date: Long,
    val flow: FlowLevel = FlowLevel.NONE,
    val symptoms: List<Symptom> = emptyList(),
    val moods: List<Mood> = emptyList(),
    val notes: String? = null,
    val isPeriodDay: Boolean = false,
    val cycleId: Long? = null
) {
    val dateFormatted: String
        get() = DateUtils.formatDate(date)

    val hasData: Boolean
        get() = flow != FlowLevel.NONE || symptoms.isNotEmpty() || moods.isNotEmpty() || (notes?.isNotBlank() == true)
}

data class Settings(
    val key: String = "user_settings",
    val averageCycleLength: Int = 28,
    val averagePeriodLength: Int = 5,
    val lastPeriodStart: Long? = null,
    val remindersEnabled: Boolean = true,
    val periodReminderDaysBefore: Int = 2,
    val ovulationReminderEnabled: Boolean = true,
    val fertileWindowReminderEnabled: Boolean = false,
    val themeMode: Int = 0,
    val language: String = "id",
    val dataExportEnabled: Boolean = true
)

data class CycleStatistics(
    val averageCycleLength: Double,
    val averagePeriodLength: Double,
    val shortestCycle: Int?,
    val longestCycle: Int?,
    val totalCycles: Int,
    val cycleLengthStdDev: Double
)

data class PredictionResult(
    val nextPeriodStart: Long,
    val nextPeriodEnd: Long,
    val ovulationDay: Long,
    val fertileWindowStart: Long,
    val fertileWindowEnd: Long,
    val pmsStart: Long,
    val daysUntilPeriod: Int,
    val daysUntilOvulation: Int,
    val currentCycleDay: Int
)

data class DayStatus(
    val date: Long,
    val isPeriodDay: Boolean = false,
    val isFertileDay: Boolean = false,
    val isOvulationDay: Boolean = false,
    val isPmsDay: Boolean = false,
    val flow: FlowLevel = FlowLevel.NONE,
    val hasLog: Boolean = false
)
