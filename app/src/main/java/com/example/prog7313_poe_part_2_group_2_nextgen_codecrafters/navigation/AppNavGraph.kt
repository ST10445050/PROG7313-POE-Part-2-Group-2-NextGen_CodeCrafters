package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.auth.LandingScreen
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.auth.LoginScreen
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.auth.NewPasswordScreen
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.auth.RegisterScreen
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.auth.ResetEmailScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "landing"
    ) {
        composable("landing") {
            LandingScreen(navController)
        }

        composable("login") {
            LoginScreen(navController)
        }

        composable("register") {
            RegisterScreen(navController)
        }

        composable("reset") {
            ResetEmailScreen(navController)
        }

        composable("newPassword/{email}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            NewPasswordScreen(navController, email)
        }

        composable("question1") {
            Text("Questionnaire Question 1")
        }

        composable("dashboard") {
            Text("Dashboard Screen")
        }
    }
}