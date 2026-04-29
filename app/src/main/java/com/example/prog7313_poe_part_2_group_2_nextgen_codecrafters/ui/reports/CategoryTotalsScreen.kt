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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.database.AppDatabase
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.R
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.dao.ExpenseDao
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun CategoryTotalsScreen(
    userId: Int,
    expenseDao: ExpenseDao,
    navController: NavController
) {
    // I pull all expenses with their category names so the totals update live.
    val allExpenses by expenseDao.getExpensesWithCategoryForUser(userId)
        .collectAsState(initial = emptyList())

    var selectedFilter by remember { mutableStateOf("All") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }

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

    val today = remember { startOfDay(Calendar.getInstance()) }

    // I filter expenses using Calendar so the app works with minSdk 24.
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

    // I group expenses by category and calculate the total amount spent per category.
    val categoryTotals = filteredExpenses
        .groupBy { it.categoryName }
        .map { (categoryName, expenses) ->
            categoryName to expenses.sumOf { it.amount }
        }
        .sortedBy { it.first }

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
            CategoryTotalsTopBar(
                onBackClick = {
                    navController.navigate("categories/$userId") {
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

                // I keep All, Today, This Week, and This Month next to one another.
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterButton("All", selectedFilter) { selectedFilter = "All" }
                    FilterButton("Today", selectedFilter) { selectedFilter = "Today" }
                    FilterButton("This Week", selectedFilter) { selectedFilter = "This Week" }
                    FilterButton("This Month", selectedFilter) { selectedFilter = "This Month" }
                }

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
                    placeholder = { Text("Start Date e.g. 29/4/2026") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = fieldColors(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = endDate,
                    onValueChange = { endDate = it },
                    placeholder = { Text("End Date e.g. 29/4/2026") },
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

        CategoryTotalsBottomNav(
            navController = navController,
            userId = userId,
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        // I show the hamburger overlay on top of the totals screen.
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
}

@Composable
private fun CategoryTotalsTopBar(
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
                    1.dp,
                    if (selected) Color(0xFF65D6D0) else Color.White.copy(alpha = 0.10f),
                    RoundedCornerShape(22.dp)
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

@Composable
private fun CategoryTotalsBottomNav(
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
            navController.navigate("dashboard/$userId") { launchSingleTop = true }
        }

        BottomNavItem(Icons.Default.List, "Expenses", false) {
            navController.navigate("expense_list/$userId") { launchSingleTop = true }
        }

        BottomNavItem(Icons.Default.Folder, "Categories", true) {
            navController.navigate("categories/$userId") { launchSingleTop = true }
        }

        BottomNavItem(Icons.Default.Settings, "Settings", false) {
            navController.navigate("settings/$userId") { launchSingleTop = true }
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
    val other = Calendar.getInstance().apply { time = date }

    return other.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
            other.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR)
}

private fun isSameMonth(date: java.util.Date, calendar: Calendar): Boolean {
    val other = Calendar.getInstance().apply { time = date }

    return other.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
            other.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
}

private fun parseExpenseDate(dateText: String): java.util.Date? {
    val formats = listOf(
        SimpleDateFormat("d/M/yyyy", Locale.getDefault()),
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()),
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    )

    return formats.firstNotNullOfOrNull { format ->
        try {
            format.isLenient = false
            startOfDay(Calendar.getInstance().apply {
                time = format.parse(dateText)!!
            }).time
        } catch (e: Exception) {
            null
        }
    }
}