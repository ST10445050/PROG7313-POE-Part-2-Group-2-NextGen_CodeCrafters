
package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBalanceWallet
import androidx.compose.material.icons.rounded.Category
import androidx.compose.material.icons.rounded.DirectionsCar
import androidx.compose.material.icons.rounded.Fastfood
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.ReceiptLong
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.NumberFormat
import java.util.Locale

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

@Composable
fun BudgetProgressSummarySection(
    monthlyBudget: Double,
    amountSpent: Double,
    spendingItems: List<SpendingSummaryItem>,
    modifier: Modifier = Modifier
) {
    val remaining = (monthlyBudget - amountSpent).coerceAtLeast(0.0)

    val usedPercentage = if (monthlyBudget > 0) {
        ((amountSpent / monthlyBudget) * 100).coerceIn(0.0, 100.0)
    } else {
        0.0
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(FinTrackBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        MonthlyBudgetOverviewCard(
            monthlyBudget = monthlyBudget,
            amountSpent = amountSpent,
            remaining = remaining,
            usedPercentage = usedPercentage
        )

        SpendingSummaryCard(
            monthlyBudget = monthlyBudget,
            spendingItems = spendingItems
        )
    }
}

@Composable
private fun MonthlyBudgetOverviewCard(
    monthlyBudget: Double,
    amountSpent: Double,
    remaining: Double,
    usedPercentage: Double
) {
    val progress = (usedPercentage / 100).toFloat()

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
                            FinTrackCardLight.copy(alpha = 0.95f),
                            FinTrackCard.copy(alpha = 0.98f)
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.AccountBalanceWallet,
                    contentDescription = "Budget Icon",
                    tint = FinTrackAqua
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "Monthly Budget Overview",
                    color = FinTrackText,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = formatRand(monthlyBudget),
                    color = FinTrackText,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold
                )

                Text(
                    text = "${usedPercentage.toInt()}% Used",
                    color = FinTrackAqua,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            GradientProgressBar(
                progress = progress,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(22.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BudgetInfoColumn(
                    title = "Budget",
                    value = formatRand(monthlyBudget)
                )

                BudgetInfoColumn(
                    title = "Amount spent",
                    value = formatRand(amountSpent)
                )

                BudgetInfoColumn(
                    title = "Remaining",
                    value = formatRand(remaining)
                )
            }
        }
    }
}

@Composable
private fun SpendingSummaryCard(
    monthlyBudget: Double,
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
                text = "Spending Summary",
                color = FinTrackText,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(18.dp))

            if (spendingItems.isEmpty()) {
                Text(
                    text = "No spending data available yet.",
                    color = FinTrackMutedText,
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                spendingItems.forEach { item ->
                    SpendingSummaryRow(
                        item = item,
                        monthlyBudget = monthlyBudget
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun SpendingSummaryRow(
    item: SpendingSummaryItem,
    monthlyBudget: Double
) {
    val progress = if (monthlyBudget > 0) {
        (item.amountSpent / monthlyBudget).toFloat().coerceIn(0f, 1f)
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

@Composable
private fun BudgetInfoColumn(
    title: String,
    value: String
) {
    Column {
        Text(
            text = title,
            color = FinTrackMutedText,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = value,
            color = FinTrackText,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun GradientProgressBar(
    progress: Float,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 1000),
        label = "MonthlyBudgetProgressAnimation"
    )

    Box(
        modifier = modifier
            .height(10.dp)
            .clip(RoundedCornerShape(50.dp))
            .background(FinTrackTrack)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(animatedProgress)
                .height(10.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            FinTrackMint,
                            FinTrackAqua,
                            FinTrackTeal
                        )
                    )
                )
        )
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
