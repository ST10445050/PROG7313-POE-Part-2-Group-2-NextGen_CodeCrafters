package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.auth

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.database.AppDatabase
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.entities.QuestionnaireAnswers
import kotlinx.coroutines.launch

@Composable
fun Question5Screen(
    navController: NavController,
    userId: Int,
    employmentStatus: String,
    monthlyIncome: Double,
    categories: String,
    financialGoal: String
) {
    val context = LocalContext.current
    val questionnaireDao = AppDatabase.getDatabase(context).questionnaireDao()
    val scope = rememberCoroutineScope()

    var savingsGoal by remember { mutableStateOf(5000f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Question 5")
        Text("Monthly Savings Goal")

        Spacer(modifier = Modifier.height(12.dp))

        Text("How much would you like to save each month?")

        Spacer(modifier = Modifier.height(20.dp))

        Text("R${savingsGoal.toInt()}")

        Slider(
            value = savingsGoal,
            onValueChange = { savingsGoal = it },
            valueRange = 0f..10000f,
            steps = 9,
            modifier = Modifier.padding(top = 12.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("R0")
            Text("R10 000+")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                scope.launch {
                    val answers = QuestionnaireAnswers(
                        userId = userId,
                        employmentStatus = employmentStatus,
                        monthlyIncome = monthlyIncome,
                        spendingCategories = categories,
                        financialGoal = financialGoal,
                        monthlySavingsGoal = savingsGoal.toDouble(),
                        dashboardType = "personalized"
                    )

                    questionnaireDao.deleteAnswersByUserId(userId)
                    questionnaireDao.insertAnswers(answers)

                    Toast.makeText(context, "Questionnaire completed successfully", Toast.LENGTH_SHORT).show()
                    Log.d("Question5Screen", "Saved questionnaire answers for user ID: $userId")

                    navController.navigate("dashboard/$userId")
                }
            }
        ) {
            Text("NEXT")
        }
    }
}