package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Category
import androidx.compose.material.icons.rounded.DirectionsCar
import androidx.compose.material.icons.rounded.Fastfood
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.ReceiptLong
import androidx.compose.material.icons.rounded.TrackChanges
import androidx.compose.material.icons.rounded.TrendingDown
import androidx.compose.material.icons.rounded.TrendingFlat
import androidx.compose.material.icons.rounded.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.max

data class SpendingSummaryItem(
    val categoryName: String,
    val amountSpent: Double,
    val icon: ImageVector = Icons.Rounded.Category,
    val progressColor: Color = FinTrackAqua
)

private val FinTrackBackground = Color(0xFF06121A)
private val FinTrackCard = Color(0xFF0B1C26)
private val FinTrackCardLight = Color(0xFF102B38)
private val FinTrackMint = Color(0xFFA6E22E)
private val FinTrackAqua = Color(0xFF38D6A5)
private val FinTrackTeal = Color(0xFF1AA3A8)
private val FinTrackText = Color(0xFFEAF6F7)
private val FinTrackMutedText = Color(0xFF9FB4BD)
private val FinTrackTrack = Color(0xFF1D3B48)

private val MinimumMarkerBlue = Color(0xFF4DA3FF)
private val SpentMarkerGrey = Color(0xFF8E9BAA)
private val BelowMinimumYellow = Color(0xFFFFD54F)
private val WithinGoalGreen = Color(0xFF38D6A5)
private val AboveMaximumRed = Color(0xFFE04F5F)
private val MarkerWhite = Color(0xFFEAF6F7)

