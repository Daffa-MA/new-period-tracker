package com.example.periodtracker.presentation.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.periodtracker.presentation.navigation.Screen
import com.example.periodtracker.presentation.ui.common.MinimalButton
import com.example.periodtracker.presentation.ui.common.MinimalCard
import com.example.periodtracker.presentation.ui.common.MinimalDivider
import com.example.periodtracker.presentation.ui.common.MinimalTag
import com.example.periodtracker.presentation.ui.common.MinimalTextField
import com.example.periodtracker.presentation.ui.common.MinimalTypography
import com.example.periodtracker.presentation.ui.common.SectionHeader
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
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onNavigateBack: () -> Unit
) {
    val avgCycleLength by viewModel.averageCycleLength.collectAsState()
    val avgPeriodLength by viewModel.averagePeriodLength.collectAsState()
    val remindersEnabled by viewModel.remindersEnabled.collectAsState()
    val periodReminderDays by viewModel.periodReminderDaysBefore.collectAsState()
    val ovulationReminderEnabled by viewModel.ovulationReminderEnabled.collectAsState()
    val fertileReminderEnabled by viewModel.fertileWindowReminderEnabled.collectAsState()
    val themeMode by viewModel.themeMode.collectAsState()
    val language by viewModel.language.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(
                        text = "Settings",
                        style = MinimalTypography.headlineMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = PinkOnSurface
                        )
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
        androidx.compose.foundation.lazy.LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 100.dp)
        ) {
            item {
                MinimalCard(padding = 16.dp) {
                    CycleSettingsSection(
                        avgCycleLength = avgCycleLength,
                        onCycleLengthChanged = { viewModel.onAverageCycleLengthChanged(it) },
                        avgPeriodLength = avgPeriodLength,
                        onPeriodLengthChanged = { viewModel.onAveragePeriodLengthChanged(it) }
                    )
                }
            }

            item {
                MinimalCard(padding = 16.dp) {
                    RemindersSection(
                        remindersEnabled = remindersEnabled,
                        onRemindersChanged = { viewModel.onRemindersEnabledChanged(it) },
                        periodReminderDays = periodReminderDays,
                        onPeriodReminderDaysChanged = { viewModel.onPeriodReminderDaysBeforeChanged(it) },
                        ovulationReminderEnabled = ovulationReminderEnabled,
                        onOvulationReminderChanged = { viewModel.onOvulationReminderEnabledChanged(it) },
                        fertileReminderEnabled = fertileReminderEnabled,
                        onFertileReminderChanged = { viewModel.onFertileWindowReminderEnabledChanged(it) }
                    )
                }
            }

            item {
                MinimalCard(padding = 16.dp) {
                    AppearanceSection(
                        themeMode = themeMode,
                        onThemeModeChanged = { viewModel.onThemeModeChanged(it) },
                        language = language,
                        onLanguageChanged = { viewModel.onLanguageChanged(it) }
                    )
                }
            }

            item {
                MinimalCard(padding = 16.dp) {
                    DataSection()
                }
            }

            item {
                MinimalCard(padding = 16.dp) {
                    AboutSection()
                }
            }
        }
    }
}

@Composable
fun CycleSettingsSection(
    avgCycleLength: Int,
    onCycleLengthChanged: (Int) -> Unit,
    avgPeriodLength: Int,
    onPeriodLengthChanged: (Int) -> Unit
) {
    Column {
        SectionHeader(title = "Cycle Settings", subtitle = "Your average cycle and period lengths")

        MinimalDivider()

        Spacer(modifier = Modifier.height(20.dp))

        SettingRow(
            label = "Average Cycle Length",
            value = "$avgCycleLength days",
            subtitle = "Typical range: 21–45 days"
        ) {
            Slider(
                value = avgCycleLength.toFloat(),
                onValueChange = { onCycleLengthChanged(it.roundToInt()) },
                valueRange = 21f..45f,
                steps = 24,
                modifier = Modifier.weight(1f),
                colors = SliderDefaults.colors(
                    thumbColor = PinkPrimary,
                    activeTrackColor = PinkPrimary,
                    inactiveTrackColor = PinkOutline.copy(alpha = 0.3f)
                )
            )
        }

        MinimalDivider()

        Spacer(modifier = Modifier.height(16.dp))

        SettingRow(
            label = "Average Period Length",
            value = "$avgPeriodLength days",
            subtitle = "Typical range: 1–10 days"
        ) {
            Slider(
                value = avgPeriodLength.toFloat(),
                onValueChange = { onPeriodLengthChanged(it.roundToInt()) },
                valueRange = 1f..10f,
                steps = 9,
                modifier = Modifier.weight(1f),
                colors = SliderDefaults.colors(
                    thumbColor = PeriodColor,
                    activeTrackColor = PeriodColor,
                    inactiveTrackColor = PinkOutline.copy(alpha = 0.3f)
                )
            )
        }
    }
}

