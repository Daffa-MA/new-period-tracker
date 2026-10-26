package com.example.periodtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.periodtracker.di.AppContainerProvider
import com.example.periodtracker.presentation.navigation.AppNavHost
import com.example.periodtracker.presentation.navigation.RememberAppNavHost
import com.example.periodtracker.presentation.ui.calendar.CalendarScreen
import com.example.periodtracker.presentation.ui.dailylog.DailyLogScreen
import com.example.periodtracker.presentation.ui.onboarding.OnboardingScreen
import com.example.periodtracker.presentation.ui.predictions.PredictionsScreen
import com.example.periodtracker.presentation.ui.settings.SettingsScreen
import com.example.periodtracker.presentation.ui.statistics.StatisticsScreen
import com.example.periodtracker.ui.theme.PeriodTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        AppContainerProvider.initialize(this)
        setContent {
            PeriodTrackerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavHost(
                        navController = RememberAppNavHost("onboarding"),
                        startDestination = "onboarding"
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PeriodTrackerTheme {
        Text("Period Tracker")
    }
}