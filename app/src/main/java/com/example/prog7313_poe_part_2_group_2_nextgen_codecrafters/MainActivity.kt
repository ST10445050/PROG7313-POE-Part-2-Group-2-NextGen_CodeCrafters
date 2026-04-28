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
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.database.ExpenseDatabase
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.expense.AddExpenseScreen
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.expense.ExpenseListScreen
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.expense.ExpenseViewModel
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.theme.PROG7313POEPart2Group2NextGen_CodeCraftersTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = ExpenseDatabase.getDatabase(applicationContext)
        val viewModel = ExpenseViewModel(application, db.expenseDao())

        enableEdgeToEdge()
        setContent {
            PROG7313POEPart2Group2NextGen_CodeCraftersTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "add_expense/1") {

                    composable("add_expense/{userId}") { backStackEntry ->
                        val userId = backStackEntry.arguments?.getString("userId")?.toIntOrNull() ?: 1
                        AddExpenseScreen(
                            userId = userId,
                            viewModel = viewModel,
                            navController = navController, // Pass the controller
                            onSaveSuccess = { navController.navigate("expense_list/$userId") }
                        )
                    }

                    composable("expense_list/{userId}") { backStackEntry ->
                        val userId = backStackEntry.arguments?.getString("userId")?.toIntOrNull() ?: 1
                        ExpenseListScreen(
                            userId = userId,
                            viewModel = viewModel,
                            navController = navController
                        )
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
