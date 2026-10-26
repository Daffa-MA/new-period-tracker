package com.example.periodtracker.presentation.ui.calendar

import androidx.compose.foundation.border
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.InsertChart
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.periodtracker.domain.model.DayStatus
import com.example.periodtracker.domain.model.FlowLevel
import com.example.periodtracker.presentation.ui.common.MinimalCard
import com.example.periodtracker.presentation.ui.common.MinimalDivider
import com.example.periodtracker.presentation.ui.common.MinimalTypography
import com.example.periodtracker.presentation.ui.common.SectionHeader
import com.example.periodtracker.presentation.util.DateUtils
import com.example.periodtracker.ui.theme.PeriodColor
import com.example.periodtracker.ui.theme.PinkOnSurface
import com.example.periodtracker.ui.theme.PinkOnSurfaceVariant
import com.example.periodtracker.ui.theme.PinkOutline
import com.example.periodtracker.ui.theme.PinkPrimary
import com.example.periodtracker.ui.theme.PinkPrimaryContainer
import com.example.periodtracker.ui.theme.PinkSurface
import com.example.periodtracker.ui.theme.PinkSurfaceContainer
import com.example.periodtracker.presentation.ui.calendar.CalendarViewModel

@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel,
    onNavigateToDailyLog: (Long) -> Unit,
    onNavigateToPredictions: () -> Unit,
    onNavigateToStatistics: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val currentMonth by viewModel.currentMonth.collectAsState()
    val dayStatuses by viewModel.dayStatuses.collectAsState()
    val prediction by viewModel.prediction.collectAsState()

    Scaffold(
        topBar = {
            CalendarTopAppBar(
                currentMonth = currentMonth,
                onMonthPrevious = { viewModel.onMonthChanged(currentMonth.minusMonths(1).year, currentMonth.minusMonths(1).monthValue) },
                onMonthNext = { viewModel.onMonthChanged(currentMonth.plusMonths(1).year, currentMonth.plusMonths(1).monthValue) },
                onNavigateToPredictions = onNavigateToPredictions,
                onNavigateToStatistics = onNavigateToStatistics,
                onNavigateToSettings = onNavigateToSettings
            )
        },
        containerColor = PinkSurface
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            MinimalCard(padding = 16.dp) {
                CalendarGrid(
                    currentMonth = currentMonth,
                    dayStatuses = dayStatuses,
                    onDayClick = { date ->
                        viewModel.onDayClick(date)
                        onNavigateToDailyLog(date)
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            MinimalCard(padding = 16.dp) {
                LegendRow()
            }

            Spacer(modifier = Modifier.height(24.dp))

            prediction?.let { pred ->
                MinimalCard(padding = 16.dp) {
                    QuickStatsCard(prediction = pred)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarTopAppBar(
    currentMonth: java.time.LocalDate,
    onMonthPrevious: () -> Unit,
    onMonthNext: () -> Unit,
    onNavigateToPredictions: () -> Unit,
    onNavigateToStatistics: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        title = {
            Text(
                text = DateUtils.formatMonthYear(DateUtils.toTimestamp(currentMonth)),
                style = MinimalTypography.headlineMedium
            )
        },
        navigationIcon = {
            IconButton(onClick = onMonthPrevious) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous month", tint = PinkOnSurface)
            }
        },
        actions = {
            IconButton(onClick = onMonthNext) {
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next month", tint = PinkOnSurface)
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = onNavigateToPredictions) {
                Icon(Icons.Filled.DateRange, contentDescription = "Predictions", tint = PinkOnSurface)
            }
            Spacer(modifier = Modifier.width(4.dp))
            IconButton(onClick = onNavigateToStatistics) {
                Icon(Icons.Filled.InsertChart, contentDescription = "Statistics", tint = PinkOnSurface)
            }
            Spacer(modifier = Modifier.width(4.dp))
            IconButton(onClick = onNavigateToSettings) {
                Icon(Icons.Filled.Settings, contentDescription = "Settings", tint = PinkOnSurface)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = PinkSurface,
            titleContentColor = PinkOnSurface,
            navigationIconContentColor = PinkOnSurface,
            actionIconContentColor = PinkOnSurface
        )
    )
}

@Composable
fun CalendarGrid(
    currentMonth: java.time.LocalDate,
    dayStatuses: List<DayStatus>,
    onDayClick: (Long) -> Unit
) {
    val daysInMonth = currentMonth.lengthOfMonth()
    val firstDayOfWeek = currentMonth.withDayOfMonth(1).dayOfWeek.value
    val adjustedFirstDay = (firstDayOfWeek + 5) % 7

    val dayStatusMap = dayStatuses.associateBy { it.date }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach { day ->
                Text(
                    text = day,
                    style = MinimalTypography.labelSmall,
                    color = PinkOnSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(adjustedFirstDay) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                )
            }

            items(daysInMonth) { day ->
                val date = DateUtils.toTimestamp(currentMonth.withDayOfMonth(day + 1))
                val status = dayStatusMap[date] ?: DayStatus(date = date)
                val isToday = DateUtils.isToday(date)

                DayCell(
                    day = day + 1,
                    status = status,
                    isToday = isToday,
                    isSelected = false,
                    onClick = { onDayClick(date) }
                )
            }
        }
    }
}

@Composable
fun DayCell(
    day: Int,
    status: DayStatus,
    isToday: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = when {
        status.isPeriodDay -> PinkPrimaryContainer
        status.isFertileDay -> Color(0xFFEDF3EC)
        status.isOvulationDay -> Color(0xFFFBF3DB)
        status.isPmsDay -> Color(0xFFFDEBEC)
        isToday -> PinkSurfaceContainer
        else -> PinkSurface
    }

    val borderColor = when {
        isToday -> PinkPrimary
        status.isPeriodDay -> PeriodColor.copy(alpha = 0.5f)
        status.isFertileDay -> Color(0xFF81C784).copy(alpha = 0.5f)
        status.isOvulationDay -> Color(0xFFFFB74D).copy(alpha = 0.5f)
        status.isPmsDay -> Color(0xFFFFAB40).copy(alpha = 0.5f)
        else -> PinkOutline.copy(alpha = 0.2f)
    }

    val textColor = when {
        status.isPeriodDay -> PeriodColor
        status.isFertileDay -> Color(0xFF346538)
        status.isOvulationDay -> Color(0xFF956400)
        status.isPmsDay -> Color(0xFF9F2F2D)
        isToday -> PinkPrimary
        else -> PinkOnSurface
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(backgroundColor, RoundedCornerShape(8.dp))
            .border(if (isToday || status.hasLog) 1.5.dp else 1.dp, borderColor, RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(4.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "$day",
                style = MinimalTypography.labelLarge.copy(color = textColor),
                modifier = Modifier.fillMaxWidth().padding(4.dp)
            )

            if (status.flow != FlowLevel.NONE) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 2.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    val dotColor = when (status.flow) {
                        FlowLevel.SPOTTING -> PeriodColor.copy(alpha = 0.4f)
                        FlowLevel.LIGHT -> PeriodColor.copy(alpha = 0.6f)
                        FlowLevel.MEDIUM -> PeriodColor.copy(alpha = 0.8f)
                        FlowLevel.HEAVY -> PeriodColor
                        else -> PinkOutline
                    }
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(dotColor, CircleShape)
                    )
                }
            }
        }
    }
}

