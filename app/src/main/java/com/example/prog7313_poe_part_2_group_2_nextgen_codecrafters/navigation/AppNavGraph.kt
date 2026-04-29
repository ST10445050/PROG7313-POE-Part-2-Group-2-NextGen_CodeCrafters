package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.navigation

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.database.AppDatabase
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.auth.*
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.categories.CategoryScreen
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.dashboard.DashboardScreen
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.expense.AddExpenseScreen
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.expense.ExpenseListScreen
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.expense.ExpenseViewModel
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.budget.BudgetGoalScreen
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.reports.CategoryTotalsScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)


    val expenseViewModel = remember {
        ExpenseViewModel(
            context.applicationContext as Application,
            db.expenseDao()
        )
    }

    NavHost(
        navController = navController,
        startDestination = "landing"
    ) {
        composable("landing") { LandingScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("reset") { ResetEmailScreen(navController) }

        composable("newPassword/{email}") {
            val email = Uri.decode(it.arguments?.getString("email") ?: "")
            NewPasswordScreen(navController, email)
        }

        composable("question1/{userId}") {
            val userId = it.arguments?.getString("userId")?.toIntOrNull() ?: 0
            Question1Screen(navController, userId)
        }

        composable("question2/{userId}/{employmentStatus}") {
            val userId = it.arguments?.getString("userId")?.toIntOrNull() ?: 0
            val status = Uri.decode(it.arguments?.getString("employmentStatus") ?: "")
            Question2Screen(navController, userId, status)
        }

        composable("question3/{userId}/{employmentStatus}/{monthlyIncome}") {
            val userId = it.arguments?.getString("userId")?.toIntOrNull() ?: 0
            val status = Uri.decode(it.arguments?.getString("employmentStatus") ?: "")
            val income = it.arguments?.getString("monthlyIncome")?.toDoubleOrNull() ?: 0.0
            Question3Screen(navController, userId, status, income)
        }

        composable("question4/{userId}/{employmentStatus}/{monthlyIncome}/{categories}") {
            val userId = it.arguments?.getString("userId")?.toIntOrNull() ?: 0
            val status = Uri.decode(it.arguments?.getString("employmentStatus") ?: "")
            val income = it.arguments?.getString("monthlyIncome")?.toDoubleOrNull() ?: 0.0
            val categories = Uri.decode(it.arguments?.getString("categories") ?: "")
            Question4Screen(navController, userId, status, income, categories)
        }

        composable("question5/{userId}/{employmentStatus}/{monthlyIncome}/{categories}/{financialGoal}") {
            val userId = it.arguments?.getString("userId")?.toIntOrNull() ?: 0
            val status = Uri.decode(it.arguments?.getString("employmentStatus") ?: "")
            val income = it.arguments?.getString("monthlyIncome")?.toDoubleOrNull() ?: 0.0
            val categories = Uri.decode(it.arguments?.getString("categories") ?: "")
            val goal = Uri.decode(it.arguments?.getString("financialGoal") ?: "")

            Question5Screen(navController, userId, status, income, categories, goal)
        }

        composable("dashboard/{userId}") {
            val userId = it.arguments?.getString("userId")?.toIntOrNull() ?: 0
            DashboardScreen(
                navController = navController,
                userId = userId,
                expenseViewModel = expenseViewModel
            )
        }

        composable("categories/{userId}") {
            val userId = it.arguments?.getString("userId")?.toIntOrNull() ?: 0
            CategoryScreen(navController, userId)
        }
        composable("category_totals/{userId}") {
            val userId = it.arguments?.getString("userId")?.toIntOrNull() ?: 0

            CategoryTotalsScreen(
                userId = userId,
                expenseDao = db.expenseDao(),
                navController = navController
            )
        }

        composable("budget_goals/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments
                ?.getString("userId")
                ?.toIntOrNull() ?: 0

            BudgetGoalScreen(
                userId = userId,
                budgetGoalDao = db.budgetGoalDao(),
                expenseDao = db.expenseDao(),
                navController = navController
            )
        }

        composable("expense_list/{userId}") {
            val userId = it.arguments?.getString("userId")?.toIntOrNull() ?: 0
            ExpenseListScreen(userId, expenseViewModel, navController)
        }

        composable("add_expense/{userId}") {
            val userId = it.arguments?.getString("userId")?.toIntOrNull() ?: 0
            AddExpenseScreen(
                userId = userId,
                viewModel = expenseViewModel,
                navController = navController,
                onSaveSuccess = {
                    navController.navigate("expense_list/$userId") {
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}