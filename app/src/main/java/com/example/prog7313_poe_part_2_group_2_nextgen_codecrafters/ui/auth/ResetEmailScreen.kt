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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.database.AppDatabase
import kotlinx.coroutines.launch

@Composable
fun ResetEmailScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }

    val context = LocalContext.current
    val userDao = AppDatabase.getDatabase(context).userDao()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Reset Password")

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Enter your registered email") },
            modifier = Modifier.padding(top = 16.dp)
        )

        Button(
            onClick = {
                Log.d("ResetEmailScreen", "Verify email clicked")

                when {
                    email.isBlank() -> {
                        Toast.makeText(context, "Please enter your email", Toast.LENGTH_SHORT).show()
                    }

                    !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                        Toast.makeText(context, "Invalid email format", Toast.LENGTH_SHORT).show()
                    }

                    else -> {
                        scope.launch {
                            val user = userDao.getUserByEmail(email)

                            if (user != null) {
                                Toast.makeText(
                                    context,
                                    "Email verified. You may now reset your password.",
                                    Toast.LENGTH_LONG
                                ).show()
                                Log.d("ResetEmailScreen", "Email verified: $email")
                                navController.navigate("newPassword/$email")
                            } else {
                                Toast.makeText(
                                    context,
                                    "Invalid email. Please enter the email used to register this account.",
                                    Toast.LENGTH_LONG
                                ).show()
                                Log.e("ResetEmailScreen", "Email not found: $email")
                            }
                        }
                    }
                }
            },
            modifier = Modifier.padding(top = 20.dp)
        ) {
            Text("VERIFY EMAIL")
        }
    }
}