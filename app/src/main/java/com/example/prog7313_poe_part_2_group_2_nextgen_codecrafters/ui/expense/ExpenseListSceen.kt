package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.expense

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseListScreen(userId: Int, viewModel: ExpenseViewModel) {
    val expenseList by viewModel.getExpensesForUser(userId).collectAsState(initial = emptyList())

    Scaffold(topBar = { SmallTopAppBar(title = { Text("My Expenses") }) }) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(expenseList) { expense ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = expense.description, style = MaterialTheme.typography.titleMedium)
                        Text(text = "Amount: R${String.format("%.2f", expense.amount)}")
                    }
                }
            }
        }
    }
}
