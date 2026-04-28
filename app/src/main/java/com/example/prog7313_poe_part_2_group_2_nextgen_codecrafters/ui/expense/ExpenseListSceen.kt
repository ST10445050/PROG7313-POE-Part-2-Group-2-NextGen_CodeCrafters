package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.expense

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseListScreen(
    userId: Int,
    viewModel: ExpenseViewModel,
    navController: NavController
) {
    val expenseList by viewModel.getExpensesForUser(userId)
        .collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            SmallTopAppBar(title = { Text("My Expenses") })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add_expense/$userId") }
            ) {
                Text("+")
            }
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            items(expenseList) { expense ->

                val formattedDate = remember(expense.date) {
                    try {
                        val inputFormat = SimpleDateFormat("d/M/yyyy", Locale.getDefault())
                        val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

                        val parsedDate = inputFormat.parse(expense.date)
                        parsedDate?.let { outputFormat.format(it) } ?: expense.date
                    } catch (e: Exception) {
                        expense.date
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        // TOP ROW: Description + Amount
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = expense.description,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.weight(1f)
                            )

                            Text(
                                text = "R${String.format("%.2f", expense.amount)}",
                                style = MaterialTheme.typography.titleMedium,
                                textAlign = TextAlign.End
                            )
                        }

                        // DATE UNDER AMOUNT (RIGHT ALIGNED)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = formattedDate,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.End
                            )
                        }
                    }
                }
            }
        }
    }
}