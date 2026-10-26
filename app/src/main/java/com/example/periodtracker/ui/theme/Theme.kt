package com.example.periodtracker.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = TerracottaClayDark,
    onPrimary = OnTerracottaDark,
    primaryContainer = TerracottaLightDark,
    onPrimaryContainer = TerracottaClayDark,
    secondary = FertileSageDark,
    onSecondary = CharcoalInkDark,
    secondaryContainer = FertileSageLightDark,
    onSecondaryContainer = FertileSageDark,
    tertiary = OvulationAmberDark,
    onTertiary = CharcoalInkDark,
    tertiaryContainer = OvulationAmberLightDark,
    onTertiaryContainer = OvulationAmberDark,
    surface = PureSurfaceDark,
    onSurface = CharcoalInkDark,
    surfaceVariant = CanvasWarmDark,
    onSurfaceVariant = MutedSlateDark,
    surfaceContainer = PureSurfaceDark,
    surfaceContainerHigh = WhisperBorderDark,
    outline = WhisperBorderDark,
    outlineVariant = WhisperBorderDark,
    background = CanvasWarmDark,
    onBackground = CharcoalInkDark,
)

private val LightColorScheme = lightColorScheme(
    primary = TerracottaClay,
    onPrimary = OnTerracotta,
    primaryContainer = TerracottaLight,
    onPrimaryContainer = TerracottaClay,
    secondary = FertileSage,
    onSecondary = PureSurface,
    secondaryContainer = FertileSageLight,
    onSecondaryContainer = FertileSage,
    tertiary = OvulationAmber,
    onTertiary = CharcoalInk,
    tertiaryContainer = OvulationAmberLight,
    onTertiaryContainer = OvulationAmber,
    surface = PureSurface,
    onSurface = CharcoalInk,
    surfaceVariant = CanvasWarm,
    onSurfaceVariant = MutedSlate,
    surfaceContainer = PureSurface,
    surfaceContainerHigh = WhisperBorder,
    outline = WhisperBorder,
    outlineVariant = WhisperBorder,
    background = CanvasWarm,
    onBackground = CharcoalInk,
)

@Composable
fun PeriodTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    isOnboarding: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        isOnboarding -> OnboardingLightColorScheme
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val typography = if (isOnboarding) OnboardingTypography else Typography

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content
    )
}
