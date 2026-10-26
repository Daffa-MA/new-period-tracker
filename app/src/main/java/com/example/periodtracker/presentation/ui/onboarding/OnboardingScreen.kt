package com.example.periodtracker.presentation.ui.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.periodtracker.presentation.ui.common.MinimalButton
import com.example.periodtracker.presentation.ui.common.MinimalCard
import com.example.periodtracker.presentation.ui.common.MinimalTypography
import com.example.periodtracker.presentation.util.DateUtils
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import com.example.periodtracker.presentation.ui.onboarding.components.BlobMascot
import com.example.periodtracker.presentation.ui.onboarding.components.BotanicalElements
import com.example.periodtracker.presentation.ui.onboarding.components.OnboardingBackground
import com.example.periodtracker.presentation.ui.onboarding.gestures.detectHorizontalSwipe
import com.example.periodtracker.ui.theme.PeriodTrackerTheme
import kotlin.math.roundToInt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel,
    onFinish: () -> Unit
) {
    val currentStep by viewModel.currentStep.collectAsState()
    val lastPeriodDate by viewModel.lastPeriodDate.collectAsState()
    val avgCycleLength by viewModel.averageCycleLength.collectAsState()
    val avgPeriodLength by viewModel.averagePeriodLength.collectAsState()
    val notificationsEnabled by viewModel.notificationsEnabled.collectAsState()
    val canProceed = viewModel.canProceed()
    val isLoading by viewModel.isLoading.collectAsState()
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()

    PeriodTrackerTheme(isOnboarding = true) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Background layer
            OnboardingBackground(currentStep = currentStep)

            // Botanical elements layer
            BotanicalElements(
                stepProgress = currentStep.toFloat() / viewModel.totalSteps
            )

            // Main content layer
            Scaffold(
                containerColor = Color.Transparent
            ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .detectHorizontalSwipe(
                        onSwipeLeft = {
                            if (currentStep < viewModel.totalSteps - 1 && canProceed) {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                viewModel.nextStep()
                            }
                        },
                        onSwipeRight = {
                            if (currentStep > 0) {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                viewModel.previousStep()
                            }
                        }
                    )
            ) {
            OnboardingProgressIndicator(
                currentStep = currentStep,
                totalSteps = viewModel.totalSteps
            )

            Spacer(modifier = Modifier.height(32.dp))

            AnimatedContent(
                targetState = currentStep,
                transitionSpec = {
                    if (targetState > initialState) {
                        slideInHorizontally(
                            initialOffsetX = { it },
                            animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
                        ) + fadeIn() togetherWith
                        slideOutHorizontally(
                            targetOffsetX = { -it },
                            animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
                        ) + fadeOut()
                    } else {
                        slideInHorizontally(
                            initialOffsetX = { -it },
                            animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
                        ) + fadeIn() togetherWith
                        slideOutHorizontally(
                            targetOffsetX = { it },
                            animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
                        ) + fadeOut()
                    }
                },
                label = "onboarding_step"
            ) { step ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    when (step) {
                    0 -> WelcomeStep()
                    1 -> LastPeriodStep(
                        selectedDate = lastPeriodDate,
                        onDateSelected = { viewModel.onLastPeriodDateSelected(it) },
                        canProceed = canProceed
                    )
                    2 -> CycleLengthStep(
                        cycleLength = avgCycleLength,
                        onChanged = { viewModel.onAverageCycleLengthChanged(it) },
                        canProceed = canProceed
                    )
                    3 -> PeriodLengthStep(
                        periodLength = avgPeriodLength,
                        onChanged = { viewModel.onAveragePeriodLengthChanged(it) },
                        canProceed = canProceed
                    )
                    4 -> NotificationsStep(
                        enabled = notificationsEnabled,
                        onChanged = { viewModel.onNotificationsEnabledChanged(it) }
                    )
                    else -> WelcomeStep()
                }
            }
        }

            OnboardingNavigation(
                currentStep = currentStep,
                totalSteps = viewModel.totalSteps,
                canProceed = canProceed,
                isLoading = isLoading,
                onNext = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    viewModel.nextStep()
                },
                onPrevious = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    viewModel.previousStep()
                },
                onFinish = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    scope.launch {
                        val success = viewModel.completeOnboarding()
                        if (success) onFinish()
                    }
                }
            )
        }
    }
        }
    }
}

@Composable
fun OnboardingProgressIndicator(currentStep: Int, totalSteps: Int) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        (0 until totalSteps).forEach { index ->
            val isActive = index <= currentStep
            val isCurrent = index == currentStep

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.Center
            ) {
                if (index > 0) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.5f)
                            .height(2.dp)
                        .background(if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                    )
                }

                Box(
                    modifier = Modifier
                        .size(if (isCurrent) 16.dp else 12.dp)
                        .background(
                            color = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                            shape = CircleShape
                        )
                )

                if (index < totalSteps - 1) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.5f)
                            .height(2.dp)
                    .background(if (index < currentStep) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                    )
                }
            }
        }
    }
}