@Composable
fun LegendRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LegendItem(color = PeriodColor, label = "Period")
        LegendItem(color = Color(0xFF81C784), label = "Fertile")
        LegendItem(color = Color(0xFFFFB74D), label = "Ovulation")
        LegendItem(color = Color(0xFFFFAB40), label = "PMS")
    }
}

@Composable
fun LegendItem(color: Color, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(color, RoundedCornerShape(12.dp))
        )
        Text(
            text = label,
            style = MinimalTypography.labelSmall
        )
    }
}

@Composable
fun QuickStatsCard(prediction: com.example.periodtracker.domain.model.PredictionResult) {
    Column {
        SectionHeader(title = "This Cycle", subtitle = null)
        MinimalDivider()
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            QuickStatItem(
                label = "Cycle Day",
                value = "${prediction.currentCycleDay}",
                icon = Icons.Filled.CalendarToday
            )
            QuickStatItem(
                label = "Next Period",
                value = "${prediction.daysUntilPeriod} days",
                icon = Icons.Filled.DateRange
            )
            QuickStatItem(
                label = "Ovulation",
                value = "${prediction.daysUntilOvulation} days",
                icon = Icons.Filled.Favorite
            )
        }
    }
}

@Composable
fun RowScope.QuickStatItem(label: String, value: String, icon: ImageVector) {
    Column(
        modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = PinkPrimary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = value,
            style = MinimalTypography.headlineMedium
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MinimalTypography.labelSmall
        )
    }
}