@Composable
fun BudgetProgressSummarySection(
    minimumGoal: Double?,
    maximumGoal: Double?,
    amountSpent: Double,
    spendingItems: List<SpendingSummaryItem> = emptyList(),
    modifier: Modifier = Modifier
) {
    val status = getProgressStatus(
        minimumGoal = minimumGoal,
        maximumGoal = maximumGoal,
        amountSpent = amountSpent
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(FinTrackBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        MainProgressVisualCard(
            minimumGoal = minimumGoal,
            maximumGoal = maximumGoal,
            amountSpent = amountSpent,
            status = status
        )

        if (spendingItems.isNotEmpty()) {
            SpendingSummaryCard(
                maximumGoal = maximumGoal,
                spendingItems = spendingItems
            )
        }
    }
}

@Composable
private fun MainProgressVisualCard(
    minimumGoal: Double?,
    maximumGoal: Double?,
    amountSpent: Double,
    status: ProgressStatus
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = FinTrackCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            FinTrackCardLight.copy(alpha = 0.96f),
                            FinTrackCard.copy(alpha = 0.99f)
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = status.icon,
                    contentDescription = null,
                    tint = status.color,
                    modifier = Modifier.size(30.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Progress Tracking",
                        color = FinTrackText,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Text(
                        text = status.title,
                        color = status.color,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = status.message,
                color = FinTrackMutedText,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(18.dp))

            VisualGoalProgressBar(
                minimumGoal = minimumGoal,
                maximumGoal = maximumGoal,
                amountSpent = amountSpent,
                progressColor = status.color,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            GoalColourLegend()
        }
    }
}

@Composable
private fun VisualGoalProgressBar(
    minimumGoal: Double?,
    maximumGoal: Double?,
    amountSpent: Double,
    progressColor: Color,
    modifier: Modifier = Modifier
) {
    val safeMin = minimumGoal ?: 0.0
    val safeMax = maximumGoal ?: 0.0
    val safeSpent = amountSpent.coerceAtLeast(0.0)

    val visualMax = max(
        max(safeMax, safeSpent),
        safeMin
    ).let { highest ->
        if (highest <= 0.0) 1.0 else highest * 1.20
    }

    val spentProgress = (safeSpent / visualMax).toFloat().coerceIn(0f, 1f)

    val animatedSpentProgress by animateFloatAsState(
        targetValue = spentProgress,
        animationSpec = tween(durationMillis = 1000),
        label = "MainGoalProgressAnimation"
    )

    Canvas(
        modifier = modifier
            .height(150.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0x9907111F), RoundedCornerShape(18.dp))
            .border(1.dp, Color.White.copy(alpha = 0.10f), RoundedCornerShape(18.dp))
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        val barLeft = 26f
        val barRight = size.width - 26f
        val barWidth = barRight - barLeft
        val barHeight = 30f
        val barTop = size.height * 0.46f
        val barBottom = barTop + barHeight
        val barCenterY = barTop + barHeight / 2f

        fun valueToX(value: Double): Float {
            return (barLeft + ((value / visualMax).toFloat().coerceIn(0f, 1f) * barWidth))
        }

        val spentX = barLeft + animatedSpentProgress * barWidth
        val minX = valueToX(safeMin)
        val maxX = valueToX(safeMax)

        val titlePaint = android.graphics.Paint().apply {
            color = android.graphics.Color.WHITE
            textSize = 26f
            isAntiAlias = true
            textAlign = android.graphics.Paint.Align.CENTER
            isFakeBoldText = true
        }

        val smallPaint = android.graphics.Paint().apply {
            color = android.graphics.Color.WHITE
            textSize = 22f
            isAntiAlias = true
            textAlign = android.graphics.Paint.Align.CENTER
            isFakeBoldText = true
        }

        val labelPaint = android.graphics.Paint().apply {
            color = android.graphics.Color.WHITE
            textSize = 20f
            isAntiAlias = true
            textAlign = android.graphics.Paint.Align.CENTER
            isFakeBoldText = true
        }

        drawRoundRect(
            color = FinTrackTrack,
            topLeft = Offset(barLeft, barTop),
            size = Size(barWidth, barHeight),
            cornerRadius = CornerRadius(30f, 30f)
        )

        drawRoundRect(
            brush = Brush.horizontalGradient(
                colors = listOf(
                    progressColor.copy(alpha = 0.95f),
                    progressColor.copy(alpha = 0.75f)
                )
            ),
            topLeft = Offset(barLeft, barTop),
            size = Size((spentX - barLeft).coerceAtLeast(0f), barHeight),
            cornerRadius = CornerRadius(30f, 30f)
        )

        if (safeMin > 0.0) {
            drawGoalMarker(
                x = minX,
                barTop = barTop,
                barBottom = barBottom,
                markerColor = MinimumMarkerBlue,
                label = "MIN",
                value = safeMin,
                labelAbove = true,
                verticalOffset = -22f,
                labelPaint = labelPaint
            )
        }

        if (safeMax > 0.0) {
            drawGoalMarker(
                x = maxX,
                barTop = barTop,
                barBottom = barBottom,
                markerColor = AboveMaximumRed,
                label = "MAX",
                value = safeMax,
                labelAbove = true,
                verticalOffset = 20f,
                labelPaint = labelPaint
            )
        }

        drawSpentMarker(
            x = spentX,
            barCenterY = barCenterY,
            markerColor = SpentMarkerGrey,
            amountSpent = safeSpent,
            titlePaint = titlePaint,
            smallPaint = smallPaint,
            labelPaint = labelPaint
        )

        drawRoundRect(
            color = Color.White.copy(alpha = 0.45f),
            topLeft = Offset(barLeft, barTop),
            size = Size(barWidth, barHeight),
            cornerRadius = CornerRadius(30f, 30f),
            style = Stroke(width = 2f)
        )
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawGoalMarker(
    x: Float,
    barTop: Float,
    barBottom: Float,
    markerColor: Color,
    label: String,
    value: Double,
    labelAbove: Boolean,
    verticalOffset: Float,
    labelPaint: android.graphics.Paint
) {
    val markerTop = barTop - 22f
    val markerBottom = barBottom + 22f

    drawLine(
        color = markerColor,
        start = Offset(x, markerTop),
        end = Offset(x, markerBottom),
        strokeWidth = 6f
    )

    drawCircle(
        color = markerColor,
        radius = 9f,
        center = Offset(x, barTop - 8f)
    )

    val badgeWidth = 96f
    val badgeHeight = 32f
    val badgeTop = if (labelAbove) {
        barTop - 72f + verticalOffset
    } else {
        barBottom + 34f + verticalOffset
    }

    val badgeLeft = (x - badgeWidth / 2f)
        .coerceIn(4f, size.width - badgeWidth - 4f)

    drawRoundRect(
        color = markerColor.copy(alpha = 0.96f),
        topLeft = Offset(badgeLeft, badgeTop),
        size = Size(badgeWidth, badgeHeight),
        cornerRadius = CornerRadius(10f, 10f)
    )

    drawRoundRect(
        color = MarkerWhite.copy(alpha = 0.75f),
        topLeft = Offset(badgeLeft, badgeTop),
        size = Size(badgeWidth, badgeHeight),
        cornerRadius = CornerRadius(10f, 10f),
        style = Stroke(width = 1.4f)
    )

    drawContext.canvas.nativeCanvas.drawText(
        "$label ${formatShortRand(value)}",
        badgeLeft + badgeWidth / 2f,
        badgeTop + 22f,
        labelPaint
    )
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawSpentMarker(
    x: Float,
    barCenterY: Float,
    markerColor: Color,
    amountSpent: Double,
    titlePaint: android.graphics.Paint,
    smallPaint: android.graphics.Paint,
    labelPaint: android.graphics.Paint
) {
    drawCircle(
        color = markerColor,
        radius = 17f,
        center = Offset(x, barCenterY)
    )

    drawCircle(
        color = Color.White.copy(alpha = 0.95f),
        radius = 7f,
        center = Offset(x, barCenterY)
    )

    val badgeWidth = 128f
    val badgeHeight = 38f
    val badgeTop = barCenterY + 34f
    val badgeLeft = (x - badgeWidth / 2f)
        .coerceIn(4f, size.width - badgeWidth - 4f)

    drawRoundRect(
        color = markerColor.copy(alpha = 0.96f),
        topLeft = Offset(badgeLeft, badgeTop),
        size = Size(badgeWidth, badgeHeight),
        cornerRadius = CornerRadius(12f, 12f)
    )

    drawRoundRect(
        color = MarkerWhite.copy(alpha = 0.75f),
        topLeft = Offset(badgeLeft, badgeTop),
        size = Size(badgeWidth, badgeHeight),
        cornerRadius = CornerRadius(12f, 12f),
        style = Stroke(width = 1.4f)
    )

    drawContext.canvas.nativeCanvas.drawText(
        "SPENT ${formatShortRand(amountSpent)}",
        badgeLeft + badgeWidth / 2f,
        badgeTop + 25f,
        labelPaint
    )
}

@Composable
private fun GoalColourLegend() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        LegendItem(
            color = BelowMinimumYellow,
            label = "Below Min"
        )

        LegendItem(
            color = WithinGoalGreen,
            label = "In Range"
        )

        LegendItem(
            color = AboveMaximumRed,
            label = "Over Max"
        )
    }
}

@Composable
private fun LegendItem(
    color: Color,
    label: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(color, RoundedCornerShape(50))
        )

        Spacer(modifier = Modifier.width(5.dp))

        Text(
            text = label,
            color = FinTrackMutedText,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun SpendingSummaryCard(
    maximumGoal: Double?,
    spendingItems: List<SpendingSummaryItem>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = FinTrackCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            FinTrackCardLight.copy(alpha = 0.9f),
                            FinTrackCard.copy(alpha = 0.98f)
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Text(
                text = "Category Breakdown",
                color = FinTrackText,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(18.dp))

            spendingItems.forEach { item ->
                SpendingSummaryRow(
                    item = item,
                    maximumGoal = maximumGoal
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun SpendingSummaryRow(
    item: SpendingSummaryItem,
    maximumGoal: Double?
) {
    val progress = if (maximumGoal != null && maximumGoal > 0) {
        (item.amountSpent / maximumGoal).toFloat().coerceIn(0f, 1f)
    } else {
        0f
    }

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 900),
        label = "CategoryProgressAnimation"
    )

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.categoryName,
                tint = FinTrackText
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = item.categoryName,
                color = FinTrackText,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = formatRand(item.amountSpent),
                color = FinTrackText,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(50.dp)),
            color = item.progressColor,
            trackColor = FinTrackTrack,
            strokeCap = StrokeCap.Round
        )
    }
}

