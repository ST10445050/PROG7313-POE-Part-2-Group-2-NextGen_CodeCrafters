package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.budget

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.dao.BudgetGoalDao
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.entities.BudgetGoal
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetGoalScreen(
    userId: Int,
    budgetGoalDao: BudgetGoalDao
) {

    // Updated variables
    var selectedMonth by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    var minimumBudget by remember { mutableStateOf("") }
    var maximumBudget by remember { mutableStateOf("") }

    var savedGoal by remember { mutableStateOf<BudgetGoal?>(null) }

    val scope = rememberCoroutineScope()

    val months = listOf(
        "January", "February", "March", "April",
        "May", "June", "July", "August",
        "September", "October", "November", "December"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Budget Goals",
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = "Set spending goals to stay in control of your finances"
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text("Monthly Budget")

                Spacer(modifier = Modifier.height(12.dp))

                // Month dropdown
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedMonth,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Select Month") },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        months.forEach { month ->
                            DropdownMenuItem(
                                text = { Text(month) },
                                onClick = {
                                    selectedMonth = month
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Minimum budget
                OutlinedTextField(
                    value = minimumBudget,
                    onValueChange = { minimumBudget = it },
                    label = { Text("Minimum Monthly Budget") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Maximum budget
                OutlinedTextField(
                    value = maximumBudget,
                    onValueChange = { maximumBudget = it },
                    label = { Text("Maximum Monthly Budget") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        scope.launch {
                            val goal = BudgetGoal(
                                userId = userId,
                                month = months.indexOf(selectedMonth) + 1,
                                year = 2026,
                                minimumGoal = minimumBudget.toDouble(),
                                maximumGoal = maximumBudget.toDouble()
                            )

                            budgetGoalDao.insertBudgetGoal(goal)
                            savedGoal = goal
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Goals")
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Saved Goal Display
        savedGoal?.let {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Saved Budget Goal")
                    Text("Month: $selectedMonth")
                    Text("Minimum Budget: R${it.minimumGoal}")
                    Text("Maximum Budget: R${it.maximumGoal}")
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Budget Insights
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Budget Insights")

                Spacer(modifier = Modifier.height(8.dp))

                Text("• Stay above your minimum budget target")
                Text("• Avoid exceeding your maximum monthly budget")
                Text("• Review weekly expenses to stay on track")
            }
        }
    }
}