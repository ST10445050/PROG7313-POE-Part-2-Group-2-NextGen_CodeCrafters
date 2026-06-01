package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.analytics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.min

@Composable
fun BudgetProgressSummary(
    totalSpent: Double,
    minimumGoal: Double,
    maximumGoal: Double,
    modifier: Modifier = Modifier
) {
    val progress = if (maximumGoal > 0) {
        min((totalSpent / maximumGoal).toFloat(), 1f)
    } else {
        0f
    }

    val status = when {
        totalSpent < minimumGoal -> BudgetProgressStatus.BELOW_MINIMUM
        totalSpent <= maximumGoal -> BudgetProgressStatus.WITHIN_GOAL
        else -> BudgetProgressStatus.ABOVE_MAXIMUM
    }

    // Color coordination: Yellow (Below), Green (Within), Red (Above)
    val progressColor = when (status) {
        BudgetProgressStatus.BELOW_MINIMUM -> Color(0xFFFFC107) // Yellow
        BudgetProgressStatus.WITHIN_GOAL -> Color(0xFF4CAF50)    // Green
        BudgetProgressStatus.ABOVE_MAXIMUM -> Color(0xFFF44336)  // Red
    }

    val statusTitle = when (status) {
        BudgetProgressStatus.BELOW_MINIMUM -> "Below Minimum Goal"
        BudgetProgressStatus.WITHIN_GOAL -> "Within Budget Goal"
        BudgetProgressStatus.ABOVE_MAXIMUM -> "Over Maximum Goal"
    }

    // Exact messages as requested by the user
    val statusMessage = when (status) {
        BudgetProgressStatus.BELOW_MINIMUM ->
            "You are currently below your minimum spending goal. You still have room in your monthly budget."

        BudgetProgressStatus.WITHIN_GOAL ->
            "Congratulations, you are doing well, you are keeping track, and you are falling in between your budgeting goals. Well done, keep it up."

        BudgetProgressStatus.ABOVE_MAXIMUM ->
            "Warning, you are currently over your minimum goal, you need to reduce your spending, etc."
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0D1B2A) // Deep navy/dark background
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Monthly Budget Progress",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Visual summary of your spending performance for the month.",
                style = MaterialTheme.typography.bodySmall,
                color = Color.LightGray
            )

            // The Progress Bar
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp),
                color = progressColor,
                trackColor = Color(0xFF1B263B),
                strokeCap = StrokeCap.Round
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Min Goal",
                        color = Color.Gray,
                        style = MaterialTheme.typography.labelSmall
                    )
                    Text(
                        text = "R${"%.2f".format(minimumGoal)}",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Max Goal",
                        color = Color.Gray,
                        style = MaterialTheme.typography.labelSmall
                    )
                    Text(
                        text = "R${"%.2f".format(maximumGoal)}",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total Spent: ",
                    color = Color.LightGray,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "R${"%.2f".format(totalSpent)}",
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            // Status message box with color-coded feedback
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = progressColor.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(16.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(progressColor, RoundedCornerShape(50))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = statusTitle,
                            color = progressColor,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = statusMessage,
                        color = Color.White,
                        style = MaterialTheme.typography.bodySmall,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}

enum class BudgetProgressStatus {
    BELOW_MINIMUM,
    WITHIN_GOAL,
    ABOVE_MAXIMUM
}
