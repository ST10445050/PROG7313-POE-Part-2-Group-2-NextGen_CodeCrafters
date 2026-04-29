package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.budget

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.dao.BudgetGoalDao
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.entities.BudgetGoal
import kotlinx.coroutines.launch

@Composable
fun BudgetGoalScreen(
    userId: Int,
    budgetGoalDao: BudgetGoalDao
) {

    // Updated input variables
    var month by remember { mutableStateOf("") }
    var monthlyBudget by remember { mutableStateOf("") }

    var groceriesLimit by remember { mutableStateOf("") }
    var transportLimit by remember { mutableStateOf("") }
    var entertainmentLimit by remember { mutableStateOf("") }
    var billsLimit by remember { mutableStateOf("") }
    var clothingLimit by remember { mutableStateOf("") }

    var savedGoal by remember { mutableStateOf<BudgetGoal?>(null) }

    val scope = rememberCoroutineScope()

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

                OutlinedTextField(
                    value = month,
                    onValueChange = { month = it },
                    label = { Text("Enter Month") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = monthlyBudget,
                    onValueChange = { monthlyBudget = it },
                    label = { Text("Enter Monthly Budget") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("Category Limits")

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = groceriesLimit,
                    onValueChange = { groceriesLimit = it },
                    label = { Text("Groceries Limit") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = transportLimit,
                    onValueChange = { transportLimit = it },
                    label = { Text("Transport Limit") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = entertainmentLimit,
                    onValueChange = { entertainmentLimit = it },
                    label = { Text("Entertainment Limit") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = billsLimit,
                    onValueChange = { billsLimit = it },
                    label = { Text("Bills Limit") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = clothingLimit,
                    onValueChange = { clothingLimit = it },
                    label = { Text("Clothing Limit") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        scope.launch {
                            val goal = BudgetGoal(
                                userId = userId,
                                month = month.toInt(),
                                year = 2026, // placeholder since year field removed
                                minimumGoal = monthlyBudget.toDouble(),
                                maximumGoal = monthlyBudget.toDouble()
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

        savedGoal?.let {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Saved Budget Goal")
                    Text("Month: ${it.month}")
                    Text("Monthly Budget: R${it.minimumGoal}")
                }
            }
        }
    }
}