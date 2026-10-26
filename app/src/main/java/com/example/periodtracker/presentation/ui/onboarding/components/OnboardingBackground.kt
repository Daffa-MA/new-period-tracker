package com.example.periodtracker.presentation.ui.onboarding.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 * Animated background that subtly shifts color per onboarding step.
 * Creates warm, progressive visual feedback.
 */
@Composable
fun OnboardingBackground(
    modifier: Modifier = Modifier,
    currentStep: Int = 0
) {
    val backgroundColor = remember { Animatable(Color(0xFFFBF9F7)) }

    LaunchedEffect(currentStep) {
        val targetColor = when (currentStep) {
            0 -> Color(0xFFFBF9F7) // cream
            1 -> Color(0xFFFAF7F5) // cream +5% pink
            2 -> Color(0xFFF9F5F2) // cream +10% pink
            3 -> Color(0xFFF8F3EF) // cream +15% pink
            4 -> Color(0xFFF7F1EC) // cream +20% pink
            else -> Color(0xFFFBF9F7)
        }
        backgroundColor.animateTo(
            targetColor,
            animationSpec = tween(600, easing = EaseInOutCubic)
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor.value)
    )
}