@Composable
fun RemindersSection(
    remindersEnabled: Boolean,
    onRemindersChanged: (Boolean) -> Unit,
    periodReminderDays: Int,
    onPeriodReminderDaysChanged: (Int) -> Unit,
    ovulationReminderEnabled: Boolean,
    onOvulationReminderChanged: (Boolean) -> Unit,
    fertileReminderEnabled: Boolean,
    onFertileReminderChanged: (Boolean) -> Unit
) {
    Column {
        SectionHeader(title = "Reminders", subtitle = "Get notified about your cycle")

        MinimalDivider()

        Spacer(modifier = Modifier.height(16.dp))

        SettingRow(
            label = "Enable Reminders",
            subtitle = "Receive notifications for period, ovulation, and fertile window"
        ) {
            Switch(
                checked = remindersEnabled,
                onCheckedChange = onRemindersChanged,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = PinkPrimary,
                    checkedTrackColor = PinkPrimaryContainer,
                    uncheckedThumbColor = PinkOnSurfaceVariant.copy(alpha = 0.6f),
                    uncheckedTrackColor = PinkOutline.copy(alpha = 0.3f)
                )
            )
        }

        if (remindersEnabled) {
            MinimalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            SettingRow(
                label = "Period Reminder",
                subtitle = "Days before expected period start",
                value = "$periodReminderDays days"
            ) {
                Slider(
                    value = periodReminderDays.toFloat(),
                    onValueChange = { onPeriodReminderDaysChanged(it.roundToInt()) },
                    valueRange = 0f..7f,
                    steps = 7,
                    modifier = Modifier.weight(1f),
                    colors = SliderDefaults.colors(
                        thumbColor = PeriodColor,
                        activeTrackColor = PeriodColor,
                        inactiveTrackColor = PinkOutline.copy(alpha = 0.3f)
                    )
                )
            }

            MinimalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            SettingRow(
                label = "Ovulation Reminder",
                subtitle = "Notify on ovulation day"
            ) {
                Switch(
                    checked = ovulationReminderEnabled,
                    onCheckedChange = onOvulationReminderChanged,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color(0xFFFFB74D),
                        checkedTrackColor = Color(0xFFFFF3E0),
                        uncheckedThumbColor = PinkOnSurfaceVariant.copy(alpha = 0.6f),
                        uncheckedTrackColor = PinkOutline.copy(alpha = 0.3f)
                    )
                )
            }

            MinimalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            SettingRow(
                label = "Fertile Window Reminder",
                subtitle = "Notify when fertile window starts"
            ) {
                Switch(
                    checked = fertileReminderEnabled,
                    onCheckedChange = onFertileReminderChanged,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color(0xFF81C784),
                        checkedTrackColor = Color(0xFFEDF3EC),
                        uncheckedThumbColor = PinkOnSurfaceVariant.copy(alpha = 0.6f),
                        uncheckedTrackColor = PinkOutline.copy(alpha = 0.3f)
                    )
                )
            }
        }
    }
}

