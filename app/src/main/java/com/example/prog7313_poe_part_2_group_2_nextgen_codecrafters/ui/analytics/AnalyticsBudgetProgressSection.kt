package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.analytics

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.dao.BudgetGoalDao
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.dao.ExpenseDao
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun AnalyticsBudgetProgressSection(
    userId: Int,
    expenseDao: ExpenseDao,
    budgetGoalDao: BudgetGoalDao,
    modifier: Modifier = Modifier
) {
    val today = LocalDate.now()

    val startDate = today
        .minusMonths(1)
        .format(DateTimeFormatter.ISO_DATE)

    val endDate = today
        .format(DateTimeFormatter.ISO_DATE)

    val currentMonth = today.monthValue
    val currentYear = today.year

    val totalSpent by expenseDao
        .getTotalSpendingForPeriod(
            userId = userId,
            startDate = startDate,
            endDate = endDate
        )
        .collectAsState(initial = 0.0)

    val budgetGoal by budgetGoalDao
        .getBudgetGoalForMonth(
            userId = userId,
            month = currentMonth,
            year = currentYear
        )
        .collectAsState(initial = null)

    if (budgetGoal == null) {
        BudgetProgressSummary(
            totalSpent = totalSpent ?: 0.0,
            minimumGoal = 0.0,
            maximumGoal = 0.0,
            modifier = modifier
        )
    } else {
        BudgetProgressSummary(
            totalSpent = totalSpent ?: 0.0,
            minimumGoal = budgetGoal!!.minimumGoal,
            maximumGoal = budgetGoal!!.maximumGoal,
            modifier = modifier
        )
    }
}