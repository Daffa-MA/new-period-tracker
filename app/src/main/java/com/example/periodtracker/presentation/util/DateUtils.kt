package com.example.periodtracker.presentation.util

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Locale

object DateUtils {
    private val locale = Locale.getDefault()
    private val dateFormat = SimpleDateFormat("dd MMM yyyy", locale)
    private val shortDateFormat = SimpleDateFormat("dd MMM", locale)
    private val dayFormat = SimpleDateFormat("d", locale)
    private val monthYearFormat = SimpleDateFormat("MMMM yyyy", locale)
    private val weekdayFormat = SimpleDateFormat("EEE", locale)

    fun formatDate(timestamp: Long): String {
        return dateFormat.format(Date(timestamp))
    }

    fun formatShortDate(timestamp: Long): String {
        return shortDateFormat.format(Date(timestamp))
    }

    fun formatDay(timestamp: Long): String {
        return dayFormat.format(Date(timestamp))
    }

    fun formatMonthYear(timestamp: Long): String {
        return monthYearFormat.format(Date(timestamp))
    }

    fun formatWeekday(timestamp: Long): String {
        return weekdayFormat.format(Date(timestamp))
    }

    fun toLocalDate(timestamp: Long): LocalDate {
        return Date(timestamp).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    }

    fun toTimestamp(localDate: LocalDate): Long {
        return localDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    fun startOfDay(timestamp: Long): Long {
        return toLocalDate(timestamp).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    fun endOfDay(timestamp: Long): Long {
        return toLocalDate(timestamp).atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    fun addDays(timestamp: Long, days: Int): Long {
        return toLocalDate(timestamp).plusDays(days.toLong()).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    fun daysBetween(start: Long, end: Long): Int {
        return ChronoUnit.DAYS.between(toLocalDate(start), toLocalDate(end)).toInt()
    }

    fun isSameDay(date1: Long, date2: Long): Boolean {
        return toLocalDate(date1) == toLocalDate(date2)
    }

    fun isToday(timestamp: Long): Boolean {
        return isSameDay(timestamp, System.currentTimeMillis())
    }

    fun isPast(timestamp: Long): Boolean {
        return toLocalDate(timestamp).isBefore(LocalDate.now())
    }

    fun isFuture(timestamp: Long): Boolean {
        return toLocalDate(timestamp).isAfter(LocalDate.now())
    }

    fun getDaysInMonth(year: Int, month: Int): Int {
        return LocalDate.of(year, month, 1).lengthOfMonth()
    }

    fun getFirstDayOfMonth(year: Int, month: Int): Int {
        return LocalDate.of(year, month, 1).dayOfWeek.value
    }
}