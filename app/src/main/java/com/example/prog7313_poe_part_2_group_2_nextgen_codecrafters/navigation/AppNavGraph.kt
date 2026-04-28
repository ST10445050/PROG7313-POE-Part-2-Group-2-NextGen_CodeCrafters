package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.auth.*
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.dashboard.DashboardScreen
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.categories.CategoryScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "landing"
    ) {

        composable("landing") { LandingScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("reset") { ResetEmailScreen(navController) }

        composable("newPassword/{email}") { backStackEntry ->
            val email = Uri.decode(backStackEntry.arguments?.getString("email") ?: "")
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
            DashboardScreen(navController, userId)
        }


        composable("categories/{userId}") {
            val userId = it.arguments?.getString("userId")?.toIntOrNull() ?: 0
            CategoryScreen(navController, userId)
        }
    }
}