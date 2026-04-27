package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.expense

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.entities.Expense

@Composable
fun AddExpenseScreen(userId: Int, onSaveSuccess: () -> Unit) {
    // State variables as per requirements [cite: 138-147]
    var description by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") } // Use DatePicker for real implementation
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }
    var categoryId by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") })
        OutlinedTextField(value = amount, onValueChange = { amount = it }, label = { Text("Amount") })



        Button(onClick = {

            if (description.isNotBlank() && amount.toDoubleOrNull() != null) {
                val newExpense = Expense(
                    userId = userId,
                    categoryId = categoryId.toIntOrNull() ?: 0,
                    date = date,
                    startTime = startTime,
                    endTime = endTime,
                    description = description,
                    amount = amount.toDouble(),
                    photoPath = null
                )

                onSaveSuccess()
            }
        }) {
            Text("Save Expense")
        }
    }
}