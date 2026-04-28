package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.auth

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.R
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.auth.Question1Screen
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.database.AppDatabase
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.entities.User
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val userDao = AppDatabase.getDatabase(context).userDao()
    val scope = rememberCoroutineScope()

    fun isValidPassword(password: String): Boolean {
        val pattern = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#\$%^&+=!]).{8,}$")
        return pattern.matches(password)
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 95.dp, bottom = 45.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.fintrack_logo),
                contentDescription = null,
                modifier = Modifier.size(300.dp),
                contentScale = ContentScale.Fit
            )

            Row {
                Text("Fin", color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.Bold)
                Text("Track", color = FinTrackMint, fontSize = 36.sp, fontWeight = FontWeight.Bold)
            }

            Text(
                text = "Create your account",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(18.dp))

            RegisterField(name, { name = it }, "Name", Icons.Outlined.Person)
            RegisterField(surname, { surname = it }, "Surname", Icons.Outlined.Person)
            RegisterField(email, { email = it }, "Email", Icons.Outlined.Email)

            GenderDropdown(
                selectedGender = gender,
                onGenderSelected = { gender = it }
            )

            RegisterField(phone, { phone = it }, "Phone Number", Icons.Outlined.Call)
            RegisterField(username, { username = it }, "Username", Icons.Outlined.Person)

            RegisterField(
                value = password,
                onValueChange = { password = it },
                placeholder = "Password",
                icon = Icons.Outlined.Lock,
                isPassword = true,
                passwordVisible = passwordVisible,
                onToggleVisibility = { passwordVisible = !passwordVisible }
            )

            RegisterField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholder = "Confirm Password",
                icon = Icons.Outlined.Lock,
                isPassword = true,
                passwordVisible = confirmPasswordVisible,
                onToggleVisibility = { confirmPasswordVisible = !confirmPasswordVisible }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    when {
                        name.isBlank() || surname.isBlank() || email.isBlank() ||
                                gender.isBlank() || phone.isBlank() ||
                                username.isBlank() || password.isBlank() || confirmPassword.isBlank() -> {
                            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                        }

                        !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                            Toast.makeText(context, "Email must contain @ and be valid", Toast.LENGTH_LONG).show()
                        }

                        !isValidPassword(password) -> {
                            Toast.makeText(
                                context,
                                "Password must be 8+ characters with uppercase, lowercase, number and special character",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        password != confirmPassword -> {
                            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                        }

                        else -> {
                            scope.launch {

                                val newUserId = userDao.insertUser(
                                    User(
                                        name = name,
                                        surname = surname,
                                        email = email,
                                        gender = gender,
                                        phone = phone,
                                        username = username,
                                        password = password
                                    )
                                )

                                Toast.makeText(context, "Registered successfully", Toast.LENGTH_SHORT).show()

                                navController.navigate("question1/$newUserId")
                            }
                        }

                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
                    .shadow(18.dp, RoundedCornerShape(30.dp)),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = FinTrackLime,
                    contentColor = FinTrackNavy
                )
            ) {
                Text("REGISTER", fontWeight = FontWeight.Bold)
            }

            Text(
                text = "Your financial journey starts here.",
                color = FinTrackMint,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 10.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenderDropdown(
    selectedGender: String,
    onGenderSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val genders = listOf("Male", "Female", "Other")

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedGender,
            onValueChange = {},
            readOnly = true,
            placeholder = {
                Text(
                    text = "Gender",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            },
            leadingIcon = {
                Box(
                    modifier = Modifier.width(38.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Outlined.Person, contentDescription = null, tint = Color.White)
                }
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
                .height(64.dp)
                .padding(bottom = 14.dp),
            shape = RoundedCornerShape(30.dp),
            textStyle = LocalTextStyle.current.copy(
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = FinTrackMint,
                unfocusedBorderColor = FinTrackMint,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedPlaceholderColor = Color.White,
                unfocusedPlaceholderColor = Color.White,
                focusedContainerColor = Color(0x99000000),
                unfocusedContainerColor = Color(0x99000000),
                focusedLeadingIconColor = Color.White,
                unfocusedLeadingIconColor = Color.White,
                focusedTrailingIconColor = Color.White,
                unfocusedTrailingIconColor = Color.White
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            genders.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        onGenderSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun RegisterField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: ImageVector,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onToggleVisibility: (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        },
        textStyle = LocalTextStyle.current.copy(
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        ),
        singleLine = true,
        leadingIcon = {
            Box(
                modifier = Modifier.width(38.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = Color.White)
            }
        },
        trailingIcon = if (isPassword) {
            {
                IconButton(onClick = { onToggleVisibility?.invoke() }) {
                    Icon(
                        imageVector = if (passwordVisible)
                            Icons.Outlined.VisibilityOff
                        else
                            Icons.Outlined.Visibility,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        } else null,
        visualTransformation = if (isPassword && !passwordVisible)
            PasswordVisualTransformation()
        else VisualTransformation.None,
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(bottom = 14.dp),
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