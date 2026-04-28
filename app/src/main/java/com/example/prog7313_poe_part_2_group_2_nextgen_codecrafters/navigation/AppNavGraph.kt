package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.expense.AddExpenseScreen
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.expense.ExpenseListScreen
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.expense.ExpenseViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    viewModel: ExpenseViewModel
) {

    NavHost(
        navController = navController,
        startDestination = "expense_list/1" // replace with real logged-in userId later
    ) {

        // -------------------------
        // EXPENSE LIST SCREEN
        // -------------------------
        composable("expense_list/{userId}") { backStackEntry ->
            val userId =
                backStackEntry.arguments?.getString("userId")?.toIntOrNull() ?: 1

            ExpenseListScreen(
                userId = userId,
                viewModel = viewModel,
                navController = navController
            )
        }

        // -------------------------
        // ADD EXPENSE SCREEN
        // -------------------------
        composable("add_expense/{userId}") { backStackEntry ->
            val userId =
                backStackEntry.arguments?.getString("userId")?.toIntOrNull() ?: 1

            AddExpenseScreen(
                userId = userId,
                viewModel = viewModel,
                navController = navController,
                onSaveSuccess = {
                    // Go back to list after saving
                    navController.popBackStack()
                }
            )
        }
    }
}