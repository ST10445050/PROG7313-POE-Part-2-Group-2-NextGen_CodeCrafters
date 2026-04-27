package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.auth

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun Question2Screen(
    navController: NavController,
    userId: Int,
    employmentStatus: String
) {
    val context = LocalContext.current
    var income by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Question 2")
        Text("Monthly Income")

        Spacer(modifier = Modifier.height(12.dp))
        Text("What is your average monthly income?")
        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = income,
            onValueChange = { income = it },
            label = { Text("R amount") }
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text("(Optional note: You can add multiple income sources later)")
        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                val incomeValue = income.toDoubleOrNull()

                if (income.isBlank() || incomeValue == null || incomeValue < 0) {
                    Toast.makeText(context, "Enter a valid amount", Toast.LENGTH_SHORT).show()
                } else {
                    val safeEmploymentStatus = Uri.encode(employmentStatus)
                    navController.navigate("question3/$userId/$safeEmploymentStatus/$incomeValue")
                }
            }
        ) {
            Text("NEXT")
        }
    }
}