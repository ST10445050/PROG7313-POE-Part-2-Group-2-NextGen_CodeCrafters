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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.R
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.repository.ExpenseRepository
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.repository.ProfileRepository
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.components.SharedBottomNav
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.components.SharedSideMenu
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.components.SharedTopBar
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun CategoryTotalsScreen(
    userId: String,
    navController: NavController
) {
    val profileRepository = remember { ProfileRepository() }
    val expenseRepository = remember { ExpenseRepository() }
    val scope = rememberCoroutineScope()

    var selectedFilter by remember { mutableStateOf("All") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }

    var categoryTotals by remember { mutableStateOf<List<Pair<String, Double>>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var feedbackMessage by remember { mutableStateOf("") }

    var showMenu by remember { mutableStateOf(false) }
    var userName by remember { mutableStateOf("User") }

    fun loadCategoryTotals(
        filter: String,
        customStartDate: String = startDate,
        customEndDate: String = endDate
    ) {
        scope.launch {
            try {
                isLoading = true
                feedbackMessage = ""

                val expenses = when (filter) {
                    "Today" -> {
                        val today = getTodayDate()
                        expenseRepository.getExpensesForUserByDateRange(
                            userId = userId,
                            startDate = today,
                            endDate = today
                        )
                    }

                    "This Week" -> {
                        val weekStart = getCurrentWeekSunday()
                        val weekEnd = getCurrentWeekSaturday()

                        expenseRepository.getExpensesForUserByDateRange(
                            userId = userId,
                            startDate = weekStart,
                            endDate = weekEnd
                        )
                    }

                    "This Month" -> {
                        val monthStart = getCurrentMonthStartDate()
                        val monthEnd = getCurrentMonthEndDate()

                        expenseRepository.getExpensesForUserByDateRange(
                            userId = userId,
                            startDate = monthStart,
                            endDate = monthEnd
                        )
                    }

                    "Custom" -> {
                        if (customStartDate.isBlank() || customEndDate.isBlank()) {
                            feedbackMessage = "Please enter both a start date and end date."
                            emptyList()
                        } else {
                            expenseRepository.getExpensesForUserByDateRange(
                                userId = userId,
                                startDate = customStartDate,
                                endDate = customEndDate
                            )
                        }
                    }

                    else -> {
                        expenseRepository.getExpensesForUser(userId)
                    }
                }

                categoryTotals = expenses
                    .groupBy { it.categoryName }
                    .map { (categoryName, expensesForCategory) ->
                        categoryName to expensesForCategory.sumOf { it.amount }
                    }
                    .sortedBy { it.first }

            } catch (e: Exception) {
                feedbackMessage = e.message ?: "Could not load category totals."
                categoryTotals = emptyList()
            } finally {
                isLoading = false
            }
        }
    }

    LaunchedEffect(userId) {
        userName = profileRepository.getProfile(userId)?.name ?: "User"
        loadCategoryTotals("All")
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

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterButton("All", selectedFilter) {
                        selectedFilter = "All"
                        loadCategoryTotals("All")
                    }

                    FilterButton("Today", selectedFilter) {
                        selectedFilter = "Today"
                        startDate = getTodayDate()
                        endDate = getTodayDate()
                        loadCategoryTotals("Today")
                    }

                    FilterButton("This Week", selectedFilter) {
                        selectedFilter = "This Week"
                        startDate = getCurrentWeekSunday()
                        endDate = getCurrentWeekSaturday()
                        loadCategoryTotals("This Week")
                    }

                    FilterButton("This Month", selectedFilter) {
                        selectedFilter = "This Month"
                        startDate = getCurrentMonthStartDate()
                        endDate = getCurrentMonthEndDate()
                        loadCategoryTotals("This Month")
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "Selected Range",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = when (selectedFilter) {
                        "All" -> "Showing all expenses"
                        else -> "Showing $startDate to $endDate"
                    },
                    color = Color.White.copy(alpha = 0.75f),
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "Custom Date Range",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = startDate,
                    onValueChange = { startDate = it },
                    placeholder = {
                        Text("Start Date e.g. 2026-06-01")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = fieldColors(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = endDate,
                    onValueChange = { endDate = it },
                    placeholder = {
                        Text("End Date e.g. 2026-06-30")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = fieldColors(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                FilterButton("Custom", selectedFilter) {
                    selectedFilter = "Custom"
                    loadCategoryTotals(
                        filter = "Custom",
                        customStartDate = startDate,
                        customEndDate = endDate
                    )
                }

                if (feedbackMessage.isNotBlank()) {
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = feedbackMessage,
                        color = Color.White.copy(alpha = 0.80f),
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color(0xFF65D6D0))
                    }
                } else if (categoryTotals.isEmpty()) {
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

        SharedBottomNav(
            navController = navController,
            userId = userId,
            currentScreen = "categories",
            modifier = Modifier.align(Alignment.BottomCenter)
        )

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
                onAnalyticsClick = {
                    showMenu = false
                    navController.navigate("analytics/$userId") {
                        launchSingleTop = true
                    }
                },
                onHelpClick = {
                    showMenu = false
                    navController.navigate("help/$userId") {
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

private fun getTodayDate(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(Calendar.getInstance().time)
}

private fun getCurrentWeekSunday(): String {
    val calendar = Calendar.getInstance()

    calendar.firstDayOfWeek = Calendar.SUNDAY
    calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)

    return formatCalendarDate(calendar)
}

private fun getCurrentWeekSaturday(): String {
    val calendar = Calendar.getInstance()

    calendar.firstDayOfWeek = Calendar.SUNDAY
    calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY)

    return formatCalendarDate(calendar)
}

private fun getCurrentMonthStartDate(): String {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.DAY_OF_MONTH, 1)

    return formatCalendarDate(calendar)
}

private fun getCurrentMonthEndDate(): String {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))

    return formatCalendarDate(calendar)
}

private fun formatCalendarDate(calendar: Calendar): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(calendar.time)
}