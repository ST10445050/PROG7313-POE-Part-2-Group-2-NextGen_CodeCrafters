package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.auth

import android.net.Uri
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.R
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.database.AppDatabase
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun ResetEmailScreen(navController: NavController) {

    var email by remember { mutableStateOf("") }

    val context = LocalContext.current
    val userDao = AppDatabase.getDatabase(context).userDao()
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {

        // 🔥 BACKGROUND
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

            // 🔥 LOGO
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(260.dp)
                    .padding(top = 10.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.fintrack_logo),
                    contentDescription = null,
                    modifier = Modifier.size(240.dp),
                    contentScale = ContentScale.Fit
                )
            }

            // 🔥 TITLE
            Row {
                Text("Fin", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                Text("Track", color = FinTrackMint, fontSize = 32.sp, fontWeight = FontWeight.Bold)
            }

            Text(
                text = "Reset Password",
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Enter your registered email",
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 🔥 EMAIL FIELD
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Email", color = Color.White) },
                singleLine = true,
                leadingIcon = {
                    Icon(Icons.Outlined.Email, contentDescription = null, tint = Color.White)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(30.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = FinTrackMint,
                    unfocusedBorderColor = FinTrackMint,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = FinTrackMint,
                    focusedContainerColor = Color(0x99000000),
                    unfocusedContainerColor = Color(0x99000000),
                    focusedLeadingIconColor = Color.White,
                    unfocusedLeadingIconColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 🔥 BUTTON
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

                                    // ✅ FIX: encode email (prevents crash)
                                    val encodedEmail = Uri.encode(email)
                                    navController.navigate("newPassword/$encodedEmail")

                                } else {
                                    Toast.makeText(
                                        context,
                                        "Incorrect email. Please enter the email you used to register.",
                                        Toast.LENGTH_LONG
                                    ).show()
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
                Text(
                    text = "VERIFY EMAIL",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}