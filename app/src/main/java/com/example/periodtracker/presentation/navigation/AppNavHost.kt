package com.example.periodtracker.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.periodtracker.di.AppContainerProvider
import com.example.periodtracker.presentation.ui.calendar.CalendarScreen
import com.example.periodtracker.presentation.ui.calendar.CalendarViewModel
import com.example.periodtracker.presentation.ui.dailylog.DailyLogScreen
import com.example.periodtracker.presentation.ui.dailylog.DailyLogViewModel
import com.example.periodtracker.presentation.ui.onboarding.OnboardingScreen
import com.example.periodtracker.presentation.ui.onboarding.OnboardingViewModel
import com.example.periodtracker.presentation.ui.predictions.PredictionsScreen
import com.example.periodtracker.presentation.ui.predictions.PredictionsViewModel
import com.example.periodtracker.presentation.ui.settings.SettingsScreen
import com.example.periodtracker.presentation.ui.settings.SettingsViewModel
import com.example.periodtracker.presentation.ui.statistics.StatisticsScreen
import com.example.periodtracker.presentation.ui.statistics.StatisticsViewModel

@Composable
fun AppNavHost(navController: NavHostController, startDestination: String = "calendar") {
    val container = remember { AppContainerProvider.get() }
    val calendarViewModel = remember { container.createCalendarViewModel() }
    val dailyLogViewModel = remember { container.createDailyLogViewModel() }
    val predictionsViewModel = remember { container.createPredictionsViewModel() }
    val statisticsViewModel = remember { container.createStatisticsViewModel() }
    val settingsViewModel = remember { container.createSettingsViewModel() }
    val onboardingViewModel = remember { container.createOnboardingViewModel() }
    
    NavHost(navController, startDestination) {
        composable("calendar") {
            CalendarScreen(
                viewModel = calendarViewModel,
                onNavigateToDailyLog = { date ->
                    navController.navigate("daily_log/$date")
                },
                onNavigateToPredictions = {
                    navController.navigate("predictions")
                },
                onNavigateToStatistics = {
                    navController.navigate("statistics")
                },
                onNavigateToSettings = {
                    navController.navigate("settings")
                }
            )
        }

        composable(
            route = "daily_log/{date}",
            arguments = listOf(androidx.navigation.navArgument("date") { type = androidx.navigation.NavType.LongType })
        ) { backStackEntry ->
            val date = backStackEntry.arguments?.getLong("date") ?: System.currentTimeMillis()
            DailyLogScreen(
                viewModel = dailyLogViewModel,
                initialDate = date,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("predictions") {
            PredictionsScreen(
                viewModel = predictionsViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("statistics") {
            StatisticsScreen(
                viewModel = statisticsViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("settings") {
            SettingsScreen(
                viewModel = settingsViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("onboarding") {
            OnboardingScreen(
                viewModel = onboardingViewModel,
                onFinish = { navController.navigate("calendar") { popUpTo("onboarding") { inclusive = true } } }
            )
        }
    }
}

@Composable
fun RememberAppNavHost(startDestination: String = "calendar"): NavHostController {
    val navController = rememberNavController()
    AppNavHost(navController, startDestination)
    return navController
}