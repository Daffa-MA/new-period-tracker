package com.example.periodtracker.presentation.ui.statistics

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
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.periodtracker.domain.model.Cycle
import com.example.periodtracker.domain.model.CycleStatistics
import com.example.periodtracker.domain.model.FlowLevel
import com.example.periodtracker.domain.model.Mood
import com.example.periodtracker.domain.model.Symptom
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
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    viewModel: StatisticsViewModel,
    onNavigateBack: () -> Unit
) {
    val statistics by viewModel.statistics.collectAsState()
    val cycles by viewModel.cycles.collectAsState()
    val symptomFrequency by viewModel.symptomFrequency.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = { Text(text = "Statistics", style = MinimalTypography.headlineMedium) },
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
            modifier = Modifier.fillMaxSize().padding(padding)
        ) {
            MinimalCard(padding = 16.dp) {
                SummaryStatsCard(statistics = statistics ?: CycleStatistics(0.0, 0.0, null, null, 0, 0.0), cycles = cycles ?: emptyList())
            }

            if ((cycles ?: emptyList()).isNotEmpty()) {
                MinimalCard(padding = 16.dp) {
                    CycleHistoryCard(cycles = cycles ?: emptyList())
                }
            }

            val symptomCounts = symptomFrequency?.symptomCounts ?: emptyMap()
            if (symptomCounts.isNotEmpty()) {
                MinimalCard(padding = 16.dp) {
                    SymptomFrequencyCard(symptomCounts = symptomCounts)
                }
            }

            val moodCounts = symptomFrequency?.moodCounts ?: emptyMap()
            if (moodCounts.isNotEmpty()) {
                MinimalCard(padding = 16.dp) {
                    MoodDistributionCard(moodCounts = moodCounts)
                }
            }

            val flowCounts = symptomFrequency?.flowCounts ?: emptyMap()
            if (flowCounts.isNotEmpty()) {
                MinimalCard(padding = 16.dp) {
                    FlowDistributionCard(flowCounts = flowCounts)
                }
            }
        }
    }
}

@Composable
fun SummaryStatsCard(
    statistics: CycleStatistics,
    cycles: List<Cycle>
) {
    Column {
        SectionHeader(title = "Overview")
        MinimalDivider()
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SummaryStatItem(
                label = "Avg Cycle",
                value = "${statistics.averageCycleLength.roundToInt()} days",
                icon = Icons.Filled.CalendarMonth
            )
            VerticalDivider()
            SummaryStatItem(
                label = "Avg Period",
                value = "${statistics.averagePeriodLength.roundToInt()} days",
                icon = Icons.Filled.Event
            )
            VerticalDivider()
            SummaryStatItem(
                label = "Cycles Tracked",
                value = "${statistics.totalCycles}",
                icon = Icons.Filled.Timeline
            )
        }

        if (statistics.shortestCycle != null && statistics.longestCycle != null) {
            Spacer(modifier = Modifier.height(20.dp))
            MinimalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SummaryStatItem(
                    label = "Shortest Cycle",
                    value = "${statistics.shortestCycle} days",
                    icon = Icons.Filled.TrendingDown
                )
                VerticalDivider()
                SummaryStatItem(
                    label = "Longest Cycle",
                    value = "${statistics.longestCycle} days",
                    icon = Icons.Filled.TrendingUp
                )
            }
        }
    }
}

@Composable
fun RowScope.SummaryStatItem(label: String, value: String, icon: ImageVector) {
    Column(
        modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = PinkPrimary, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = value, style = MinimalTypography.headlineLarge)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, style = MinimalTypography.labelSmall)
    }
}

@Composable
fun CycleHistoryCard(cycles: List<Cycle>) {
    Column {
        SectionHeader(title = "Cycle History", subtitle = "${cycles.size} cycles recorded")
        MinimalDivider()
        Spacer(modifier = Modifier.height(12.dp))

        cycles.take(10).forEachIndexed { index, cycle ->
            if (index > 0) MinimalDivider()
            CycleHistoryItem(cycle = cycle)
        }

        if (cycles.size > 10) {
            MinimalDivider()
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "And ${cycles.size - 10} more cycles...",
                style = MinimalTypography.labelSmall,
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )
        }
    }
}

