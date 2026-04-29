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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.R
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.database.AppDatabase
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.components.SharedBottomNav
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.components.SharedSideMenu
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.components.SharedTopBar
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

    // Stores the selected receipt image path.
    // When this value is not null, the full receipt preview dialog opens.
    var selectedReceiptImage by remember { mutableStateOf<String?>(null) }

    // Loads expenses inside the selected date range.
    val filteredExpenseList by viewModel.getExpensesForUserByDateRange(
        userId = userId,
        startDate = startDate,
        endDate = endDate
    ).collectAsState(initial = emptyList())

    // If All is selected, show every expense. Otherwise, show the filtered list.
    val displayList = if (selectedFilter == "All") expenseList else filteredExpenseList

    // Controls the shared hamburger menu.
    var showMenu by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)

    // Stores the logged-in user's name for the shared side menu.
    var userName by remember { mutableStateOf("User") }

    // Loads the logged-in user's real name from RoomDB.
    LaunchedEffect(userId) {
        userName = db.userDao().getUserById(userId)?.name ?: "User"
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
                                photoPath = expense.photoPath,

                                // Opens the full receipt preview when the expense has an attached photo.
                                onPhotoClick = {
                                    selectedReceiptImage = expense.photoPath
                                }
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

        // Full receipt image preview.
        // This opens only when the user clicks on a receipt thumbnail.
        if (!selectedReceiptImage.isNullOrBlank()) {
            ReceiptImagePreviewDialog(
                imagePath = selectedReceiptImage,
                onClose = {
                    selectedReceiptImage = null
                }
            )
        }
    }
}

@Composable
private fun ReceiptImagePreviewDialog(
    imagePath: String?,
    onClose: () -> Unit
) {
    Dialog(onDismissRequest = onClose) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 360.dp, max = 650.dp)
                .background(Color.Black.copy(alpha = 0.96f), RoundedCornerShape(18.dp))
                .border(1.dp, Color.White.copy(alpha = 0.18f), RoundedCornerShape(18.dp))
                .padding(12.dp)
        ) {
            // Full-size receipt image.
            AsyncImage(
                model = imagePath,
                contentDescription = "Full receipt image",
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .clip(RoundedCornerShape(14.dp)),
                contentScale = ContentScale.Fit
            )

            // Close button in the top-right corner.
            IconButton(
                onClick = onClose,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(42.dp)
                    .background(Color.Black.copy(alpha = 0.65f), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close receipt preview",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }

            // Small label at the bottom so the user knows what they are viewing.
            Text(
                text = "Receipt Preview",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .background(Color.Black.copy(alpha = 0.55f), RoundedCornerShape(20.dp))
                    .padding(horizontal = 14.dp, vertical = 7.dp)
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
private fun ExpenseCard(
    title: String,
    amount: String,
    date: String,
    icon: ImageVector,
    iconBg: Color,
    photoPath: String?,
    onPhotoClick: () -> Unit
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
            // Clickable receipt thumbnail.
            // Only records with a photo attached will open the full preview.
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .border(1.dp, Color(0xFF65D6D0).copy(alpha = 0.55f), RoundedCornerShape(10.dp))
                    .clickable { onPhotoClick() }
            ) {
                AsyncImage(
                    model = photoPath,
                    contentDescription = "Receipt photo",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Small visibility icon to show the user the image can be viewed.
                Icon(
                    imageVector = Icons.Default.Visibility,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(20.dp)
                        .background(Color.Black.copy(alpha = 0.55f), CircleShape)
                        .padding(3.dp)
                )
            }
        } else {
            // Empty placeholder shown when no photo was attached.
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(10.dp))
                    .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ImageNotSupported,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.35f),
                    modifier = Modifier.size(24.dp)
                )
            }
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