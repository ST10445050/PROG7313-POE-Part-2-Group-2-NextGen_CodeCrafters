package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.theme.PROG7313POEPart2Group2NextGen_CodeCraftersTheme
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.expense.AddExpenseScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PROG7313POEPart2Group2NextGen_CodeCraftersTheme {
                val navController = rememberNavController()


                NavHost(navController = navController, startDestination = "add_expense/1") {

                    // Route for your custom screen
                    composable("add_expense/{userId}") { backStackEntry ->
                        val userId = backStackEntry.arguments?.getString("userId")?.toInt() ?: 1
                        AddExpenseScreen(userId = userId, onSaveSuccess = {
                            navController.navigate("home")
                        })
                    }


                    composable("home") {
                        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                            Greeting(
                                name = "Android",
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PROG7313POEPart2Group2NextGen_CodeCraftersTheme {
        Greeting("Android")
    }
}