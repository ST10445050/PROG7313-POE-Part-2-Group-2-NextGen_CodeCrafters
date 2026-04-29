package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.R
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.database.AppDatabase
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.entities.QuestionnaireAnswers
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.entities.User
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.components.SharedBottomNav
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.components.SharedSideMenu
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.components.SharedTopBar
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.expense.ExpenseViewModel
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.theme.*

@Composable
fun DashboardScreen(
    navController: NavController,
    userId: Int,
    expenseViewModel: ExpenseViewModel
) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)

    // Stores the logged-in user's details from RoomDB.
    var user by remember { mutableStateOf<User?>(null) }

    // Stores the user's questionnaire answers from RoomDB.
    var answers by remember { mutableStateOf<QuestionnaireAnswers?>(null) }

    // Controls whether the shared hamburger menu is visible.
    var showMenu by remember { mutableStateOf(false) }

    // Loads all expenses for the logged-in user.
    val expenses by expenseViewModel
        .getExpensesForUser(userId)
        .collectAsState(initial = emptyList())

    // Loads user details and questionnaire answers when the screen opens.
    LaunchedEffect(userId) {
        user = db.userDao().getUserById(userId)
        answers = db.questionnaireDao().getAnswersByUserId(userId)
    }

    // Safely displays the user's name, or "User" if the database has no name.
    val userName = user?.name ?: "User"

    // Pulls budget values from the questionnaire answers.
    val monthlyBudget = answers?.monthlyIncome ?: 0.0
    val savingsGoal = answers?.monthlySavingsGoal ?: 0.0

    // Calculates how much the user has spent.
    val amountSpent = expenses.sumOf { it.amount }

    // Calculates the remaining amount from the monthly budget.
    val remaining = monthlyBudget - amountSpent

    // Calculates the percentage of the budget already used.
    val usedPercentage = if (monthlyBudget > 0) {
        ((amountSpent / monthlyBudget) * 100).toInt()
    } else {
        0
    }

    // Calculates progress bar value from 0f to 1f.
    val progressValue = if (monthlyBudget > 0) {
        (amountSpent / monthlyBudget).toFloat().coerceIn(0f, 1f)
    } else {
        0f
    }

    // Converts the stored comma-separated spending categories into a clean list.
    val categories = answers?.spendingCategories
        ?.split(",")
        ?.map { it.trim() }
        ?.filter { it.isNotBlank() }
        ?: emptyList()

    // Shows the latest 3 expenses on the dashboard.
    val recentExpenses = expenses.takeLast(3).reversed()

    // Displays a message based on the user's selected financial goal.
    val personalisedMessage = when (answers?.financialGoal) {
        "Save more money" -> "Your dashboard is focused on saving and reaching your monthly savings goal."
        "Reduce spending" -> "Your dashboard is focused on helping you control spending before adding expenses."
        "Pay off debt" -> "Your dashboard is focused on reducing debt through better tracking."
        "Track my finances better" -> "Your dashboard is focused on giving you better visibility of your money."
        else -> "Complete your questionnaire to personalise your dashboard."
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background image used across the app.
        Image(
            painter = painterResource(id = R.drawable.fintrack_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Shared fixed top bar.
            // Dashboard does not need a back button, only the menu icon.
            SharedTopBar(
                onMenuClick = { showMenu = true },
                showBackButton = false
            )

            // Main scrollable dashboard content.
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 18.dp)
                    .padding(top = 22.dp, bottom = 96.dp)
            ) {
                Text(
                    text = "Hello $userName 👋",
                    color = FinTrackMint,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = personalisedMessage,
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 17.sp,
                    modifier = Modifier.padding(top = 6.dp)
                )

                Spacer(modifier = Modifier.height(22.dp))

                DashboardCard {
                    Text(
                        text = "Monthly Budget Overview",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = "R${monthlyBudget.toInt()}",
                            color = Color.White,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Row {
                            Text(
                                text = "$usedPercentage%",
                                color = FinTrackMint,
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = " Used",
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LinearProgressIndicator(
                        progress = { progressValue },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp),
                        color = FinTrackLime,
                        trackColor = FinTrackTeal.copy(alpha = 0.45f)
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        BudgetItem("Budget", "R${monthlyBudget.toInt()}", Modifier.weight(1f))
                        BudgetItem("Spent", "R${amountSpent.toInt()}", Modifier.weight(1f))
                        BudgetItem("Remaining", "R${remaining.toInt()}", Modifier.weight(1f))
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Savings Goal: R${savingsGoal.toInt()}",
                        color = FinTrackMint,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                DashboardCard {
                    Text(
                        text = "Spending Summary",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    if (categories.isEmpty()) {
                        Text(
                            text = "No spending categories selected yet.",
                            color = Color.White.copy(alpha = 0.75f),
                            fontSize = 16.sp
                        )
                    } else {
                        categories.take(4).forEachIndexed { index, category ->
                            val categoryTotal = expenses
                                .filter { it.categoryId == index + 1 }
                                .sumOf { it.amount }

                            SpendingRow(
                                icon = when (category.lowercase()) {
                                    "groceries" -> Icons.Outlined.ShoppingBasket
                                    "transport" -> Icons.Outlined.DirectionsCar
                                    "subscriptions" -> Icons.Outlined.Receipt
                                    "shopping" -> Icons.Outlined.ShoppingCart
                                    "food", "eating out" -> Icons.Outlined.Restaurant
                                    else -> Icons.Outlined.Category
                                },
                                title = category,
                                amount = "R${categoryTotal.toInt()}",
                                progress = if (monthlyBudget > 0) {
                                    (categoryTotal / monthlyBudget).toFloat().coerceIn(0f, 1f)
                                } else {
                                    0f
                                },
                                color = listOf(
                                    FinTrackLime,
                                    FinTrackMint,
                                    Color(0xFFB075D6),
                                    Color(0xFFE85FA3)
                                ).getOrElse(index) { FinTrackMint }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                DashboardCard {
                    Text(
                        text = "Personalised Goal",
                        color = Color.White,
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = answers?.financialGoal ?: "No financial goal selected yet.",
                        color = FinTrackMint,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                DashboardCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Quick Actions",
                            color = Color.White,
                            fontSize = 21.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Your Progress ›",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        QuickAction("✚", "Add\nExpense", Modifier.weight(1f)) {
                            navController.navigate("add_expense/$userId") {
                                launchSingleTop = true
                            }
                        }

                        QuickAction("▥", "View\nInsights", Modifier.weight(1f)) {
                            // This can be linked to an insights screen later.
                        }

                        QuickAction("✪", "Budget\nGoals", Modifier.weight(1f)) {
                            // This can be linked to a budget goals screen later.
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "Recent Expenses",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                DashboardCard {
                    if (recentExpenses.isEmpty()) {
                        Text(
                            text = "No expenses yet. Add your first expense to start tracking.",
                            color = Color.White.copy(alpha = 0.75f),
                            fontSize = 16.sp
                        )
                    } else {
                        recentExpenses.forEach { expense ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = expense.description.ifBlank { "Expense" },
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.weight(1f)
                                )

                                Text(
                                    text = "R${expense.amount.toInt()}",
                                    color = FinTrackMint,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // Shared bottom navigation bar.
        SharedBottomNav(
            navController = navController,
            userId = userId,
            currentScreen = "dashboard",
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
private fun DashboardCard(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xCC071827), RoundedCornerShape(18.dp))
            .padding(18.dp),
        content = content
    )
}

@Composable
private fun BudgetItem(
    label: String,
    value: String,
    modifier: Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.75f),
            fontSize = 14.sp
        )

        Text(
            text = value,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun SpendingRow(
    icon: ImageVector,
    title: String,
    amount: String,
    progress: Float,
    color: Color
) {
    Column(modifier = Modifier.padding(bottom = 12.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(25.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = title,
                color = Color.White,
                fontSize = 20.sp,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = amount,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 38.dp)
                .height(7.dp),
            color = color,
            trackColor = Color(0xFF17354A)
        )
    }
}

@Composable
private fun QuickAction(
    icon: String,
    label: String,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .height(62.dp)
            .clickable { onClick() }
            .background(
                Brush.horizontalGradient(
                    listOf(FinTrackLime, FinTrackTeal)
                ),
                RoundedCornerShape(10.dp)
            )
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = icon,
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = label,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}