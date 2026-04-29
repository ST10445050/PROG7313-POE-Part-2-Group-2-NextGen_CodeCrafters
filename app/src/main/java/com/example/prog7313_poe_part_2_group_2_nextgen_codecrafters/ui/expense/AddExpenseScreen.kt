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
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Folder
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
import androidx.compose.ui.platform.LocalContext
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.database.AppDatabase
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.R
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.entities.Expense
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.categories.CategoryViewModel
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

    // I store the selected category from the user's saved RoomDB categories.
    var selectedCategoryName by remember { mutableStateOf("") }
    var selectedCategoryId by remember { mutableStateOf(0) }

    // I keep the receipt image optional.
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    var date by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }

    // I use this state to open and close the hamburger menu.
    var showMenu by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)

// I store the logged-in user's name for the hamburger menu.
    var userName by remember { mutableStateOf("User") }

// I load the real user name from RoomDB using the current userId.
    LaunchedEffect(userId) {
        userName = db.userDao().getUserById(userId)?.name ?: "User"
    }
    val calendar = Calendar.getInstance()

    var showDateDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    val categories by categoryViewModel.categories.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    // I open the gallery so the user can optionally upload a receipt image.
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        imageUri = uri
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
                .padding(bottom = 78.dp)
        ) {
            AddExpenseTopBar(
                onBackClick = {
                    navController.navigate("expense_list/$userId") {
                        launchSingleTop = true
                    }
                },
                onMenuClick = {
                    // I open the hamburger menu when the user taps the three lines.
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

                        // I only save the expense when the amount and category are valid.
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
                            // I keep the Add Expense button in the bright ombré colour.
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

        ExpenseBottomNav(
            navController = navController,
            userId = userId,
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        // I show the hamburger menu overlay on top of this screen.
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
                    // I close this for now because Budget Goals will be linked later.
                    showMenu = false
                },
                onLogoutClick = {
                    showMenu = false

                    // I send the user back to the landing page and clear the back stack.
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
                            date =
                                "${cal.get(Calendar.DAY_OF_MONTH)}/${cal.get(Calendar.MONTH) + 1}/${cal.get(Calendar.YEAR)}"
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
private fun AddExpenseTopBar(
    onBackClick: () -> Unit,
    onMenuClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .background(Color(0xEE071827))
            .padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            tint = Color.White,
            modifier = Modifier
                .size(34.dp)
                .clickable { onBackClick() }
        )

        Spacer(modifier = Modifier.width(14.dp))

        Text("Fin", color = Color.White, fontSize = 27.sp, fontWeight = FontWeight.Bold)
        Text("Track", color = Color(0xFF65D6D0), fontSize = 27.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.weight(1f))

        // I make the hamburger icon open the shared side menu.
        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = "Open menu",
            tint = Color.White,
            modifier = Modifier
                .size(34.dp)
                .clickable { onMenuClick() }
        )
    }
}

@Composable
private fun SharedSideMenu(
    modifier: Modifier = Modifier,
    userName: String,
    onBudgetGoalsClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Column(
        modifier = modifier
            .padding(top = 72.dp, bottom = 78.dp)
            .width(278.dp)
            .fillMaxHeight()
            .background(Color(0xF0111C2D))
            .border(1.dp, Color.White.copy(alpha = 0.10f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xDD071827))
                .padding(horizontal = 24.dp, vertical = 22.dp)
        ) {
            Row {

                Text(
                    text = "Hello ",
                    color = Color(0xFF65D6D0),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )


                Text(
                    text = "$userName 👋",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Let’s manage your finances today.",
                color = Color.White.copy(alpha = 0.82f),
                fontSize = 16.sp
            )
        }

        Text(
            text = "MENU",
            color = Color.White.copy(alpha = 0.58f),
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
        )

        Divider(color = Color.White.copy(alpha = 0.08f))

        SharedMenuItem(
            icon = Icons.Default.Flag,
            title = "Budget Goals",
            iconColor = Color(0xFF65D6D0),
            onClick = onBudgetGoalsClick
        )

        Divider(color = Color.White.copy(alpha = 0.08f))

        SharedMenuItem(
            icon = Icons.Default.Logout,
            title = "Logout",
            iconColor = Color(0xFFE04F5F),
            onClick = onLogoutClick
        )
    }
}

@Composable
private fun SharedMenuItem(
    icon: ImageVector,
    title: String,
    iconColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .clickable { onClick() }
            .padding(horizontal = 28.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = iconColor,
            modifier = Modifier.size(32.dp)
        )

        Spacer(modifier = Modifier.width(18.dp))

        Text(
            text = title,
            color = Color.White,
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.55f),
            modifier = Modifier.size(30.dp)
        )
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
            Text(placeholder, color = Color.White.copy(alpha = 0.45f))
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
            Text(placeholder, color = Color.White.copy(alpha = 0.45f))
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

@Composable
private fun ExpenseBottomNav(
    navController: NavController,
    userId: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(78.dp)
            .background(Color(0xF5071827))
            .padding(horizontal = 10.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomNavItem(Icons.Default.Home, "Dashboard", false) {
            navController.navigate("dashboard/$userId") {
                launchSingleTop = true
            }
        }

        BottomNavItem(Icons.Outlined.CreditCard, "Expenses", true) {
            navController.navigate("expense_list/$userId") {
                launchSingleTop = true
            }
        }

        BottomNavItem(Icons.Outlined.Folder, "Categories", false) {
            navController.navigate("categories/$userId") {
                launchSingleTop = true
            }
        }

        BottomNavItem(Icons.Default.Settings, "Settings", false) {
            navController.navigate("settings/$userId") {
                launchSingleTop = true
            }
        }
    }
}

@Composable
private fun BottomNavItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val activeColor = Color(0xFF65D6D0)
    val inactiveColor = Color.White.copy(alpha = 0.65f)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (selected) activeColor else inactiveColor,
            modifier = Modifier.size(27.dp)
        )

        Text(
            text = label,
            color = if (selected) activeColor else inactiveColor,
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
        )
    }
}