package com.example.periodtracker.presentation.ui.onboarding.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import kotlinx.coroutines.launch

/**
 * Botanical elements (leaves/petals) that fade in progressively as user advances through steps.
 * Positioned at corners, non-intrusive.
 */
@Composable
fun BotanicalElements(
    modifier: Modifier = Modifier,
    stepProgress: Float = 0f  // 0.0 to 1.0
) {
    // Three staggered elements with individual alpha animations
    val elementAlphas = remember {
        List(3) { Animatable(0f) }
    }

    LaunchedEffect(stepProgress) {
        elementAlphas.forEachIndexed { index, alpha ->
            launch {
                kotlinx.coroutines.delay((index * 200).toLong())
                alpha.animateTo(
                    0.25f,
                    animationSpec = tween(1200, easing = EaseInOutCubic)
                )
            }
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val baseColor = Color(0xFFE8A8B0) // soft coral

        // Leaf at top-right
        drawLeaf(
            center = Offset(size.width * 0.85f, size.height * 0.1f),
            color = baseColor.copy(alpha = elementAlphas[0].value),
            rotation = 45f
        )

        // Petal at bottom-left
        drawPetal(
            center = Offset(size.width * 0.15f, size.height * 0.85f),
            color = baseColor.copy(alpha = elementAlphas[1].value),
            rotation = -30f
        )

        // Small leaf at top-left
        drawLeaf(
            center = Offset(size.width * 0.1f, size.height * 0.2f),
            color = baseColor.copy(alpha = elementAlphas[2].value),
            rotation = -60f,
            scale = 0.7f
        )
    }
}

private fun DrawScope.drawLeaf(
    center: Offset,
    color: Color,
    rotation: Float = 0f,
    scale: Float = 1f
) {
    rotate(rotation, pivot = center) {
        val leafPath = Path().apply {
            val width = 40f * scale
            val height = 60f * scale
            val left = center.x - width / 2
            val top = center.y - height / 2

            moveTo(center.x, top)
            cubicTo(
                left + width, top + height * 0.3f,
                left + width, top + height * 0.7f,
                center.x, top + height
            )
            cubicTo(
                left, top + height * 0.7f,
                left, top + height * 0.3f,
                center.x, top
            )
            close()
        }
        drawPath(leafPath, color = color)
    }
}

private fun DrawScope.drawPetal(
    center: Offset,
    color: Color,
    rotation: Float = 0f,
    scale: Float = 1f
) {
    rotate(rotation, pivot = center) {
        val petalPath = Path().apply {
            val width = 20f * scale
            val height = 30f * scale
            val left = center.x - width / 2
            val top = center.y - height / 2

            // Rounded drop shape
            moveTo(center.x, top)
            quadraticBezierTo(
                left + width, top + height * 0.4f,
                center.x, top + height
            )
            quadraticBezierTo(
                left, top + height * 0.4f,
                center.x, top
            )
            close()
        }
        drawPath(petalPath, color = color)
    }
}