private data class ProgressStatus(
    val title: String,
    val message: String,
    val icon: ImageVector,
    val color: Color
)

private fun getProgressStatus(
    minimumGoal: Double?,
    maximumGoal: Double?,
    amountSpent: Double
): ProgressStatus {
    return when {
        minimumGoal == null || maximumGoal == null -> {
            ProgressStatus(
                title = "Goal Not Set",
                message = "Set your minimum and maximum monthly goals to activate the visual progress tracker.",
                icon = Icons.Rounded.TrackChanges,
                color = FinTrackAqua
            )
        }

        amountSpent < minimumGoal -> {
            ProgressStatus(
                title = "Below Minimum Goal",
                message = "Your spending is still below your minimum goal. The blue marker shows that you have not yet reached your planned range.",
                icon = Icons.Rounded.TrendingDown,
                color = BelowMinimumYellow
            )
        }

        amountSpent in minimumGoal..maximumGoal -> {
            ProgressStatus(
                title = "Within Goal Range",
                message = "Your spending is between your minimum and maximum goals. The green progress bar shows that you are currently on track.",
                icon = Icons.Rounded.TrendingFlat,
                color = WithinGoalGreen
            )
        }

        else -> {
            ProgressStatus(
                title = "Above Maximum Goal",
                message = "Your spending has passed your maximum goal. The red progress bar shows that you have exceeded your planned range.",
                icon = Icons.Rounded.TrendingUp,
                color = AboveMaximumRed
            )
        }
    }
}

fun getCategoryIcon(categoryName: String): ImageVector {
    return when (categoryName.lowercase()) {
        "food", "groceries" -> Icons.Rounded.Fastfood
        "transport" -> Icons.Rounded.DirectionsCar
        "entertainment" -> Icons.Rounded.Movie
        "bills" -> Icons.Rounded.ReceiptLong
        else -> Icons.Rounded.Category
    }
}

fun getCategoryColor(categoryName: String): Color {
    return when (categoryName.lowercase()) {
        "food", "groceries" -> Color(0xFFA6E22E)
        "transport" -> Color(0xFF38D6A5)
        "entertainment" -> Color(0xFFB983FF)
        "bills" -> Color(0xFFFF6FB1)
        else -> Color(0xFF1AA3A8)
    }
}

private fun formatRand(amount: Double): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("en", "ZA"))
    return formatter.format(amount)
}

private fun formatShortRand(amount: Double): String {
    return "R${amount.toInt()}"
}