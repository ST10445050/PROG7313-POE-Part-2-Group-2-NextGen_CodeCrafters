package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DirectionsCar
import androidx.compose.material.icons.rounded.Fastfood
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.ReceiptLong
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BudgetGoalScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Budget Goals"
        )

        BudgetProgressSummarySection(
            monthlyBudget = 10000.00,
            amountSpent = 4200.00,
            spendingItems = listOf(
                SpendingSummaryItem(
                    categoryName = "Groceries",
                    amountSpent = 1200.00,
                    icon = Icons.Rounded.Fastfood,
                    progressColor = Color(0xFFA6E22E)
                ),
                SpendingSummaryItem(
                    categoryName = "Transport",
                    amountSpent = 800.00,
                    icon = Icons.Rounded.DirectionsCar,
                    progressColor = Color(0xFF38D6A5)
                ),
                SpendingSummaryItem(
                    categoryName = "Entertainment",
                    amountSpent = 600.00,
                    icon = Icons.Rounded.Movie,
                    progressColor = Color(0xFFB983FF)
                ),
                SpendingSummaryItem(
                    categoryName = "Bills",
                    amountSpent = 1600.00,
                    icon = Icons.Rounded.ReceiptLong,
                    progressColor = Color(0xFFFF6FB1)
                )
            )
        )
    }
}