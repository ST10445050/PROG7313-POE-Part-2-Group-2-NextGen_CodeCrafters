package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.expense

import android.app.TimePickerDialog
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.R
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.database.AppDatabase
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.entities.Expense
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.categories.CategoryViewModel
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.components.SharedBottomNav
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.components.SharedSideMenu
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.components.SharedTopBar
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    userId: Int,
    viewModel: ExpenseViewModel,
    navController: NavController,
    onSaveSuccess: () -> Unit,
    categoryViewModel: CategoryViewModel = viewModel()
) {
    var description by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }

    // Stores the selected category from the user's saved RoomDB categories.
    var selectedCategoryName by remember { mutableStateOf("") }
    var selectedCategoryId by remember { mutableStateOf(0) }

    // Keeps the receipt image optional.
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // Stores the selected expense date and time values.
    var date by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }

    // Controls whether the shared hamburger menu is visible.
    var showMenu by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)

    // Stores the logged-in user's name for the shared side menu.
    var userName by remember { mutableStateOf("User") }

    // Loads the real user name from RoomDB using the current userId.
    LaunchedEffect(userId) {
        userName = db.userDao().getUserById(userId)?.name ?: "User"
    }

    val calendar = Calendar.getInstance()

    // Controls the Material date picker dialog.
    var showDateDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    // Loads the categories from the CategoryViewModel.
    val categories by categoryViewModel.categories.collectAsState()

    // Controls whether the category dropdown is open.
    var expanded by remember { mutableStateOf(false) }

    // Opens the gallery so the user can optionally upload a receipt image.
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        imageUri = uri
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background image used behind the screen content.
        Image(
            painter = painterResource(id = R.drawable.fintrack_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 78.dp)
        ) {
            // Shared top bar used across the app.
            SharedTopBar(
                showBackButton = true,
                onBackClick = {
                    navController.navigate("expense_list/$userId") {
                        launchSingleTop = true
                    }
                },
                onMenuClick = {
                    showMenu = true
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 18.dp)
                    .padding(top = 22.dp, bottom = 20.dp)
            ) {
                Text(
                    text = "Add Expense",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Record a new expense to track your spending.",
                    color = Color(0xFFB7C3D5),
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 6.dp, bottom = 20.dp)
                )

                InputCard {
                    FieldLabel("Amount")

                    StyledTextField(
                        value = amount,
                        onValueChange = { amount = it },
                        placeholder = "Enter amount",
                        keyboardType = KeyboardType.Number
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    FieldLabel("Category")

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = selectedCategoryName,
                            onValueChange = {},
                            readOnly = true,
                            placeholder = {
                                Text(
                                    text = if (categories.isEmpty()) {
                                        "No categories created yet"
                                    } else {
                                        "Select category"
                                    },
                                    color = Color.White.copy(alpha = 0.45f)
                                )
                            },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = Color(0xFF65D6D0)
                                )
                            },
                            colors = fieldColors(),
                            shape = RoundedCornerShape(10.dp),
                            enabled = categories.isNotEmpty()
                        )

                        ExposedDropdownMenu(
                            expanded = expanded && categories.isNotEmpty(),
                            onDismissRequest = { expanded = false }
                        ) {
                            categories.forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(category.name) },
                                    onClick = {
                                        selectedCategoryName = category.name
                                        selectedCategoryId = category.categoryId
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    if (categories.isEmpty()) {
                        Text(
                            text = "Create categories first before adding an expense.",
                            color = Color(0xFFB7C3D5),
                            fontSize = 13.sp,
                            modifier = Modifier.padding(top = 6.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    FieldLabel("Date")

                    ReadOnlyField(
                        value = date,
                        placeholder = "Select date",
                        icon = Icons.Default.DateRange,
                        onClick = { showDateDialog = true }
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    FieldLabel("Start Time")

                    ReadOnlyField(
                        value = startTime,
                        placeholder = "Select start time",
                        icon = Icons.Default.AccessTime,
                        onClick = {
                            TimePickerDialog(
                                context,
                                { _, hour, minute ->
                                    startTime = "%02d:%02d".format(hour, minute)
                                },
                                calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.MINUTE),
                                true
                            ).show()
                        }
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    FieldLabel("End Time")

                    ReadOnlyField(
                        value = endTime,
                        placeholder = "Select end time",
                        icon = Icons.Default.AccessTime,
                        onClick = {
                            TimePickerDialog(
                                context,
                                { _, hour, minute ->
                                    endTime = "%02d:%02d".format(hour, minute)
                                },
                                calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.MINUTE),
                                true
                            ).show()
                        }
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    FieldLabel("Description")

                    StyledTextField(
                        value = description,
                        onValueChange = { description = it },
                        placeholder = "Add a short description"
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                InputCard {
                    FieldLabel("Upload Receipt (Optional)")

                    Button(
                        onClick = { launcher.launch("image/*") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues(0.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(Color(0xFF65D6D0), Color(0xFF1AA3A8))
                                    ),
                                    RoundedCornerShape(10.dp)
                                )
                                .padding(horizontal = 22.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            Text(
                                text = if (imageUri == null) "Upload Receipt" else "File Selected",
                                color = Color.White,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(26.dp))

                Button(
                    onClick = {
                        val parsedAmount = amount.toDoubleOrNull()

                        // Saves the expense only when the amount and category are valid.
                        if (parsedAmount != null && selectedCategoryId != 0) {
                            viewModel.addExpense(
                                Expense(
                                    userId = userId,
                                    categoryId = selectedCategoryId,
                                    date = date,
                                    startTime = startTime,
                                    endTime = endTime,
                                    description = description,
                                    amount = parsedAmount,
                                    photoPath = imageUri?.toString()
                                )
                            )

                            onSaveSuccess()
                        }
                    },
                    enabled = true,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .height(56.dp)
                        .shadow(10.dp, RoundedCornerShape(28.dp)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent
                    ),
                    contentPadding = PaddingValues(0.dp),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        Color(0xFFA6F22E),
                                        Color(0xFF38D6A5)
                                    )
                                ),
                                RoundedCornerShape(28.dp)
                            )
                            .padding(horizontal = 28.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = Color.White
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "Add Expense",
                            color = Color.White,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Shared bottom navigation bar used across the app.
        SharedBottomNav(
            navController = navController,
            userId = userId,
            currentScreen = "expenses",
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        // Shared side menu overlay.
        if (showMenu) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.45f))
                    .clickable {
                        showMenu = false
                    }
            )

            SharedSideMenu(
                modifier = Modifier.align(Alignment.TopEnd),
                userName = userName,
                onBudgetGoalsClick = {
                    showMenu = false
                },
                onLogoutClick = {
                    showMenu = false

                    navController.navigate("landing") {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
    }

    if (showDateDialog) {
        DatePickerDialog(
            onDismissRequest = { showDateDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            val cal = Calendar.getInstance().apply { timeInMillis = it }

                            // Saves the date in yyyy-MM-dd format so filtering works correctly.
                            date = "%04d-%02d-%02d".format(
                                cal.get(Calendar.YEAR),
                                cal.get(Calendar.MONTH) + 1,
                                cal.get(Calendar.DAY_OF_MONTH)
                            )
                        }

                        showDateDialog = false
                    }
                ) {
                    Text("OK")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
private fun InputCard(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xCC101B2D), RoundedCornerShape(16.dp))
            .border(1.dp, Color.White.copy(alpha = 0.10f), RoundedCornerShape(16.dp))
            .padding(18.dp),
        content = content
    )
}

@Composable
private fun FieldLabel(text: String) {
    Text(
        text = text,
        color = Color.White,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
private fun StyledTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                color = Color.White.copy(alpha = 0.45f)
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        colors = fieldColors(),
        shape = RoundedCornerShape(10.dp)
    )
}

@Composable
private fun ReadOnlyField(
    value: String,
    placeholder: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = {},
        readOnly = true,
        placeholder = {
            Text(
                text = placeholder,
                color = Color.White.copy(alpha = 0.45f)
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        trailingIcon = {
            IconButton(onClick = onClick) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFF65D6D0)
                )
            }
        },
        colors = fieldColors(),
        shape = RoundedCornerShape(10.dp)
    )
}

@Composable
private fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    focusedBorderColor = Color(0xFF65D6D0),
    unfocusedBorderColor = Color(0xFF1AA3A8),
    cursorColor = Color(0xFF65D6D0),
    focusedContainerColor = Color.Transparent,
    unfocusedContainerColor = Color.Transparent
)