@Composable
fun AppearanceSection(
    themeMode: Int,
    onThemeModeChanged: (Int) -> Unit,
    language: String,
    onLanguageChanged: (String) -> Unit
) {
    Column {
        SectionHeader(title = "Appearance", subtitle = "Customize how the app looks")

        MinimalDivider()

        Spacer(modifier = Modifier.height(16.dp))

        SettingRow(
            label = "Theme",
            value = when (themeMode) {
                0 -> "System Default"
                1 -> "Light"
                2 -> "Dark"
                else -> "System Default"
            }
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterEnd
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(0 to "System", 1 to "Light", 2 to "Dark").forEach { (mode, label) ->
                        val isSelected = themeMode == mode
                        androidx.compose.material3.TextButton(
                            onClick = { onThemeModeChanged(mode) },
                            modifier = Modifier
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                                .background(
                                    color = if (isSelected) PinkPrimaryContainer else PinkSurfaceContainer,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .border(if (isSelected) 1.5.dp else 1.dp,
                                    if (isSelected) PinkPrimary else PinkOutline.copy(alpha = 0.3f),
                                    RoundedCornerShape(8.dp)
                                )
                        ) {
                            Text(
                                text = label,
                                style = MinimalTypography.labelSmall.copy(
                                    color = if (isSelected) PinkPrimary else PinkOnSurfaceVariant
                                )
                            )
                        }
                    }
                }
            }
        }

        MinimalDivider()
        Spacer(modifier = Modifier.height(16.dp))

        SettingRow(
            label = "Language",
            value = when (language) {
                "id" -> "Indonesian"
                "en" -> "English"
                else -> "Indonesian"
            }
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterEnd
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("id" to "ID", "en" to "EN").forEach { (lang, label) ->
                        val isSelected = language == lang
                        androidx.compose.material3.TextButton(
                            onClick = { onLanguageChanged(lang) },
                            modifier = Modifier
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                                .background(
                                    color = if (isSelected) PinkPrimaryContainer else PinkSurfaceContainer,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .border(if (isSelected) 1.5.dp else 1.dp,
                                    if (isSelected) PinkPrimary else PinkOutline.copy(alpha = 0.3f),
                                    RoundedCornerShape(8.dp)
                                )
                        ) {
                            Text(
                                text = label,
                                style = MinimalTypography.labelSmall.copy(
                                    color = if (isSelected) PinkPrimary else PinkOnSurfaceVariant
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DataSection() {
    Column {
        SectionHeader(title = "Data", subtitle = "Export or backup your data")

        MinimalDivider()

        Spacer(modifier = Modifier.height(16.dp))

        SettingRow(
            label = "Export Data",
            subtitle = "Download your cycle data as CSV"
        ) {
            androidx.compose.material3.TextButton(
                onClick = { /* viewModel.exportData() */ },
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .background(
                        color = PinkPrimaryContainer,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                Text(text = "Export CSV", style = MinimalTypography.labelLarge.copy(color = PinkPrimary))
            }
        }

        MinimalDivider()
        Spacer(modifier = Modifier.height(16.dp))

        SettingRow(
            label = "Import Data",
            subtitle = "Restore from a CSV backup"
        ) {
            androidx.compose.material3.TextButton(
                onClick = { /* viewModel.importData() */ },
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .background(
                        color = PinkSurfaceContainer,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .border(1.dp, PinkOutline.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            ) {
                Text(text = "Import CSV", style = MinimalTypography.labelLarge.copy(color = PinkOnSurface))
            }
        }

        MinimalDivider()
        Spacer(modifier = Modifier.height(16.dp))

        SettingRow(
            label = "Clear All Data",
            subtitle = "Permanently delete all cycles and logs",
            value = ""
        ) {
            androidx.compose.material3.TextButton(
                onClick = { /* show confirmation dialog */ },
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .background(
                        color = Color(0xFFFDEBEC),
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                Text(text = "Delete All", style = MinimalTypography.labelLarge.copy(color = Color(0xFF9F2F2D)))
            }
        }
    }
}

@Composable
fun AboutSection() {
    Column {
        SectionHeader(title = "About", subtitle = "App information")

        MinimalDivider()

        Spacer(modifier = Modifier.height(16.dp))

        SettingRow(label = "Version", value = "1.0.0") {}

        MinimalDivider()
        Spacer(modifier = Modifier.height(16.dp))

        SettingRow(label = "Privacy Policy", value = "") {
            androidx.compose.material3.TextButton(onClick = { /* open privacy policy */ }) {
                Text(text = "View", style = MinimalTypography.labelLarge.copy(color = PinkPrimary))
            }
        }

        MinimalDivider()
        Spacer(modifier = Modifier.height(16.dp))

        SettingRow(label = "Terms of Service", value = "") {
            androidx.compose.material3.TextButton(onClick = { /* open terms */ }) {
                Text(text = "View", style = MinimalTypography.labelLarge.copy(color = PinkPrimary))
            }
        }
    }
}

@Composable
fun SettingRow(
    label: String,
    subtitle: String? = null,
    value: String = "",
    trailingContent: @Composable () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = label, style = MinimalTypography.bodyLarge)
            subtitle?.let {
                Text(text = it, style = MinimalTypography.labelSmall)
            }
        }
        if (value.isNotBlank()) {
            Text(text = value, style = MinimalTypography.bodyMedium.copy(color = PinkOnSurfaceVariant))
        }
        trailingContent()
    }
}
