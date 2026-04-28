package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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
    var currentScreen by remember { mutableStateOf("Home") }

    MaterialTheme {
        Scaffold(
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                        label = { Text("Home") },
                        selected = currentScreen == "Home",
                        onClick = { currentScreen = "Home" }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.List, contentDescription = "Categories") },
                        label = { Text("Categories") },
                        selected = currentScreen == "Categories",
                        onClick = { currentScreen = "Categories" }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Settings, contentDescription = "Goals") },
                        label = { Text("Goals") },
                        selected = currentScreen == "Goals",
                        onClick = { currentScreen = "Goals" }
                    )
                }
            }
        ) { innerPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                color = MaterialTheme.colorScheme.background
            ) {
                when (currentScreen) {
                    "Home" -> HomeScreen()
                    "Categories" -> CategoriesScreen()
                    "Goals" -> GoalsScreen()
                }
            }
        }
    }
}

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Welcome to FinTrack", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Your personal budget tracker")
    }
}

@Composable
fun CategoriesScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Category Totals", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        // Placeholder for category totals
        Text("Food: R 0.00")
        Text("Transport: R 0.00")
        Text("Entertainment: R 0.00")
    }
}

@Composable
fun GoalsScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Financial Goals", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text("No goals set yet.")
    }
}