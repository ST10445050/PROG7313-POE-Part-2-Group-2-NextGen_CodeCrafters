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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.database.AppDatabase
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val context = LocalContext.current
    val userDao = AppDatabase.getDatabase(context).userDao()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Welcome back")

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.padding(top = 16.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.padding(top = 12.dp)
        )

        Button(
            onClick = {
                Log.d("LoginScreen", "Login button clicked")

                when {
                    username.isBlank() || password.isBlank() -> {
                        Toast.makeText(context, "Please enter username and password", Toast.LENGTH_SHORT).show()
                        Log.e("LoginScreen", "Validation failed: empty username or password")
                    }

                    else -> {
                        scope.launch {
                            val user = userDao.loginUser(username, password)

                            if (user != null) {
                                Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                                Log.d("LoginScreen", "Login successful for user: ${user.username}")

                                navController.navigate("dashboard")
                            } else {
                                Toast.makeText(context, "Invalid username or password", Toast.LENGTH_SHORT).show()
                                Log.e("LoginScreen", "Login failed: invalid credentials")
                            }
                        }
                    }
                }
            },
            modifier = Modifier.padding(top = 20.dp)
        ) {
            Text("LOGIN")
        }

        TextButton(
            onClick = {
                Log.d("LoginScreen", "Forgot Password clicked")
                navController.navigate("reset")
            }
        ) {
            Text("Forgot Password?")
        }
    }
}