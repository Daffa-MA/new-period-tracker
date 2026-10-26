package com.example.periodtracker.presentation.ui.dailylog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.periodtracker.domain.model.FlowLevel
import com.example.periodtracker.domain.model.Mood
import com.example.periodtracker.domain.model.Symptom
import com.example.periodtracker.domain.model.getDisplayName
import com.example.periodtracker.presentation.ui.common.MinimalButton
import com.example.periodtracker.presentation.ui.common.MinimalCard
import com.example.periodtracker.presentation.ui.common.MinimalTextField
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyLogScreen(
    viewModel: DailyLogViewModel,
    initialDate: Long,
    onNavigateBack: () -> Unit
) {
    val flow by viewModel.flow.collectAsState()
    val symptoms by viewModel.symptoms.collectAsState()
    val moods by viewModel.moods.collectAsState()
    val notes by viewModel.notes.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val hasData by remember { mutableStateOf(viewModel.hasData()) }

    LaunchedEffect(initialDate) {
        viewModel.initialize(initialDate)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = { Text(text = DateUtils.formatDate(initialDate), style = MinimalTypography.headlineMedium) },
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
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = PinkPrimary)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().weight(1f).padding(bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    item {
                        MinimalCard(padding = 16.dp) {
                            FlowSection(selectedFlow = flow, onFlowSelected = { viewModel.onFlowSelected(it) })
                        }
                    }
                    item {
                        MinimalCard(padding = 16.dp) {
                            MultiSelectSection(
                                title = "Symptoms",
                                options = Symptom.entries.toTypedArray(),
                                selected = symptoms,
                                onToggle = { viewModel.onSymptomToggle(it) },
                                getLabel = { it.getDisplayName() }
                            )
                        }
                    }
                    item {
                        MinimalCard(padding = 16.dp) {
                            MultiSelectSection(
                                title = "Moods",
                                options = Mood.entries.toTypedArray(),
                                selected = moods,
                                onToggle = { viewModel.onMoodToggle(it) },
                                getLabel = { it.getDisplayName() }
                            )
                        }
                    }
                    item {
                        MinimalCard(padding = 16.dp) {
                            NotesSection(notes = notes, onNotesChanged = { viewModel.onNotesChanged(it) })
                        }
                    }
                }

                Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp)) {
                    MinimalButton(
                        onClick = { viewModel.saveLog() },
                        enabled = hasData && !isLoading
                    ) {
                        Text(text = "Save", style = MinimalTypography.labelLarge)
                    }
                }
            }
        }
    }
}

@Composable
fun FlowSection(
    selectedFlow: FlowLevel,
    onFlowSelected: (FlowLevel) -> Unit
) {
    Column {
        SectionHeader(title = "Flow", subtitle = "How heavy is your flow today?")
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FlowLevel.entries.forEach { level ->
                Box(modifier = Modifier.weight(1f)) {
                    FlowOptionChip(
                        flow = level,
                        isSelected = selectedFlow == level,
                        onClick = { onFlowSelected(level) }
                    )
                }
            }
        }
    }
}

@Composable
fun FlowOptionChip(
    flow: FlowLevel,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bgColor = when (flow) {
        FlowLevel.NONE -> PinkSurfaceContainer
        FlowLevel.SPOTTING -> PinkPrimaryContainer.copy(alpha = 0.3f)
        FlowLevel.LIGHT -> PinkPrimaryContainer.copy(alpha = 0.5f)
        FlowLevel.MEDIUM -> PinkPrimaryContainer.copy(alpha = 0.7f)
        FlowLevel.HEAVY -> PinkPrimaryContainer
    }
    val textColor = when (flow) {
        FlowLevel.NONE -> PinkOnSurfaceVariant
        FlowLevel.SPOTTING -> PeriodColor.copy(alpha = 0.7f)
        FlowLevel.LIGHT -> PeriodColor.copy(alpha = 0.8f)
        FlowLevel.MEDIUM -> PeriodColor
        FlowLevel.HEAVY -> PeriodColor
    }
    val icon: ImageVector = when (flow) {
        FlowLevel.NONE -> Icons.Filled.RemoveCircle
        else -> Icons.Filled.Circle
    }

    val borderColor = if (isSelected) PeriodColor else PinkOutline.copy(alpha = 0.3f)
    val borderWidth = if (isSelected) 2.dp else 1.dp

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) bgColor else PinkSurface,
            contentColor = textColor
        ),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(borderWidth, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = textColor, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = flow.getDisplayName(),
                style = MinimalTypography.labelSmall.copy(color = textColor),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun <T> MultiSelectSection(
    title: String,
    options: Array<T>,
    selected: Set<T>,
    onToggle: (T) -> Unit,
    getLabel: (T) -> String
) {
    Column {
        SectionHeader(title = title, subtitle = "Tap to select")
        Spacer(modifier = Modifier.height(12.dp))
        LazyVerticalGrid(
            columns = GridCells.Adaptive(100.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.height((((options.size + 2) / 3) * 52).dp)
        ) {
            items(options.toList()) { option ->
                val isSelected = selected.contains(option)
                TagChip(text = getLabel(option), isSelected = isSelected, onClick = { onToggle(option) })
            }
        }
    }
}

@Composable
fun TagChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bgColor = if (isSelected) PinkPrimaryContainer else PinkSurfaceContainer
    val textColor = if (isSelected) PinkPrimary else PinkOnSurfaceVariant
    val borderColor = if (isSelected) PinkPrimary else PinkOutline.copy(alpha = 0.3f)

    Card(
        modifier = Modifier.fillMaxWidth().height(44.dp).clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = bgColor, contentColor = textColor),
        shape = RoundedCornerShape(999.dp),
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp), contentAlignment = Alignment.Center) {
            Text(text = text, style = MinimalTypography.labelSmall.copy(color = textColor))
        }
    }
}

@Composable
fun NotesSection(
    notes: String,
    onNotesChanged: (String) -> Unit
) {
    Column {
        SectionHeader(title = "Notes", subtitle = "Optional")
        Spacer(modifier = Modifier.height(12.dp))
        MinimalTextField(
            value = notes,
            onValueChange = onNotesChanged,
            placeholder = "Add notes...",
            singleLine = false,
            modifier = Modifier.fillMaxWidth().height(100.dp)
        )
    }
}
