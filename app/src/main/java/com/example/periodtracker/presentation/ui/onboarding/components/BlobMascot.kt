package com.example.periodtracker.presentation.ui.onboarding.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Playful blob mascot that animates with breathing and reacts to step changes.
 * Purely decorative (aria-hidden).
 */
@Composable
fun BlobMascot(
    modifier: Modifier = Modifier,
    currentStep: Int = 0,
    size: Dp = 100.dp
) {
    // Breath animation: continuous scale pulse
    val infiniteTransition = rememberInfiniteTransition(label = "blob_breath")
    val breathScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breath_scale"
    )

    // Color tint per step
    val colorTint = remember { Animatable(Color(0xFFE8A8B0)) }
    LaunchedEffect(currentStep) {
        val targetColor = when (currentStep) {
            0, 4 -> Color(0xFFE8A8B0) // primary pink
            1 -> Color(0xFFD4A5A5)     // dusty rose
            2 -> Color(0xFFC89BA8)     // muted mauve
            3 -> Color(0xFFFBF9F7)     // cream
            else -> Color(0xFFE8A8B0)
        }
        colorTint.animateTo(
            targetColor,
            animationSpec = tween(600, easing = EaseInOutCubic)
        )
    }

    // React to step change: vertical bounce
    val verticalBounce = remember { Animatable(0f) }
    LaunchedEffect(currentStep) {
        verticalBounce.animateTo(-12f, animationSpec = spring(stiffness = Spring.StiffnessMedium))
        verticalBounce.animateTo(0f, animationSpec = spring(stiffness = Spring.StiffnessMedium))
    }

    Box(
        modifier = modifier
            .size(size)
            .graphicsLayer(
                scaleX = breathScale,
                scaleY = breathScale
            )
            .offset(y = verticalBounce.value.dp)
            .background(
                color = colorTint.value,
                shape = RoundedCornerShape(50) // bulging rounded corners
            ),
        contentAlignment = Alignment.Center
    ) {
        // Empty blob (decorative only)
    }
}
