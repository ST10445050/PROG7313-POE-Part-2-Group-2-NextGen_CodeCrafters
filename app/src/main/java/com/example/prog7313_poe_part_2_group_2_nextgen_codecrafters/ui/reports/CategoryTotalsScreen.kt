package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.reports

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.dao.ExpenseDao
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.model.CategoryTotal
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun CategoryTotalsScreen(
    userId: Int,
    expenseDao: ExpenseDao
) {
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var totals by remember { mutableStateOf(listOf<CategoryTotal>()) }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Category Totals",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Today button
        Button(onClick = {
            val today = LocalDate.now().toString()

            scope.launch {
                totals = expenseDao.getTotalSpentByCategory(
                    userId,
                    today,
                    today
                )
            }
        }) {
            Text("Today")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // This Week button
        Button(onClick = {
            val today = LocalDate.now()
            val startOfWeek = today.minusDays(7).toString()

            scope.launch {
                totals = expenseDao.getTotalSpentByCategory(
                    userId,
                    startOfWeek,
                    today.toString()
                )
            }
        }) {
            Text("This Week")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // This Month button
        Button(onClick = {
            val today = LocalDate.now()
            val startOfMonth = today.withDayOfMonth(1).toString()

            scope.launch {
                totals = expenseDao.getTotalSpentByCategory(
                    userId,
                    startOfMonth,
                    today.toString()
                )
            }
        }) {
            Text("This Month")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Custom Date Range")

        OutlinedTextField(
            value = startDate,
            onValueChange = { startDate = it },
            label = { Text("Start Date") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = endDate,
            onValueChange = { endDate = it },
            label = { Text("End Date") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            scope.launch {
                totals = expenseDao.getTotalSpentByCategory(
                    userId,
                    startDate,
                    endDate
                )
            }
        }) {
            Text("Custom")
        }

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn {
            items(totals) { total ->
                Text(
                    text = "${total.categoryName}: R${total.totalAmount}"
                )
            }
        }
    }
}