@Composable
fun WelcomeStep() {
    Column(
        modifier = Modifier.padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        BlobMascot(
            currentStep = 0,
            size = 100.dp,
            modifier = Modifier
        )

        Spacer(modifier = Modifier.height(32.dp))

        AnimatedVisibility(
            visible = true,
            enter = fadeIn(animationSpec = tween(600)) + slideInVertically(
                initialOffsetY = { it / 4 },
                animationSpec = tween(600)
            )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Selamat datang di Period Tracker",
                    style = MaterialTheme.typography.displayMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Lacak siklus, prediksi periode, dan pahami tubuh kamu lebih baik. Semua data aman di perangkatmu.",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LastPeriodStep(
    selectedDate: Long?,
    onDateSelected: (Long) -> Unit,
    canProceed: Boolean
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate ?: System.currentTimeMillis(),
        initialDisplayMode = DisplayMode.Picker
    )

    Column(
        modifier = Modifier.padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        BlobMascot(currentStep = 1, size = 80.dp)

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Kapan periode terakhir kamu dimulai?",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Ini membantu kami memprediksi siklus kamu dengan akurat",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        MinimalCard(padding = 24.dp, modifier = Modifier.fillMaxWidth()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (selectedDate != null) DateUtils.formatDate(selectedDate) else "Pilih tanggal",
                    style = if (selectedDate != null) MaterialTheme.typography.headlineMedium
                           else MaterialTheme.typography.bodyLarge.copy(
                               color = MaterialTheme.colorScheme.onSurfaceVariant
                           ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                MinimalButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        showDatePicker = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (selectedDate != null) "Ubah Tanggal" else "Pilih Tanggal",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { onDateSelected(it) }
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Batal")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
fun CycleLengthStep(
    cycleLength: Int,
    onChanged: (Int) -> Unit,
    canProceed: Boolean
) {
    Column(
        modifier = Modifier.padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        BlobMascot(currentStep = 2, size = 80.dp)

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Siklus kamu berapa hari biasanya?",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Dari hari pertama periode ke hari pertama periode berikutnya",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        MinimalCard(padding = 24.dp, modifier = Modifier.fillMaxWidth()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "$cycleLength hari",
                    style = MaterialTheme.typography.displayLarge.copy(
                        color = MaterialTheme.colorScheme.primary
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                Slider(
                    value = cycleLength.toFloat(),
                    onValueChange = { onChanged(it.roundToInt()) },
                    valueRange = 21f..45f,
                    steps = 24,
                    modifier = Modifier.fillMaxWidth(),
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary,
                        activeTrackColor = MaterialTheme.colorScheme.primary,
                        inactiveTrackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "21 hari", style = MaterialTheme.typography.labelSmall)
                    Text(text = "45 hari", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}

@Composable
fun PeriodLengthStep(
    periodLength: Int,
    onChanged: (Int) -> Unit,
    canProceed: Boolean
) {
    Column(
        modifier = Modifier.padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        BlobMascot(currentStep = 3, size = 80.dp)

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Periode kamu biasa berapa hari?",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Jumlah hari pendarahan",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        MinimalCard(padding = 24.dp, modifier = Modifier.fillMaxWidth()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "$periodLength hari",
                    style = MaterialTheme.typography.displayLarge.copy(
                        color = MaterialTheme.colorScheme.primary
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                Slider(
                    value = periodLength.toFloat(),
                    onValueChange = { onChanged(it.roundToInt()) },
                    valueRange = 1f..10f,
                    steps = 9,
                    modifier = Modifier.fillMaxWidth(),
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary,
                        activeTrackColor = MaterialTheme.colorScheme.primary,
                        inactiveTrackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "1 hari", style = MaterialTheme.typography.labelSmall)
                    Text(text = "10 hari", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}

@Composable
fun NotificationsStep(
    enabled: Boolean,
    onChanged: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        BlobMascot(currentStep = 4, size = 80.dp)

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Aktifkan pengingat?",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Dapat notifikasi sebelum periode, saat ovulasi, dan masa subur",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        MinimalCard(padding = 24.dp, modifier = Modifier.fillMaxWidth()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Switch(
                    checked = enabled,
                    onCheckedChange = onChanged,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.primary,
                        checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                        uncheckedThumbColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                            alpha = 0.6f
                        ),
                        uncheckedTrackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = if (enabled) "Pengingat aktif" else "Pengingat nonaktif",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = if (enabled) MaterialTheme.colorScheme.primary
                               else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }
    }
}

@Composable
fun OnboardingNavigation(
    currentStep: Int,
    totalSteps: Int,
    canProceed: Boolean,
    isLoading: Boolean,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onFinish: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (currentStep > 0) {
            TextButton(onClick = onPrevious) {
                Text(text = "Back", style = MinimalTypography.labelLarge.copy(color = PinkOnSurfaceVariant))
            }
        } else {
            Spacer(modifier = Modifier.size(1.dp))
        }

        if (currentStep == totalSteps - 1) {
            MinimalButton(
                onClick = onFinish,
                enabled = canProceed && !isLoading,
                modifier = Modifier.weight(1f).padding(start = 16.dp)
            ) {
                if (isLoading) {
                    androidx.compose.material3.CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = PinkOnPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(text = "Mulai", style = MaterialTheme.typography.labelLarge)
                }
            }
        } else {
            MinimalButton(
                onClick = onNext,
                enabled = canProceed && !isLoading,
                modifier = Modifier.weight(1f).padding(start = 16.dp)
            ) {
                Text(text = "Lanjut", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}
