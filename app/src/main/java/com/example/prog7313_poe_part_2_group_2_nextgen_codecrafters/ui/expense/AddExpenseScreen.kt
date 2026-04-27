package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.expense

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.entities.Expense

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(userId: Int, viewModel: ExpenseViewModel, onSaveSuccess: () -> Unit) {
    var description by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Groceries") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        imageUri = uri
    }

    val categories = listOf("Groceries", "Transport", "Entertainment", "Utilities")
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Add New Expense", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(value = amount, onValueChange = { amount = it }, label = { Text("Amount") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())

        // Category Dropdown
        Box {
            OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) { Text(selectedCategory) }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                categories.forEach { cat ->
                    DropdownMenuItem(text = { Text(cat) }, onClick = { selectedCategory = cat; expanded = false })
                }
            }
        }

        OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") }, modifier = Modifier.fillMaxWidth())

        Button(onClick = { launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }, modifier = Modifier.fillMaxWidth()) {
            Text(if (imageUri == null) "Upload Receipt" else "Receipt Selected")
        }

        Button(onClick = {
            if (amount.toDoubleOrNull() != null) {
                viewModel.addExpense(Expense(
                    userId = userId,
                    categoryId = categories.indexOf(selectedCategory) + 1,
                    date = "2026-04-27", // Replace with DatePicker logic
                    startTime = "00:00", // Default value
                    endTime = "00:00",   // Default value
                    description = description,
                    amount = amount.toDouble(),
                    photoPath = imageUri?.toString()
                ))
                onSaveSuccess()
            }
        }, modifier = Modifier.fillMaxWidth()) { Text("ADD EXPENSE") }
    }
}
