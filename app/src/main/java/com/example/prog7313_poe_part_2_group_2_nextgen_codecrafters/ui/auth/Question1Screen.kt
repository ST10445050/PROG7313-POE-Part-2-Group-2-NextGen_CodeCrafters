package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.auth

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun Question1Screen(navController: NavController, userId: Int) {
    val context = LocalContext.current
    var selectedOption by remember { mutableStateOf("") }

    val options = listOf(
        "Full-time employed",
        "Part-time employed",
        "Self-employed / Freelancer",
        "Student",
        "Currently unemployed",
        "Prefer not to say"
    )

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Question 1")
        Text("Employment Status")

        Spacer(modifier = Modifier.height(12.dp))
        Text("What best describes your current employment?")
        Spacer(modifier = Modifier.height(20.dp))

        options.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { selectedOption = option }
                    .padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = selectedOption == option,
                    onCheckedChange = { selectedOption = option }
                )

                Spacer(modifier = Modifier.width(8.dp))
                Text(option)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (selectedOption.isBlank()) {
                    Toast.makeText(context, "Please select an option", Toast.LENGTH_SHORT).show()
                } else {
                    val safeEmploymentStatus = Uri.encode(selectedOption)
                    navController.navigate("question2/$userId/$safeEmploymentStatus")
                }
            }
        ) {
            Text("NEXT")
        }

        TextButton(
            onClick = {
                Toast.makeText(context, "Skipping questionnaire", Toast.LENGTH_SHORT).show()
                navController.navigate("dashboard/$userId")
            }
        ) {
            Text("Skip Questionnaire")
        }
    }
}