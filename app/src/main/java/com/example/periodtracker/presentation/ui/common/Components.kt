package com.example.periodtracker.presentation.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.compose.ui.unit.sp
import com.example.periodtracker.ui.theme.*

// Minimalist Card - inspired by Stripe design system
// Conservative 6dp radius, subtle elevation, pink-tinted aesthetic
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MinimalCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    padding: androidx.compose.ui.unit.Dp = 24.dp,
    elevated: Boolean = false,
    content: @Composable () -> Unit
) {
    val cardColors = CardDefaults.cardColors(
        containerColor = PinkSurface,
        contentColor = PinkOnSurface
    )
    
    val elevation = if (elevated) 8.dp else 2.dp

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp)),
        colors = cardColors,
        border = BorderStroke(1.dp, PinkOutline.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        onClick = { onClick?.invoke() }
    ) {
        Box(
            modifier = Modifier.padding(padding),
            contentAlignment = Alignment.CenterStart
        ) {
            content()
        }
    }
}

// Minimalist Button - solid background, 4-6dp radius, no shadow
@Composable
fun MinimalButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = PinkPrimary,
            contentColor = PinkOnPrimary,
            disabledContainerColor = PinkPrimary.copy(alpha = 0.38f),
            disabledContentColor = PinkOnPrimary.copy(alpha = 0.38f)
        ),
        shape = RoundedCornerShape(6.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
    ) {
        content()
    }
}

// Secondary/Outline Button
@Composable
fun MinimalOutlineButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        enabled = enabled,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = PinkPrimary,
            disabledContentColor = PinkPrimary.copy(alpha = 0.38f)
        ),
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(1.dp, PinkOutline.copy(alpha = if (enabled) 0.6f else 0.12f)),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
    ) {
        content()
    }
}

// Tag/Badge - pill shape, small text, muted pastel background
@Composable
fun MinimalTag(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = PinkPrimaryContainer,
    textColor: Color = PinkOnSurfaceVariant
) {
    Box(
        modifier = modifier
            .padding(horizontal = 10.dp, vertical = 4.dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(999.dp)
            )
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.5.sp,
            color = textColor
        )
    }
}

// Status Tag with semantic colors
@Composable
fun StatusTag(
    text: String,
    modifier: Modifier = Modifier,
    status: StatusType
) {
    val (bgColor, textColor) = when (status) {
        StatusType.PERIOD -> PinkPrimaryContainer to PinkOnSurface
        StatusType.FERTILE -> Color(0xFFEDF3EC) to Color(0xFF346538)
        StatusType.OVULATION -> Color(0xFFFBF3DB) to Color(0xFF956400)
        StatusType.PMS -> Color(0xFFFDEBEC) to Color(0xFF9F2F2D)
        StatusType.NORMAL -> Color(0xFFF0F0F0) to PinkOnSurfaceVariant
    }
    MinimalTag(text = text, modifier = modifier, backgroundColor = bgColor, textColor = textColor)
}

enum class StatusType {
    PERIOD, FERTILE, OVULATION, PMS, NORMAL
}

// Divider - 1px ultra-light
@Composable
fun MinimalDivider(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(PinkOutline.copy(alpha = 0.3f))
    )
}

// Section Header with editorial typography
@Composable
fun SectionHeader(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier,
    action: (() -> Unit)? = null,
    actionLabel: String? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Normal,
                    letterSpacing = (-0.03).sp,
                    lineHeight = 34.sp,
                    color = PinkOnSurface,
                    fontFamily = FontFamily.Default
                )
                subtitle?.let {
                    Text(
                        text = it,
                        fontSize = 14.sp,
                        color = PinkOnSurfaceVariant,
                        lineHeight = 20.sp,
                        fontFamily = FontFamily.Default
                    )
                }
            }
            action?.let {
                Text(
                    text = actionLabel ?: "",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = PinkPrimary,
                    modifier = Modifier
                        .clickable { it() }
                        .padding(start = 16.dp)
                )
            }
        }
    }
}

// Typography styles for the app
object MinimalTypography {
    val displayLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 40.sp,
        lineHeight = 48.sp,
        letterSpacing = (-0.04).sp,
        color = PinkOnSurface
    )

    val displayMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = (-0.03).sp,
        color = PinkOnSurface
    )

    val headlineLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = (-0.02).sp,
        color = PinkOnSurface
    )

    val headlineMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
        color = PinkOnSurface
    )

    val titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp,
        color = PinkOnSurface
    )

    val bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.15.sp,
        color = PinkOnSurface
    )

    val bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.25.sp,
        color = PinkOnSurfaceVariant
    )

    val labelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
        color = PinkOnSurface
    )

    val labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
        color = PinkOnSurfaceVariant
    )
}

// Input Field - minimal, flat
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MinimalTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String? = null,
    placeholder: String = "",
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    trailingIcon: (@Composable () -> Unit)? = null
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp),
        label = label?.let { { Text(text = it, style = MinimalTypography.labelSmall) } },
        placeholder = { Text(text = placeholder, style = MinimalTypography.bodyMedium) },
        enabled = enabled,
        singleLine = singleLine,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = PinkSurfaceContainer,
            unfocusedContainerColor = PinkSurfaceContainer,
            disabledContainerColor = PinkSurfaceContainer.copy(alpha = 0.5f),
            focusedIndicatorColor = PinkPrimary,
            unfocusedIndicatorColor = PinkOutline.copy(alpha = 0.3f),
            cursorColor = PinkPrimary
        ),
        shape = RoundedCornerShape(8.dp),
        trailingIcon = trailingIcon
    )
}

// Empty State
@Composable
fun MinimalEmptyState(
    title: String,
    message: String,
    modifier: Modifier = Modifier,
    action: (() -> Unit)? = null,
    actionLabel: String? = null,
    icon: ImageVector? = null
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        icon?.let {
            Icon(
                imageVector = it,
                contentDescription = null,
                tint = PinkOutline.copy(alpha = 0.4f),
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        Text(
            text = title,
            style = MinimalTypography.headlineMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            style = MinimalTypography.bodyMedium,
            textAlign = TextAlign.Center
        )
        action?.let {
            Spacer(modifier = Modifier.height(24.dp))
            MinimalButton(onClick = it) {
                Text(text = actionLabel ?: "", style = MinimalTypography.labelLarge)
            }
        }
    }
}