@Composable
fun CycleHistoryItem(cycle: Cycle) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = cycle.startDateFormatted,
                style = MinimalTypography.bodyLarge
            )
            Text(
                text = "Cycle ${cycle.cycleLength?.let { "$it days" } ?: "—"} • Period ${cycle.periodLength?.let { "$it days" } ?: "—"}",
                style = MinimalTypography.labelSmall
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (cycle.cycleLength != null) {
                Text(text = "${cycle.cycleLength} days", style = MinimalTypography.bodyMedium)
            }
            if (cycle.periodLength != null) {
                Text(text = "• ${cycle.periodLength} days", style = MinimalTypography.labelSmall)
            }
        }
    }
}

@Composable
fun SymptomFrequencyCard(symptomCounts: Map<Symptom, Int>) {
    val sortedSymptoms = symptomCounts.toList().sortedByDescending { it.second }

    Column {
        SectionHeader(title = "Most Frequent Symptoms")
        MinimalDivider()
        Spacer(modifier = Modifier.height(12.dp))

        LazyVerticalGrid(
            columns = GridCells.Adaptive(140.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(sortedSymptoms.size) { index ->
                val (symptom, count) = sortedSymptoms[index]
                FrequencyItem(label = symptom.toString(), count = count, color = PeriodColor)
            }
        }
    }
}

@Composable
fun MoodDistributionCard(moodCounts: Map<Mood, Int>) {
    val sortedMoods = moodCounts.toList().sortedByDescending { it.second }

    Column {
        SectionHeader(title = "Mood Distribution")
        MinimalDivider()
        Spacer(modifier = Modifier.height(12.dp))

        LazyVerticalGrid(
            columns = GridCells.Adaptive(140.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(sortedMoods.size) { index ->
                val (mood, count) = sortedMoods[index]
                FrequencyItem(label = mood.toString(), count = count, color = Color(0xFFFFB74D))
            }
        }
    }
}

@Composable
fun FlowDistributionCard(flowCounts: Map<FlowLevel, Int>) {
    val sortedFlows = flowCounts.toList().sortedByDescending { it.second }
    val total = flowCounts.values.sum()

    Column {
        SectionHeader(title = "Flow Distribution")
        MinimalDivider()
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            sortedFlows.forEach { (flow, count) ->
                FlowDistributionItem(flow = flow, count = count, total = total)
            }
        }
    }
}

@Composable
fun FrequencyItem(label: String, count: Int, color: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = PinkSurface,
            contentColor = PinkOnSurface
        ),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, PinkOutline.copy(alpha = 0.3f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = label, style = MinimalTypography.bodyMedium, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "$count", style = MinimalTypography.headlineLarge.copy(color = color))
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = "times", style = MinimalTypography.labelSmall)
        }
    }
}

@Composable
fun RowScope.FlowDistributionItem(flow: FlowLevel, count: Int, total: Int) {
    val percentage = if (total > 0) (count * 100 / total) else 0
    val color = when (flow) {
        FlowLevel.SPOTTING -> PeriodColor.copy(alpha = 0.4f)
        FlowLevel.LIGHT -> PeriodColor.copy(alpha = 0.6f)
        FlowLevel.MEDIUM -> PeriodColor.copy(alpha = 0.8f)
        FlowLevel.HEAVY -> PeriodColor
        else -> PinkOutline
    }

    Card(
        modifier = Modifier.weight(1f),
        colors = CardDefaults.cardColors(
            containerColor = PinkSurface,
            contentColor = PinkOnSurface
        ),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, PinkOutline.copy(alpha = 0.3f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(color, CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = flow.toString(), style = MinimalTypography.bodyMedium)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "$count", style = MinimalTypography.headlineLarge.copy(color = color))
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = "$percentage% of logs", style = MinimalTypography.labelSmall)
        }
    }
}

@Composable
private fun VerticalDivider() {
    Spacer(modifier = Modifier.height(60.dp).width(1.dp))
}
