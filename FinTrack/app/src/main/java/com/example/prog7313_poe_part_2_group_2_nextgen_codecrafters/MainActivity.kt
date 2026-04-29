package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.database.AppDatabase
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.reports.CategoryTotalsScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FinTrackApp()
        }
    }
}

@Composable
fun FinTrackApp() {

    // Opens Category screen first
    var currentScreen by remember { mutableStateOf("Categories") }

    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context)
    val expenseDao = database.expenseDao()

    MaterialTheme {
        Scaffold { innerPadding ->

            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                color = MaterialTheme.colorScheme.background
            ) {

                when (currentScreen) {

                    "Home" -> HomeScreen()

                    "Categories" -> CategoryTotalsScreen(
                        userId = 1,
                        expenseDao = expenseDao
                    )

                    "Goals" -> GoalsScreen()
                }
            }
        }
    }
}

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Welcome to FinTrack",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text("Your personal budget tracker")
    }
}

@Composable
fun GoalsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Financial Goals",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("No goals set yet.")
    }
}