package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.reports

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
import androidx.compose.material.icons.filled.Category
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.R
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.dao.ExpenseDao
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.database.AppDatabase
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.components.SharedBottomNav
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.components.SharedSideMenu
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.components.SharedTopBar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun CategoryTotalsScreen(
    userId: Int,
    expenseDao: ExpenseDao,
    navController: NavController
) {
    // Loads all expenses with category names so totals update live.
    val allExpenses by expenseDao.getExpensesWithCategoryForUser(userId)
        .collectAsState(initial = emptyList())

    // Tracks the selected date filter.
    var selectedFilter by remember { mutableStateOf("All") }

    // Stores custom start and end date text.
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }

    // Controls the shared hamburger side menu.
    var showMenu by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)

    // Stores the logged-in user's name for the shared side menu.
    var userName by remember { mutableStateOf("User") }

    // Loads the real user name from RoomDB.
    LaunchedEffect(userId) {
        userName = db.userDao().getUserById(userId)?.name ?: "User"
    }

    // Stores today's date at midnight to make comparisons accurate.
    val today = remember { startOfDay(Calendar.getInstance()) }

    // Filters expenses based on the selected date option.
    val filteredExpenses = allExpenses.filter { expense ->
        val expenseDate = parseExpenseDate(expense.date)

        when (selectedFilter) {
            "Today" -> {
                expenseDate != null && isSameDay(expenseDate, today)
            }

            "This Week" -> {
                val startOfWeek = calendarDaysAgo(6)
                expenseDate != null &&
                        !expenseDate.before(startOfWeek.time) &&
                        !expenseDate.after(today.time)
            }

            "This Month" -> {
                expenseDate != null && isSameMonth(expenseDate, today)
            }

            "Custom" -> {
                val customStart = parseExpenseDate(startDate)
                val customEnd = parseExpenseDate(endDate)

                expenseDate != null &&
                        customStart != null &&
                        customEnd != null &&
                        !expenseDate.before(customStart) &&
                        !expenseDate.after(customEnd)
            }

            else -> true
        }
    }

    // Groups expenses by category name and calculates the total amount per category.
    val categoryTotals = filteredExpenses
        .groupBy { it.categoryName }
        .map { (categoryName, expenses) ->
            categoryName to expenses.sumOf { it.amount }
        }
        .sortedBy { it.first }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background image used across the app.
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
            // Shared fixed top bar.
            SharedTopBar(
                showBackButton = true,
                onBackClick = {
                    navController.navigate("categories/$userId") {
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
                    text = "Category Totals",
                    color = Color.White,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "View total spending per category for a selected period.",
                    color = Color(0xFF65D6D0),
                    fontSize = 15.sp,
                    modifier = Modifier.padding(top = 6.dp, bottom = 18.dp)
                )

                // Quick date filters.
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterButton("All", selectedFilter) {
                        selectedFilter = "All"
                    }

                    FilterButton("Today", selectedFilter) {
                        selectedFilter = "Today"
                    }

                    FilterButton("This Week", selectedFilter) {
                        selectedFilter = "This Week"
                    }

                    FilterButton("This Month", selectedFilter) {
                        selectedFilter = "This Month"
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "Custom Date Range",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Custom start date input.
                OutlinedTextField(
                    value = startDate,
                    onValueChange = { startDate = it },
                    placeholder = {
                        Text("Start Date e.g. 2026-04-29")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = fieldColors(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Custom end date input.
                OutlinedTextField(
                    value = endDate,
                    onValueChange = { endDate = it },
                    placeholder = {
                        Text("End Date e.g. 2026-04-29")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = fieldColors(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                FilterButton("Custom", selectedFilter) {
                    selectedFilter = "Custom"
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Shows an empty message when no totals match the selected date range.
                if (categoryTotals.isEmpty()) {
                    EmptyTotalsCard()
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 18.dp)
                    ) {
                        items(categoryTotals) { (categoryName, totalAmount) ->
                            CategoryTotalCard(categoryName, totalAmount)
                        }
                    }
                }
            }
        }

        // Shared bottom navbar.
        SharedBottomNav(
            navController = navController,
            userId = userId,
            currentScreen = "categories",
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        // Dark overlay and shared side menu.
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
                    navController.navigate("budget_goals/$userId") {
                        launchSingleTop = true
                    }
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
}

@Composable
private fun FilterButton(
    text: String,
    selectedFilter: String,
    onClick: () -> Unit
) {
    val selected = selectedFilter == text

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues(0.dp),
        shape = RoundedCornerShape(22.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.horizontalGradient(
                        if (selected) {
                            listOf(Color(0xFFA6F22E), Color(0xFF38D6A5))
                        } else {
                            listOf(Color(0xFF17263A), Color(0xFF17263A))
                        }
                    ),
                    RoundedCornerShape(22.dp)
                )
                .border(
                    width = 1.dp,
                    color = if (selected) Color(0xFF65D6D0) else Color.White.copy(alpha = 0.10f),
                    shape = RoundedCornerShape(22.dp)
                )
                .padding(horizontal = 18.dp, vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun CategoryTotalCard(
    categoryName: String,
    totalAmount: Double
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xCC101B2D), RoundedCornerShape(16.dp))
            .border(1.dp, Color.White.copy(alpha = 0.10f), RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(Color(0xFF1F4B5A), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Category,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Text(
            text = categoryName,
            color = Color.White,
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = "R ${String.format("%.2f", totalAmount)}",
            color = Color.White,
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun EmptyTotalsCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(Color(0xCC101B2D), RoundedCornerShape(16.dp))
            .border(1.dp, Color.White.copy(alpha = 0.10f), RoundedCornerShape(16.dp))
            .padding(18.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No expenses found for this period.",
            color = Color.White.copy(alpha = 0.75f),
            fontSize = 16.sp
        )
    }
}

@Composable
private fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    focusedPlaceholderColor = Color.White.copy(alpha = 0.45f),
    unfocusedPlaceholderColor = Color.White.copy(alpha = 0.45f),
    focusedBorderColor = Color(0xFF65D6D0),
    unfocusedBorderColor = Color(0xFF1AA3A8),
    cursorColor = Color(0xFF65D6D0),
    focusedContainerColor = Color.Transparent,
    unfocusedContainerColor = Color.Transparent
)

private fun startOfDay(calendar: Calendar): Calendar {
    return calendar.apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
}

private fun calendarDaysAgo(daysAgo: Int): Calendar {
    return startOfDay(Calendar.getInstance()).apply {
        add(Calendar.DAY_OF_YEAR, -daysAgo)
    }
}

private fun isSameDay(date: java.util.Date, calendar: Calendar): Boolean {
    val other = Calendar.getInstance().apply {
        time = date
    }

    return other.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
            other.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR)
}

private fun isSameMonth(date: java.util.Date, calendar: Calendar): Boolean {
    val other = Calendar.getInstance().apply {
        time = date
    }

    return other.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
            other.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
}

private fun parseExpenseDate(dateText: String): java.util.Date? {
    val formats = listOf(
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()),
        SimpleDateFormat("d/M/yyyy", Locale.getDefault()),
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    )

    return formats.firstNotNullOfOrNull { format ->
        try {
            format.isLenient = false
            startOfDay(
                Calendar.getInstance().apply {
                    time = format.parse(dateText)!!
                }
            ).time
        } catch (e: Exception) {
            null
        }
    }
}