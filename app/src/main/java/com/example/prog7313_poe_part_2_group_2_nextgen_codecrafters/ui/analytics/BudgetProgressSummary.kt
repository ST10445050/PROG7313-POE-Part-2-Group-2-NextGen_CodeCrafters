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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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

    val progressColor = when (status) {
        BudgetProgressStatus.BELOW_MINIMUM -> Color(0xFFFFC107)
        BudgetProgressStatus.WITHIN_GOAL -> Color(0xFF4CAF50)
        BudgetProgressStatus.ABOVE_MAXIMUM -> Color(0xFFF44336)
    }

    val statusTitle = when (status) {
        BudgetProgressStatus.BELOW_MINIMUM -> "Below Minimum Goal"
        BudgetProgressStatus.WITHIN_GOAL -> "Within Budget Goal"
        BudgetProgressStatus.ABOVE_MAXIMUM -> "Over Maximum Goal"
    }

    val statusMessage = when (status) {
        BudgetProgressStatus.BELOW_MINIMUM ->
            "You are currently below your minimum spending goal. You still have room in your monthly budget."

        BudgetProgressStatus.WITHIN_GOAL ->
            "Well done! You are staying between your minimum and maximum budget goals. Keep it up."

        BudgetProgressStatus.ABOVE_MAXIMUM ->
            "Warning! You are over your maximum spending goal. Try to reduce your spending for the rest of the month."
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF06121A)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Monthly Budget Progress",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "This shows how well you are staying between your minimum and maximum monthly spending goals.",
                style = MaterialTheme.typography.bodySmall,
                color = Color.LightGray
            )

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(14.dp),
                color = progressColor,
                trackColor = Color(0xFF263238)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Min: R${"%.2f".format(minimumGoal)}",
                    color = Color.LightGray,
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    text = "Max: R${"%.2f".format(maximumGoal)}",
                    color = Color.LightGray,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Text(
                text = "Total Spent: R${"%.2f".format(totalSpent)}",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = progressColor.copy(alpha = 0.18f),
                        shape = RoundedCornerShape(14.dp)
                    )
                    .padding(14.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Column {
                    Text(
                        text = statusTitle,
                        color = progressColor,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = statusMessage,
                        color = Color.White,
                        style = MaterialTheme.typography.bodySmall
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