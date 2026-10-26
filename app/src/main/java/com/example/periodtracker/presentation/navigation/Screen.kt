package com.example.periodtracker.presentation.navigation

import com.example.periodtracker.presentation.ui.calendar.CalendarScreen
import com.example.periodtracker.presentation.ui.dailylog.DailyLogScreen
import com.example.periodtracker.presentation.ui.onboarding.OnboardingScreen
import com.example.periodtracker.presentation.ui.predictions.PredictionsScreen
import com.example.periodtracker.presentation.ui.settings.SettingsScreen
import com.example.periodtracker.presentation.ui.statistics.StatisticsScreen

sealed interface Screen {
    data object Calendar : Screen
    data object Predictions : Screen
    data object Statistics : Screen
    data object Settings : Screen
    data object Onboarding : Screen
    data class DailyLog(val date: Long) : Screen
}