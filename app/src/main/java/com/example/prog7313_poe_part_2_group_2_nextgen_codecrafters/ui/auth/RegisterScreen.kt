package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.auth

import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.entities.User
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val context = LocalContext.current
    val userDao = AppDatabase.getDatabase(context).userDao()
    val scope = rememberCoroutineScope()

    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPassword(password: String): Boolean {
        val passwordPattern = Regex(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#\$%^&+=!]).{8,}\$"
        )
        return passwordPattern.matches(password)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Create Your Account")

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.padding(top = 12.dp)
        )

        OutlinedTextField(
            value = surname,
            onValueChange = { surname = it },
            label = { Text("Surname") },
            modifier = Modifier.padding(top = 12.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.padding(top = 12.dp)
        )

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.padding(top = 12.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.padding(top = 12.dp)
        )

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.padding(top = 12.dp)
        )

        Button(
            onClick = {
                Log.d("RegisterScreen", "Register button clicked")

                when {
                    name.isBlank() || surname.isBlank() || email.isBlank() ||
                            username.isBlank() || password.isBlank() || confirmPassword.isBlank() -> {
                        Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                        Log.e("RegisterScreen", "Validation failed: empty fields")
                    }

                    !isValidEmail(email) -> {
                        Toast.makeText(context, "Invalid email format", Toast.LENGTH_SHORT).show()
                        Log.e("RegisterScreen", "Validation failed: invalid email format")
                    }

                    !isValidPassword(password) -> {
                        Toast.makeText(
                            context,
                            "Password must be 8+ chars, include upper, lower, number & special char",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.e("RegisterScreen", "Validation failed: weak password")
                    }

                    password != confirmPassword -> {
                        Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                        Log.e("RegisterScreen", "Validation failed: passwords do not match")
                    }

                    else -> {
                        scope.launch {
                            val existingUser = userDao.getUserByUsername(username)
                            val existingEmail = userDao.getUserByEmail(email)

                            when {
                                existingUser != null -> {
                                    Toast.makeText(context, "Username already exists", Toast.LENGTH_SHORT).show()
                                    Log.e("RegisterScreen", "Username already exists")
                                }

                                existingEmail != null -> {
                                    Toast.makeText(context, "Email already exists", Toast.LENGTH_SHORT).show()
                                    Log.e("RegisterScreen", "Email already exists")
                                }

                                else -> {
                                    val user = User(
                                        name = name,
                                        surname = surname,
                                        email = email,
                                        username = username,
                                        password = password
                                    )

                                    val newUserId = userDao.insertUser(user).toInt()

                                    Toast.makeText(context, "Account successfully created", Toast.LENGTH_SHORT).show()
                                    Log.d("RegisterScreen", "User registered successfully with ID: $newUserId")

                                    navController.navigate("question1/$newUserId")
                                }
                            }
                        }
                    }
                }
            },
            modifier = Modifier.padding(top = 20.dp)
        ) {
            Text("REGISTER")
        }
    }
}