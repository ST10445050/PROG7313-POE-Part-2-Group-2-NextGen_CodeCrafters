package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.auth

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun Question4Screen(
    navController: NavController,
    userId: Int,
    employmentStatus: String,
    monthlyIncome: Double,
    categories: String
) {
    val context = LocalContext.current
    var selectedGoal by remember { mutableStateOf("") }

    val options = listOf(
        "Save more money",
        "Reduce spending",
        "Pay off debt",
        "Track my finances better"
    )

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Question 4")
        Text("Main Financial Goal")

        Spacer(modifier = Modifier.height(12.dp))
        Text("What is your main financial goal right now?")
        Spacer(modifier = Modifier.height(20.dp))

        options.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { selectedGoal = option }
                    .padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = selectedGoal == option,
                    onCheckedChange = { selectedGoal = option }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(option)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (selectedGoal.isBlank()) {
                    Toast.makeText(context, "Please select a goal", Toast.LENGTH_SHORT).show()
                } else {
                    val safeEmploymentStatus = Uri.encode(employmentStatus)
                    val safeCategories = Uri.encode(categories)
                    val safeGoal = Uri.encode(selectedGoal)

                    navController.navigate(
                        "question5/$userId/$safeEmploymentStatus/$monthlyIncome/$safeCategories/$safeGoal"
                    )
                }
            }
        ) {
            Text("NEXT")
        }
    }
}