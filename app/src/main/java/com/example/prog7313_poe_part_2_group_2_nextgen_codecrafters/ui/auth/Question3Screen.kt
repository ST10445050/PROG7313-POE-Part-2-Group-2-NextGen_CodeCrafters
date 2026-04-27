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
fun Question3Screen(
    navController: NavController,
    userId: Int,
    employmentStatus: String,
    monthlyIncome: Double
) {
    val context = LocalContext.current

    val options = listOf(
        "Groceries",
        "Rent / Housing",
        "Transport",
        "Eating out",
        "Shopping",
        "Subscriptions",
        "Other"
    )

    var selectedCategories by remember { mutableStateOf(listOf<String>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Question 3")
        Text("Biggest Spending Categories")

        Spacer(modifier = Modifier.height(12.dp))

        Text("What do you spend most of your money on? (Select up to 3)")

        Spacer(modifier = Modifier.height(20.dp))

        options.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        selectedCategories =
                            if (selectedCategories.contains(option)) {
                                selectedCategories - option
                            } else {
                                if (selectedCategories.size < 3) {
                                    selectedCategories + option
                                } else {
                                    Toast.makeText(
                                        context,
                                        "You can select up to 3 categories",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    selectedCategories
                                }
                            }
                    }
                    .padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = selectedCategories.contains(option),
                    onCheckedChange = { checked ->
                        selectedCategories =
                            if (checked) {
                                if (selectedCategories.size < 3) {
                                    selectedCategories + option
                                } else {
                                    Toast.makeText(
                                        context,
                                        "You can select up to 3 categories",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    selectedCategories
                                }
                            } else {
                                selectedCategories - option
                            }
                    }
                )

                Spacer(modifier = Modifier.width(8.dp))
                Text(option)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (selectedCategories.isEmpty()) {
                    Toast.makeText(
                        context,
                        "Please select at least one category",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val safeEmploymentStatus = Uri.encode(employmentStatus)
                    val safeCategories = Uri.encode(selectedCategories.joinToString(","))

                    navController.navigate(
                        "question4/$userId/$safeEmploymentStatus/$monthlyIncome/$safeCategories"
                    )
                }
            }
        ) {
            Text("NEXT")
        }
    }
}