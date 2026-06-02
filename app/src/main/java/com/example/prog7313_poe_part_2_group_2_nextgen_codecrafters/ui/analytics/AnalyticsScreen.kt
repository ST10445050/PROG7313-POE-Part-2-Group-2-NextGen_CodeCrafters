package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.analytics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.database.AppDatabase

@Composable
fun AnalyticsScreen(userId: Int = 1) {
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context)
    val expenseDao = database.expenseDao()
    val budgetGoalDao = database.budgetGoalDao()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0E14))
            .padding(16.dp)
    ) {
        Text(
            text = "Analytics",
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Your financial insights and goals",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        AnalyticsBudgetProgressSection(
            userId = userId,
            expenseDao = expenseDao,
            budgetGoalDao = budgetGoalDao
        )
        
        // Add more analytics components here as needed
    }
}
