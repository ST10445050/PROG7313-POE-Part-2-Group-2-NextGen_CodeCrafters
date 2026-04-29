package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.auth

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.R
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.database.AppDatabase
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun NewPasswordScreen(navController: NavController, email: String) {

    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val userDao = AppDatabase.getDatabase(context).userDao()
    val scope = rememberCoroutineScope()

    fun isValidPassword(password: String): Boolean {
        val passwordPattern = Regex(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#\$%^&+=!]).{8,}\$"
        )
        return passwordPattern.matches(password)
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.drawable.fintrack_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp)
                .padding(top = 120.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.fintrack_logo),
                contentDescription = null,
                modifier = Modifier.size(250.dp),
                contentScale = ContentScale.Fit
            )

            Row {
                Text("Fin", color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.Bold)
                Text("Track", color = FinTrackMint, fontSize = 36.sp, fontWeight = FontWeight.Bold)
            }

            Text(
                text = "Set New Password",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            PasswordField(
                value = password,
                onValueChange = { password = it },
                placeholder = "New Password",
                passwordVisible = passwordVisible,
                onToggleVisibility = { passwordVisible = !passwordVisible }
            )

            PasswordField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholder = "Confirm New Password",
                passwordVisible = confirmPasswordVisible,
                onToggleVisibility = { confirmPasswordVisible = !confirmPasswordVisible }
            )

            Spacer(modifier = Modifier.height(20.dp))

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

                                delay(3000)

                                navController.navigate("login") {
                                    popUpTo("landing") { inclusive = false }
                                }
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(20.dp, RoundedCornerShape(30.dp)),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = FinTrackLime,
                    contentColor = FinTrackNavy
                )
            ) {
                Text("UPDATE PASSWORD", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    passwordVisible: Boolean,
    onToggleVisibility: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(placeholder, color = Color.White, fontWeight = FontWeight.Medium)
        },
        textStyle = LocalTextStyle.current.copy(
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium
        ),
        singleLine = true,
        leadingIcon = {
            Icon(Icons.Outlined.Lock, contentDescription = null, tint = Color.White)
        },
        trailingIcon = {
            IconButton(onClick = onToggleVisibility) {
                Icon(
                    imageVector = if (passwordVisible)
                        Icons.Outlined.VisibilityOff
                    else
                        Icons.Outlined.Visibility,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        },
        visualTransformation = if (passwordVisible)
            VisualTransformation.None
        else
            PasswordVisualTransformation(),
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(bottom = 10.dp),
        shape = RoundedCornerShape(30.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = FinTrackMint,
            unfocusedBorderColor = FinTrackMint,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedPlaceholderColor = Color.White,
            unfocusedPlaceholderColor = Color.White,
            cursorColor = FinTrackMint,
            focusedContainerColor = Color(0x99000000),
            unfocusedContainerColor = Color(0x99000000),
            focusedLeadingIconColor = Color.White,
            unfocusedLeadingIconColor = Color.White,
            focusedTrailingIconColor = Color.White,
            unfocusedTrailingIconColor = Color.White
        )
    )
}