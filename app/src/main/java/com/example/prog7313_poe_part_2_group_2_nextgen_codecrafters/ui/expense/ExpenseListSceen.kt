package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.expense

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.R
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.database.AppDatabase
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseListScreen(
    userId: Int,
    viewModel: ExpenseViewModel,
    navController: NavController
) {
    // Loads all expenses for the logged-in user.
    val expenseList by viewModel.getExpensesForUser(userId)
        .collectAsState(initial = emptyList())

    // Tracks the selected filter chip.
    var selectedFilter by remember { mutableStateOf("All") }

    // Stores the start and end dates used for filtering.
    var startDate by remember { mutableStateOf("2020-01-01") }
    var endDate by remember { mutableStateOf("2030-12-31") }

    // Controls whether the custom date picker dialog is open.
    var showCustomDatePicker by remember { mutableStateOf(false) }

    // Loads expenses inside the selected date range.
    val filteredExpenseList by viewModel.getExpensesForUserByDateRange(
        userId = userId,
        startDate = startDate,
        endDate = endDate
    ).collectAsState(initial = emptyList())

    // If All is selected, show everything. Otherwise show the filtered list.
    val displayList = if (selectedFilter == "All") expenseList else filteredExpenseList

    // Controls the hamburger menu.
    var showMenu by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)

    // Stores the user's name for the side menu.
    var userName by remember { mutableStateOf("User") }

    // Loads the logged-in user's real name from RoomDB.
    LaunchedEffect(userId) {
        userName = db.userDao().getUserById(userId)?.name ?: "User"
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background image.
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
            ExpenseTopBar(
                onBackClick = {
                    navController.navigate("dashboard/$userId") {
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
                    .padding(horizontal = 18.dp)
                    .padding(top = 22.dp)
            ) {
                Text(
                    text = "Expenses",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Track and manage your spending.",
                    color = Color(0xFF65D6D0),
                    fontSize = 17.sp,
                    modifier = Modifier.padding(top = 6.dp, bottom = 14.dp)
                )

                // Date filter section at the top of the Expense page.
                ExpenseFilterSection(
                    selectedFilter = selectedFilter,
                    startDate = startDate,
                    endDate = endDate,
                    onFilterSelected = { filter ->
                        when (filter) {
                            "Today" -> {
                                selectedFilter = filter
                                startDate = getTodayDate()
                                endDate = getTodayDate()
                            }

                            "This Week" -> {
                                selectedFilter = filter
                                startDate = getWeekStartDate()
                                endDate = getTodayDate()
                            }

                            "This Month" -> {
                                selectedFilter = filter
                                startDate = getMonthStartDate()
                                endDate = getTodayDate()
                            }

                            "Custom" -> {
                                showCustomDatePicker = true
                            }

                            else -> {
                                selectedFilter = "All"
                                startDate = "2020-01-01"
                                endDate = "2030-12-31"
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Expense list area.
                if (displayList.isEmpty()) {
                    EmptyExpenseCard(selectedFilter)
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 18.dp)
                    ) {
                        items(displayList) { expense ->
                            val formattedDate = remember(expense.date) {
                                formatExpenseDateForDisplay(expense.date)
                            }

                            ExpenseCard(
                                title = expense.description.ifBlank { "Expense" },
                                amount = "R ${String.format("%.2f", expense.amount)}",
                                date = formattedDate,
                                icon = getExpenseIconFromText(expense.description),
                                iconBg = getExpenseIconColorFromText(expense.description),
                                photoPath = expense.photoPath
                            )
                        }
                    }
                }

                // Add Expense button.
                Button(
                    onClick = { navController.navigate("add_expense/$userId") },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 32.dp)
                        .height(58.dp)
                        .shadow(14.dp, RoundedCornerShape(28.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(0.dp),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color(0xFFA6F22E), Color(0xFF38D6A5))
                                ),
                                RoundedCornerShape(28.dp)
                            )
                            .padding(horizontal = 28.dp, vertical = 13.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "Add Expense",
                            color = Color.White,
                            fontSize = 19.sp,
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

        // Side menu overlay.
        if (showMenu) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.45f))
                    .clickable { showMenu = false }
            )

            SharedSideMenu(
                modifier = Modifier.align(Alignment.TopEnd),
                userName = userName,
                onBudgetGoalsClick = { showMenu = false },
                onLogoutClick = {
                    showMenu = false
                    navController.navigate("landing") {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        // Custom start date and end date picker.
        if (showCustomDatePicker) {
            CustomDateRangeDialog(
                startDate = startDate,
                endDate = endDate,
                onDismiss = {
                    showCustomDatePicker = false
                },
                onSearch = { selectedStartDate, selectedEndDate ->
                    startDate = selectedStartDate
                    endDate = selectedEndDate
                    selectedFilter = "Custom"
                    showCustomDatePicker = false
                }
            )
        }
    }
}

@Composable
private fun ExpenseFilterSection(
    selectedFilter: String,
    startDate: String,
    endDate: String,
    onFilterSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xCC101B2D), RoundedCornerShape(16.dp))
            .border(1.dp, Color.White.copy(alpha = 0.10f), RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = null,
                tint = Color(0xFF65D6D0),
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Filter by Date",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = if (selectedFilter == "All") {
                "Showing all expenses"
            } else {
                "Showing $startDate to $endDate"
            },
            color = Color.White.copy(alpha = 0.70f),
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("All", "Today", "This Week", "This Month", "Custom").forEach { filter ->
                FilterChip(
                    selected = selectedFilter == filter,
                    onClick = { onFilterSelected(filter) },
                    label = {
                        Text(
                            text = filter,
                            fontWeight = if (selectedFilter == filter) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = Color.Transparent,
                        labelColor = Color.White.copy(alpha = 0.75f),
                        selectedContainerColor = Color(0xFF65D6D0).copy(alpha = 0.20f),
                        selectedLabelColor = Color(0xFF65D6D0)
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = selectedFilter == filter,
                        borderColor = Color.White.copy(alpha = 0.18f),
                        selectedBorderColor = Color(0xFF65D6D0)
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CustomDateRangeDialog(
    startDate: String,
    endDate: String,
    onDismiss: () -> Unit,
    onSearch: (String, String) -> Unit
) {
    // DateRangePicker needs dates as milliseconds.
    val dateRangePickerState = rememberDateRangePickerState(
        initialSelectedStartDateMillis = parseDateToMillis(startDate),
        initialSelectedEndDateMillis = parseDateToMillis(endDate),

        // Opens the picker as a proper calendar instead of text input.
        // This lets the user move between months using the calendar arrows.
        initialDisplayMode = DisplayMode.Picker
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    val selectedStart = dateRangePickerState.selectedStartDateMillis
                    val selectedEnd = dateRangePickerState.selectedEndDateMillis

                    if (selectedStart != null && selectedEnd != null) {
                        onSearch(
                            formatDateForDatabase(selectedStart),
                            formatDateForDatabase(selectedEnd)
                        )
                    }
                },
                enabled = dateRangePickerState.selectedStartDateMillis != null &&
                        dateRangePickerState.selectedEndDateMillis != null
            ) {
                Text("Search", color = Color(0xFF65D6D0))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color.White)
            }
        },
        colors = DatePickerDefaults.colors(
            containerColor = Color(0xFF101B2D)
        )
    ) {
        DateRangePicker(
            state = dateRangePickerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(560.dp)
                .padding(12.dp),
            title = {
                Text(
                    text = "Select Date Range",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
            },
            headline = {
                val start = dateRangePickerState.selectedStartDateMillis
                    ?.let { formatDateForDatabase(it) } ?: "Start Date"

                val end = dateRangePickerState.selectedEndDateMillis
                    ?.let { formatDateForDatabase(it) } ?: "End Date"

                Text(
                    text = "$start - $end",
                    color = Color.White,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            },


            showModeToggle = true,

            colors = DatePickerDefaults.colors(
                containerColor = Color(0xFF101B2D),
                titleContentColor = Color.White,
                headlineContentColor = Color.White,
                navigationContentColor = Color.White,
                dayContentColor = Color.White,
                selectedDayContentColor = Color.Black,
                selectedDayContainerColor = Color(0xFF65D6D0),
                todayContentColor = Color(0xFF65D6D0),
                todayDateBorderColor = Color(0xFF65D6D0),
                yearContentColor = Color.White,
                selectedYearContentColor = Color.Black,
                selectedYearContainerColor = Color(0xFF65D6D0)
            )
        )
    }
}
@Composable
private fun ExpenseTopBar(
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
            icon = Icons.Default.TrackChanges,
            title = "Budget Goals",
            iconColor = Color(0xFF65D6D0),
            onClick = onBudgetGoalsClick
        )

        Divider(color = Color.White.copy(alpha = 0.08f))

        SharedMenuItem(
            icon = Icons.Default.PowerSettingsNew,
            title = "Logout",
            iconColor = Color(0xFFE04F5F),
            onClick = onLogoutClick
        )

        Divider(color = Color.White.copy(alpha = 0.08f))
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
private fun ExpenseCard(
    title: String,
    amount: String,
    date: String,
    icon: ImageVector,
    iconBg: Color,
    photoPath: String?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(Color(0xCC101B2D), RoundedCornerShape(16.dp))
            .border(1.dp, Color.White.copy(alpha = 0.10f), RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .background(iconBg, RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(30.dp)
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = title,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f),
            maxLines = 2
        )

        Spacer(modifier = Modifier.width(10.dp))

        if (!photoPath.isNullOrBlank()) {
            AsyncImage(
                model = photoPath,
                contentDescription = "Receipt photo",
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(10.dp))
                    .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(10.dp))
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column(
            modifier = Modifier.width(92.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = amount,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End,
                maxLines = 1
            )

            Text(
                text = date,
                color = Color.White,
                fontSize = 12.sp,
                textAlign = TextAlign.End,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun EmptyExpenseCard(selectedFilter: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .background(Color(0xCC101B2D), RoundedCornerShape(16.dp))
            .border(1.dp, Color.White.copy(alpha = 0.10f), RoundedCornerShape(16.dp))
            .padding(18.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (selectedFilter == "All") {
                "No expenses yet. Add your first expense to start tracking."
            } else {
                "No expenses found for this selected period."
            },
            color = Color.White.copy(alpha = 0.75f),
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}

private fun getExpenseIconFromText(description: String): ImageVector {
    val text = description.lowercase(Locale.getDefault())

    return when {
        text.contains("uber") ||
                text.contains("bolt") ||
                text.contains("taxi") ||
                text.contains("transport") -> Icons.Default.DirectionsCar

        text.contains("kfc") ||
                text.contains("mcdonald") ||
                text.contains("steers") ||
                text.contains("burger") ||
                text.contains("king") ||
                text.contains("nandos") ||
                text.contains("pizza") ||
                text.contains("pasta") ||
                text.contains("food") -> Icons.Default.Fastfood

        text.contains("netflix") ||
                text.contains("prime") ||
                text.contains("video") ||
                text.contains("dstv") ||
                text.contains("entertainment") -> Icons.Default.Tv

        text.contains("pick n pay") ||
                text.contains("picknpay") ||
                text.contains("take n pay") ||
                text.contains("spar") ||
                text.contains("checkers") ||
                text.contains("grocery") ||
                text.contains("groceries") -> Icons.Default.ShoppingCart

        text.contains("clothing") ||
                text.contains("clothes") ||
                text.contains("edgars") ||
                text.contains("shirt") -> Icons.Default.Checkroom

        text.contains("petrol") ||
                text.contains("diesel") ||
                text.contains("fuel") ||
                text.contains("shell") ||
                text.contains("bp") ||
                text.contains("engen") ||
                text.contains("total") -> Icons.Default.LocalGasStation

        else -> Icons.Default.CreditCard
    }
}

private fun getExpenseIconColorFromText(description: String): Color {
    val text = description.lowercase(Locale.getDefault())

    return when {
        text.contains("uber") ||
                text.contains("bolt") ||
                text.contains("taxi") ||
                text.contains("transport") -> Color(0xFF185EA8)

        text.contains("kfc") ||
                text.contains("mcdonald") ||
                text.contains("steers") ||
                text.contains("burger") ||
                text.contains("king") ||
                text.contains("nandos") ||
                text.contains("pizza") ||
                text.contains("pasta") ||
                text.contains("food") -> Color(0xFFD83A3A)

        text.contains("netflix") ||
                text.contains("prime") ||
                text.contains("video") ||
                text.contains("dstv") ||
                text.contains("entertainment") -> Color(0xFF6932B8)

        text.contains("pick n pay") ||
                text.contains("picknpay") ||
                text.contains("take n pay") ||
                text.contains("spar") ||
                text.contains("checkers") ||
                text.contains("grocery") ||
                text.contains("groceries") -> Color(0xFF1F8A4C)

        text.contains("clothing") ||
                text.contains("clothes") ||
                text.contains("edgars") ||
                text.contains("shirt") -> Color(0xFFE0A800)

        text.contains("petrol") ||
                text.contains("diesel") ||
                text.contains("fuel") ||
                text.contains("shell") ||
                text.contains("bp") ||
                text.contains("engen") ||
                text.contains("total") -> Color(0xFFE87500)

        else -> Color(0xFF2E3A46)
    }
}

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

private fun formatExpenseDateForDisplay(date: String): String {
    return try {
        val inputFormats = listOf(
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()),
            SimpleDateFormat("d/M/yyyy", Locale.getDefault()),
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        )

        val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

        for (format in inputFormats) {
            try {
                val parsedDate = format.parse(date)
                if (parsedDate != null) {
                    return outputFormat.format(parsedDate)
                }
            } catch (_: Exception) {
            }
        }

        date
    } catch (_: Exception) {
        date
    }
}

private fun getTodayDate(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(Date())
}

private fun getWeekStartDate(): String {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)

    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(calendar.time)
}

private fun getMonthStartDate(): String {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.DAY_OF_MONTH, 1)

    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(calendar.time)
}

private fun formatDateForDatabase(millis: Long): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    formatter.timeZone = TimeZone.getTimeZone("UTC")
    return formatter.format(Date(millis))
}

private fun parseDateToMillis(date: String): Long? {
    return try {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        formatter.parse(date)?.time
    } catch (_: Exception) {
        null
    }
}