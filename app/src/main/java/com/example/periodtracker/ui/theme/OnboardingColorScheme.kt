package com.example.periodtracker.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Onboarding Warm Pink Palette
val OnboardingPrimary = Color(0xFFE8A8B0)      // soft coral
val OnboardingSecondary = Color(0xFFD4A5A5)    // dusty rose
val OnboardingTertiary = Color(0xFFC89BA8)     // muted mauve
val OnboardingBackground = Color(0xFFFBF9F7)   // cream
val OnboardingSurface = Color(0xFFFBF9F7)      // cream
val OnboardingOnBackground = Color(0xFF3D2E2B) // dark text
val OnboardingOnSurface = Color(0xFF3D2E2B)    // dark text

// Light variant (primary use for onboarding)
val OnboardingLightColorScheme = lightColorScheme(
    primary = OnboardingPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFEF3F0),
    onPrimaryContainer = OnboardingPrimary,
    secondary = OnboardingSecondary,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFF5F1F0),
    onSecondaryContainer = OnboardingSecondary,
    tertiary = OnboardingTertiary,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFF3EFF0),
    onTertiaryContainer = OnboardingTertiary,
    background = OnboardingBackground,
    onBackground = OnboardingOnBackground,
    surface = OnboardingSurface,
    onSurface = OnboardingOnSurface,
    surfaceVariant = Color(0xFFF5F3F2),
    onSurfaceVariant = Color(0xFF7A6E69),
    outline = Color(0xFFCCC5C0),
    outlineVariant = Color(0xFFE7E1DC),
)

// Dark variant (inverted, but onboarding uses light for now)
val OnboardingDarkColorScheme = darkColorScheme(
    primary = Color(0xFFE8A8B0),
    onPrimary = Color(0xFF3D2E2B),
    primaryContainer = Color(0xFF6B5254),
    onPrimaryContainer = Color(0xFFFEF3F0),
    secondary = Color(0xFFD4A5A5),
    onSecondary = Color(0xFF3D2E2B),
    secondaryContainer = Color(0xFF6B5254),
    onSecondaryContainer = Color(0xFFF5F1F0),
    tertiary = Color(0xFFC89BA8),
    onTertiary = Color(0xFF3D2E2B),
    tertiaryContainer = Color(0xFF6B5254),
    onTertiaryContainer = Color(0xFFF3EFF0),
    background = Color(0xFF1C1917),
    onBackground = Color(0xFFFBF9F7),
    surface = Color(0xFF292524),
    onSurface = Color(0xFFFBF9F7),
    surfaceVariant = Color(0xFF44403C),
    onSurfaceVariant = Color(0xFFA8A29E),
    outline = Color(0xFF9F9892),
    outlineVariant = Color(0xFF44403C),
)
