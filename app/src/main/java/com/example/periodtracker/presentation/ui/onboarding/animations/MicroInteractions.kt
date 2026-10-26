package com.example.periodtracker.presentation.ui.onboarding.animations

import androidx.compose.animation.core.*
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

/**
 * Reusable animation specs for micro-interactions throughout onboarding.
 */

// Animation specs
val glowAnimationSpec = tween<Float>(
    durationMillis = 500,
    easing = EaseInOutCubic
)

val burstAnimationSpec = tween<Float>(
    durationMillis = 1200,
    easing = FastOutSlowInEasing
)

val pulseAnimationSpec = spring<Float>(
    dampingRatio = Spring.DampingRatioHighBouncy,
    stiffness = Spring.StiffnessMedium
)

val slideAnimationSpec = spring<Float>(
    dampingRatio = 0.8f,
    stiffness = Spring.StiffnessMediumLow
)

/**
 * Animates input glow effect (border/shadow alpha).
 * Returns alpha value 0.0 to 0.6 when active.
 */
@Composable
fun animateInputGlow(isActive: Boolean): Float {
    val alpha = remember { Animatable(0f) }
    LaunchedEffect(isActive) {
        alpha.animateTo(
            if (isActive) 0.6f else 0f,
            animationSpec = glowAnimationSpec
        )
    }
    return alpha.value
}

/**
 * Animates petal burst effect (radial scatter).
 * Returns list of petal positions radiating from center.
 */
@Composable
fun animatePetalBurst(
    burstTriggered: Boolean,
    petalCount: Int = 6,
    maxRadius: Dp = 80.dp
): List<Offset> {
    val progress = remember { Animatable(0f) }

    LaunchedEffect(burstTriggered) {
        if (burstTriggered) {
            progress.snapTo(0f)
            progress.animateTo(1f, animationSpec = burstAnimationSpec)
        }
    }

    return remember(progress.value) {
        val angleStep = 360f / petalCount
        List(petalCount) { index ->
            val angle = Math.toRadians((angleStep * index).toDouble())
            val distance = progress.value * maxRadius.value
            Offset(
                x = (cos(angle) * distance).toFloat(),
                y = (sin(angle) * distance).toFloat()
            )
        }
    }
}

/**
 * Animates validation pulse (scale feedback).
 * Returns scale value for soft pulse on invalid input.
 */
@Composable
fun animateValidationPulse(isValidating: Boolean): Float {
    val scale = remember { Animatable(1f) }
    LaunchedEffect(isValidating) {
        if (isValidating) {
            scale.animateTo(0.98f, animationSpec = pulseAnimationSpec)
            scale.animateTo(1f, animationSpec = pulseAnimationSpec)
        }
    }
    return scale.value
}
