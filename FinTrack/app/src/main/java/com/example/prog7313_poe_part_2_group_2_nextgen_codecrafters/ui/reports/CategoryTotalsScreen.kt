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

@Composable
fun CategoryTotalsScreen(
    userId: Int,
    expenseDao: ExpenseDao
) {
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }

    var totals by remember {
        mutableStateOf<List<CategoryTotal>>(emptyList())
    }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // Heading
        Text(
            text = "Category Totals",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Track and manage your spending",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Filter Card
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    text = "Filter by Date",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Quick date buttons row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    Button(
                        onClick = {
                            startDate = "2026-04-29"
                            endDate = "2026-04-29"
                        }
                    ) {
                        Text("Today")
                    }

                    Button(
                        onClick = {
                            startDate = "2026-04-22"
                            endDate = "2026-04-29"
                        }
                    ) {
                        Text("This Week")
                    }

                    Button(
                        onClick = {
                            startDate = "2026-04-01"
                            endDate = "2026-04-29"
                        }
                    ) {
                        Text("This Month")
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Custom date section
                Text(
                    text = "Custom Date Range",
                    style = MaterialTheme.typography.titleSmall
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = startDate,
                    onValueChange = { startDate = it },
                    label = { Text("Start Date") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = endDate,
                    onValueChange = { endDate = it },
                    label = { Text("End Date") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                // View totals button
                Button(
                    onClick = {
                        scope.launch {
                            totals = expenseDao.getTotalSpentByCategory(
                                userId,
                                startDate,
                                endDate
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("View Category Totals")
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Results Section
        LazyColumn {
            items(totals) { total ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = total.categoryName,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Total: R${total.totalAmount}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}