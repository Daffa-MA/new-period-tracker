package com.example.periodtracker.presentation.ui.onboarding.gestures

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import kotlin.math.abs

/**
 * Detects horizontal swipe gestures for navigation.
 * Threshold: 50dp horizontal movement, with vertical suppression to avoid false triggers.
 */
fun Modifier.detectHorizontalSwipe(
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit,
    thresholdPx: Float = 150f  // ~50dp at typical density
) = pointerInput(Unit) {
    var totalHorizontalDrag = 0f
    var totalVerticalDrag = 0f

    detectHorizontalDragGestures(
        onDragEnd = {
            // Only trigger if horizontal movement dominates
            if (abs(totalHorizontalDrag) > abs(totalVerticalDrag) &&
                abs(totalHorizontalDrag) > thresholdPx) {
                when {
                    totalHorizontalDrag > 0 -> onSwipeRight()
                    totalHorizontalDrag < 0 -> onSwipeLeft()
                }
            }
            totalHorizontalDrag = 0f
            totalVerticalDrag = 0f
        },
        onDragCancel = {
            totalHorizontalDrag = 0f
            totalVerticalDrag = 0f
        },
        onHorizontalDrag = { change, dragAmount ->
            totalHorizontalDrag += dragAmount
            // Track vertical movement for suppression
            totalVerticalDrag += change.position.y - change.previousPosition.y
            change.consume()
        }
    )
}
