package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.database.AppDatabase
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.entities.QuestionnaireAnswers
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.entities.User

@Composable
fun DashboardScreen(userId: Int) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)

    var user by remember { mutableStateOf<User?>(null) }
    var answers by remember { mutableStateOf<QuestionnaireAnswers?>(null) }

    LaunchedEffect(userId) {
        user = db.userDao().getUserById(userId)
        answers = db.questionnaireDao().getAnswersByUserId(userId)
    }

    val userName = user?.name ?: "User"

    val monthlyBudget = answers?.monthlyIncome ?: 0.0
    val savingsGoal = answers?.monthlySavingsGoal ?: 0.0
    val spendingCategories = answers?.spendingCategories ?: "General spending"
    val mainCategory = spendingCategories.split(",").firstOrNull() ?: "General spending"

    val usedPercentage = when {
        monthlyBudget <= 0.0 -> 0
        savingsGoal >= monthlyBudget -> 100
        else -> ((savingsGoal / monthlyBudget) * 100).toInt()
    }

    val personalisedMessage = when (answers?.financialGoal) {
        "Save more money" -> "Your dashboard is focused on helping you save consistently."
        "Reduce spending" -> "Your dashboard is focused on reducing unnecessary expenses."
        "Pay off debt" -> "Your dashboard is focused on disciplined spending and debt control."
        "Track my finances better" -> "Your dashboard is focused on improving daily financial tracking."
        else -> "Start tracking your finances to build better money habits."
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = { Text("🏠") },
                    label = { Text("Dashboard") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = { },
                    icon = { Text("💳") },
                    label = { Text("Expenses") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = { },
                    icon = { Text("📁") },
                    label = { Text("Categories") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = { },
                    icon = { Text("⚙️") },
                    label = { Text("Settings") }
                )
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text("FinTrack")

            Spacer(modifier = Modifier.height(16.dp))

            Text("Hello $userName 👋")
            Text("Let’s manage your finances today.")

            Spacer(modifier = Modifier.height(24.dp))

            Text("Monthly Budget Overview")
            Text("Budget: R${monthlyBudget.toInt()}")
            Text("$usedPercentage% Savings Target")
            Text("Savings Goal: R${savingsGoal.toInt()}")
            Text("Estimated Remaining: R${(monthlyBudget - savingsGoal).toInt()}")

            Spacer(modifier = Modifier.height(24.dp))

            Text("Spending Focus")
            Text("Your main focus category is: $mainCategory")
            Text("Other selected categories: $spendingCategories")

            Spacer(modifier = Modifier.height(24.dp))

            Text("Personalised Insight")
            Text(personalisedMessage)

            Spacer(modifier = Modifier.height(24.dp))

            Text("Quick Actions")
            Row {
                Button(onClick = { }) {
                    Text("Add Expense")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(onClick = { }) {
                    Text("View Insights")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Recent Expenses")
            Text("No recent expenses yet.")
        }
    }
}