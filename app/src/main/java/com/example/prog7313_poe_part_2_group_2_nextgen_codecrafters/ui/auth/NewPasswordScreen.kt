package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.auth

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.database.AppDatabase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun NewPasswordScreen(navController: NavController, email: String) {
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val context = LocalContext.current
    val userDao = AppDatabase.getDatabase(context).userDao()
    val scope = rememberCoroutineScope()

    fun isValidPassword(password: String): Boolean {
        val passwordPattern = Regex(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#\$%^&+=!]).{8,}\$"
        )
        return passwordPattern.matches(password)
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Set New Password")

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("New Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.padding(top = 12.dp)
        )

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm New Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.padding(top = 12.dp)
        )

        Button(
            onClick = {
                Log.d("NewPasswordScreen", "Update password clicked")

                when {
                    password.isBlank() || confirmPassword.isBlank() -> {
                        Toast.makeText(context, "Please fill in both password fields", Toast.LENGTH_SHORT).show()
                    }

                    !isValidPassword(password) -> {
                        Toast.makeText(
                            context,
                            "Password must be 8+ chars, include upper, lower, number & special char",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    password != confirmPassword -> {
                        Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                    }

                    else -> {
                        scope.launch {
                            userDao.updatePassword(email, password)

                            Toast.makeText(
                                context,
                                "Password updated successfully. Please log in with your new password.",
                                Toast.LENGTH_LONG
                            ).show()

                            Log.d("NewPasswordScreen", "Password updated for email: $email")

                            delay(3000)

                            navController.navigate("login") {
                                popUpTo("landing") { inclusive = false }
                            }
                        }
                    }
                }
            },
            modifier = Modifier.padding(top = 20.dp)
        ) {
            Text("UPDATE PASSWORD")
        }
    }
}