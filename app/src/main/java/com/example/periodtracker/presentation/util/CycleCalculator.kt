package com.example.periodtracker.presentation.util

import com.example.periodtracker.domain.model.Cycle
import com.example.periodtracker.domain.model.CycleStatistics
import com.example.periodtracker.domain.model.DayStatus
import com.example.periodtracker.domain.model.FlowLevel
import com.example.periodtracker.domain.model.PredictionResult
import com.example.periodtracker.domain.model.Settings
import kotlin.math.roundToInt
import kotlin.math.sqrt

object CycleCalculator {
    private const val DEFAULT_LUTEAL_PHASE = 14

    fun calculatePredictions(
        lastPeriodStart: Long?,
        settings: Settings,
        completedCycles: List<Cycle>
    ): PredictionResult? {
        val cycleLength = if (completedCycles.isNotEmpty()) {
            completedCycles.map { it.cycleLength ?: settings.averageCycleLength }.average().roundToInt()
        } else {
            settings.averageCycleLength
        }

        val periodLength = if (completedCycles.isNotEmpty()) {
            completedCycles.map { it.periodLength ?: settings.averagePeriodLength }.average().roundToInt()
        } else {
            settings.averagePeriodLength
        }

        val lastStart = lastPeriodStart ?: completedCycles.firstOrNull()?.startDate
            ?: return null

        val nextPeriodStart = addDays(lastStart, cycleLength)
        val nextPeriodEnd = addDays(nextPeriodStart, periodLength - 1)

        val ovulationDay = addDays(nextPeriodStart, -DEFAULT_LUTEAL_PHASE)
        val fertileWindowStart = addDays(ovulationDay, -5)
        val fertileWindowEnd = addDays(ovulationDay, 1)
        val pmsStart = addDays(nextPeriodStart, -7)

        val today = DateUtils.startOfDay(System.currentTimeMillis())
        val daysUntilPeriod = DateUtils.daysBetween(today, nextPeriodStart)
        val daysUntilOvulation = DateUtils.daysBetween(today, ovulationDay)
        val currentCycleDay = DateUtils.daysBetween(lastStart, today) + 1

        return PredictionResult(
            nextPeriodStart = nextPeriodStart,
            nextPeriodEnd = nextPeriodEnd,
            ovulationDay = ovulationDay,
            fertileWindowStart = fertileWindowStart,
            fertileWindowEnd = fertileWindowEnd,
            pmsStart = pmsStart,
            daysUntilPeriod = daysUntilPeriod,
            daysUntilOvulation = daysUntilOvulation,
            currentCycleDay = currentCycleDay
        )
    }

    fun calculateCycleStatistics(cycles: List<Cycle>): CycleStatistics {
        val completedCycles = cycles.filter { it.endDate != null }
        val cycleLengths = completedCycles.mapNotNull { it.cycleLength }
        val periodLengths = completedCycles.mapNotNull { it.periodLength }

        val avgCycleLength = if (cycleLengths.isNotEmpty()) cycleLengths.average() else 0.0
        val avgPeriodLength = if (periodLengths.isNotEmpty()) periodLengths.average() else 0.0
        val shortestCycle = cycleLengths.minOrNull()
        val longestCycle = cycleLengths.maxOrNull()
        val totalCycles = completedCycles.size

        val stdDev = if (cycleLengths.size > 1) {
            val mean = avgCycleLength
            val variance = cycleLengths.map { (it - mean) * (it - mean) }.average()
            sqrt(variance)
        } else 0.0

        return CycleStatistics(
            averageCycleLength = avgCycleLength,
            averagePeriodLength = avgPeriodLength,
            shortestCycle = shortestCycle,
            longestCycle = longestCycle,
            totalCycles = totalCycles,
            cycleLengthStdDev = stdDev
        )
    }

    fun getDayStatus(
        date: Long,
        prediction: PredictionResult?,
        logs: Map<Long, FlowLevel>
    ): DayStatus {
        val flow = logs[date] ?: FlowLevel.NONE
        val hasLog = logs.containsKey(date)

        return if (prediction == null) {
            DayStatus(date = date, flow = flow, hasLog = hasLog)
        } else {
            val isPeriodDay = date >= prediction.nextPeriodStart && date <= prediction.nextPeriodEnd
            val isFertileDay = date >= prediction.fertileWindowStart && date <= prediction.fertileWindowEnd
            val isOvulationDay = date == prediction.ovulationDay
            val isPmsDay = date >= prediction.pmsStart && date < prediction.nextPeriodStart

            DayStatus(
                date = date,
                isPeriodDay = isPeriodDay,
                isFertileDay = isFertileDay,
                isOvulationDay = isOvulationDay,
                isPmsDay = isPmsDay,
                flow = flow,
                hasLog = hasLog
            )
        }
    }

    fun getDayStatusesForMonth(
        year: Int,
        month: Int,
        prediction: PredictionResult?,
        logs: Map<Long, FlowLevel>
    ): List<DayStatus> {
        val daysInMonth = DateUtils.getDaysInMonth(year, month)
        val statuses = mutableListOf<DayStatus>()

        for (day in 1..daysInMonth) {
            val date = DateUtils.toTimestamp(java.time.LocalDate.of(year, month, day))
            statuses.add(getDayStatus(date, prediction, logs))
        }

        return statuses
    }

    private fun addDays(timestamp: Long, days: Int): Long {
        return DateUtils.addDays(timestamp, days)
    }
}
