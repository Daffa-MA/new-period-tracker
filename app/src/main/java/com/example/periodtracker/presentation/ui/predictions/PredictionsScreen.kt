package com.example.periodtracker.presentation.ui.predictions

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.periodtracker.domain.model.PredictionResult
import com.example.periodtracker.presentation.navigation.Screen
import com.example.periodtracker.presentation.ui.common.MinimalButton
import com.example.periodtracker.presentation.ui.common.MinimalCard
import com.example.periodtracker.presentation.ui.common.MinimalDivider
import com.example.periodtracker.presentation.ui.common.MinimalTypography
import com.example.periodtracker.presentation.ui.common.SectionHeader
import com.example.periodtracker.presentation.ui.common.StatusTag
import com.example.periodtracker.presentation.util.DateUtils
import com.example.periodtracker.ui.theme.PeriodColor
import com.example.periodtracker.ui.theme.PinkOnSurface
import com.example.periodtracker.ui.theme.PinkOnSurfaceVariant
import com.example.periodtracker.ui.theme.PinkOutline
import com.example.periodtracker.ui.theme.PinkPrimary
import com.example.periodtracker.ui.theme.PinkPrimaryContainer
import com.example.periodtracker.ui.theme.PinkSurface
import com.example.periodtracker.ui.theme.PinkSurfaceContainer
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PredictionsScreen(
    viewModel: PredictionsViewModel,
    onNavigateBack: () -> Unit
) {
    val prediction by viewModel.prediction.collectAsState()
    val statistics by viewModel.statistics.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = { Text(text = "Predictions", style = MinimalTypography.headlineMedium) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = PinkOnSurface)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PinkSurface,
                    titleContentColor = PinkOnSurface,
                    navigationIconContentColor = PinkOnSurface
                )
            )
        },
        containerColor = PinkSurface
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            prediction?.let { pred ->
                MinimalCard(padding = 16.dp) {
                    CountdownCard(prediction = pred)
                }

                Spacer(modifier = Modifier.height(24.dp))

                MinimalCard(padding = 16.dp) {
                    KeyDatesCard(prediction = pred)
                }

                Spacer(modifier = Modifier.height(24.dp))

                MinimalCard(padding = 16.dp) {
                    CycleInfoCard(statistics = statistics)
                }
            } ?: MinimalCard(padding = 32.dp) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CalendarToday,
                            contentDescription = null,
                            tint = PinkOutline.copy(alpha = 0.4f),
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No Data Yet",
                            style = MinimalTypography.headlineMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Log your first period to see predictions",
                            style = MinimalTypography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        MinimalButton(onClick = { /* navigate to log */ }) {
                            Text(text = "Log Period", style = MinimalTypography.labelLarge)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CountdownCard(prediction: PredictionResult) {
    val daysUntilPeriod = prediction.daysUntilPeriod
    val daysUntilOvulation = prediction.daysUntilOvulation

    Column {
        SectionHeader(title = "Countdown")
        MinimalDivider()
        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            CountdownItem(
                label = "Next Period",
                value = if (daysUntilPeriod <= 0) "Due" else "$daysUntilPeriod days",
                date = DateUtils.formatShortDate(prediction.nextPeriodStart),
                color = PeriodColor,
                icon = Icons.Filled.Event
            )

            VerticalDivider()

            CountdownItem(
                label = "Ovulation",
                value = if (daysUntilOvulation <= 0) "Today" else "$daysUntilOvulation days",
                date = DateUtils.formatShortDate(prediction.ovulationDay),
                color = Color(0xFFFFB74D),
                icon = Icons.Filled.Favorite
            )
        }
    }
}

@Composable
fun RowScope.CountdownItem(
    label: String,
    value: String,
    date: String,
    color: Color,
    icon: ImageVector
) {
    Column(
        modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(28.dp))
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = value, style = MinimalTypography.displayMedium.copy(color = color))
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = date, style = MinimalTypography.labelSmall)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, style = MinimalTypography.labelSmall)
    }
}

@Composable
fun KeyDatesCard(prediction: PredictionResult) {
    Column {
        SectionHeader(title = "Key Dates")
        MinimalDivider()
        Spacer(modifier = Modifier.height(16.dp))

        KeyDateRow(label = "Period Starts", date = DateUtils.formatDate(prediction.nextPeriodStart), status = "Period")
        MinimalDivider()
        KeyDateRow(label = "Period Ends", date = DateUtils.formatDate(prediction.nextPeriodEnd), status = "Period")
        MinimalDivider()
        KeyDateRow(
            label = "Fertile Window",
            date = "${DateUtils.formatShortDate(prediction.fertileWindowStart)} – ${DateUtils.formatShortDate(prediction.fertileWindowEnd)}",
            status = "Fertile"
        )
        MinimalDivider()
        KeyDateRow(label = "Ovulation Day", date = DateUtils.formatDate(prediction.ovulationDay), status = "Ovulation")
        MinimalDivider()
        KeyDateRow(
            label = "PMS Window",
            date = "${DateUtils.formatShortDate(prediction.pmsStart)} – ${DateUtils.formatShortDate(prediction.nextPeriodStart)}",
            status = "PMS"
        )
    }
}

@Composable
fun KeyDateRow(label: String, date: String, status: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = label, style = MinimalTypography.bodyMedium)
            Text(text = date, style = MinimalTypography.bodyLarge)
        }
        Box(
            modifier = Modifier
                .background(
                    color = when (status) {
                        "Period" -> PeriodColor.copy(alpha = 0.2f)
                        "Fertile" -> Color(0xFF81C784).copy(alpha = 0.2f)
                        "Ovulation" -> Color(0xFFFFB74D).copy(alpha = 0.2f)
                        "PMS" -> Color(0xFFFFAB40).copy(alpha = 0.2f)
                        else -> PinkOutline.copy(alpha = 0.1f)
                    },
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = status,
                style = MinimalTypography.labelSmall.copy(
                    color = when (status) {
                        "Period" -> PeriodColor
                        "Fertile" -> Color(0xFF346538)
                        "Ovulation" -> Color(0xFF956400)
                        "PMS" -> Color(0xFF9F2F2D)
                        else -> PinkOnSurface
                    }
                )
            )
        }
    }
}

@Composable
fun CycleInfoCard(statistics: com.example.periodtracker.domain.model.CycleStatistics?) {
    if (statistics == null) return

    Column {
        SectionHeader(title = "Cycle Averages")
        MinimalDivider()
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatItem(label = "Cycle Length", value = "${statistics.averageCycleLength.roundToInt()} days", subtitle = "Average")
            VerticalDivider()
            StatItem(label = "Period Length", value = "${statistics.averagePeriodLength.roundToInt()} days", subtitle = "Average")
            VerticalDivider()
            StatItem(label = "Variability", value = "±${statistics.cycleLengthStdDev.roundToInt()} days", subtitle = "Std Dev")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (statistics.totalCycles > 0) {
            MinimalDivider()
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Completed Cycles", style = MinimalTypography.bodyMedium)
                Text(text = "${statistics.totalCycles}", style = MinimalTypography.headlineMedium)
            }
        }
    }
}

@Composable
fun RowScope.StatItem(label: String, value: String, subtitle: String) {
    Column(
        modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = value, style = MinimalTypography.headlineLarge)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, style = MinimalTypography.bodyMedium)
        Text(text = subtitle, style = MinimalTypography.labelSmall)
    }
}

@Composable
private fun VerticalDivider() {
    Spacer(modifier = Modifier.height(60.dp).width(1.dp))
}
