package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.border
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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

    var user by remember { mutableStateOf<User?>(null) }
    var answers by remember { mutableStateOf<QuestionnaireAnswers?>(null) }

    // I use this state to control when the hamburger menu is shown or hidden.
    var showMenu by remember { mutableStateOf(false) }

    val expenses by expenseViewModel
        .getExpensesForUser(userId)
        .collectAsState(initial = emptyList())

    LaunchedEffect(userId) {
        user = db.userDao().getUserById(userId)
        answers = db.questionnaireDao().getAnswersByUserId(userId)
    }

    val userName = user?.name ?: "User"
    val monthlyBudget = answers?.monthlyIncome ?: 0.0
    val savingsGoal = answers?.monthlySavingsGoal ?: 0.0

    val amountSpent = expenses.sumOf { it.amount }
    val remaining = monthlyBudget - amountSpent

    val usedPercentage = if (monthlyBudget > 0) {
        ((amountSpent / monthlyBudget) * 100).toInt()
    } else {
        0
    }

    val progressValue = if (monthlyBudget > 0) {
        (amountSpent / monthlyBudget).toFloat().coerceIn(0f, 1f)
    } else {
        0f
    }

    val categories = answers?.spendingCategories
        ?.split(",")
        ?.map { it.trim() }
        ?.filter { it.isNotBlank() }
        ?: emptyList()

    val recentExpenses = expenses.takeLast(3).reversed()

    val personalisedMessage = when (answers?.financialGoal) {
        "Save more money" -> "Your dashboard is focused on saving and reaching your monthly savings goal."
        "Reduce spending" -> "Your dashboard is focused on helping you control spending before adding expenses."
        "Pay off debt" -> "Your dashboard is focused on reducing debt through better tracking."
        "Track my finances better" -> "Your dashboard is focused on giving you better visibility of your money."
        else -> "Complete your questionnaire to personalise your dashboard."
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
                .padding(horizontal = 18.dp)
                .padding(top = 32.dp, bottom = 90.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row {
                    Text("Fin", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    Text("Track", color = FinTrackMint, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                }

                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Open menu",
                    tint = Color.White,
                    modifier = Modifier
                        .size(34.dp)
                        .clickable {
                            // I open the hamburger menu when the user taps the three lines.
                            showMenu = true
                        }
                )
            }

            Divider(
                color = Color.White.copy(alpha = 0.25f),
                modifier = Modifier.padding(top = 18.dp, bottom = 18.dp)
            )

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
                Text("Monthly Budget Overview", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text("R${monthlyBudget.toInt()}", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)

                    Row {
                        Text("$usedPercentage%", color = FinTrackMint, fontSize = 30.sp, fontWeight = FontWeight.Bold)
                        Text(" Used", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
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
                Text("Spending Summary", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(14.dp))

                if (categories.isEmpty()) {
                    Text("No spending categories selected yet.", color = Color.White.copy(alpha = 0.75f), fontSize = 16.sp)
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
                Text("Personalised Goal", color = Color.White, fontSize = 21.sp, fontWeight = FontWeight.Bold)

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
                    Text("Quick Actions", color = Color.White, fontSize = 21.sp, fontWeight = FontWeight.Bold)
                    Text("Your Progress ›", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    QuickAction("✚", "Add\nExpense", Modifier.weight(1f)) {
                        navController.navigate("add_expense/$userId") {
                            launchSingleTop = true
                        }
                    }

                    QuickAction("▥", "View\nInsights", Modifier.weight(1f)) {}

                    QuickAction("✪", "Budget\nGoals", Modifier.weight(1f)) {}
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

        BottomDashboardNav(
            navController = navController,
            userId = userId,
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        // I show the dark overlay and side menu only when the hamburger menu is open.
        if (showMenu) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.45f))
                    .clickable {
                        // I allow the user to close the menu by tapping outside it.
                        showMenu = false
                    }
            )

            DashboardSideMenu(
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
private fun DashboardSideMenu(
    modifier: Modifier = Modifier,
    userName: String,
    onBudgetGoalsClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Column(
        modifier = modifier
            // I start the menu below the top bar so it lines up neatly like the reference.
            .padding(top = 72.dp, bottom = 76.dp)
            .width(278.dp)
            .fillMaxHeight()
            .background(Color(0xF0111C2D))
            .border(1.dp, Color.White.copy(alpha = 0.10f))
    ) {
        // I added the user greeting at the top of the hamburger menu.
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xDD071827))
                .padding(horizontal = 24.dp, vertical = 22.dp)
        ) {
            Row {
                Text(
                    text = "Hello ",
                    color = FinTrackMint,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "$userName 👋",
                    color = Color.White,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Let’s manage your finances today.",
                color = Color.White.copy(alpha = 0.82f),
                fontSize = 16.sp,
                lineHeight = 22.sp
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

        DashboardMenuItem(
            icon = Icons.Default.TrackChanges,
            title = "Budget Goals",
            iconColor = FinTrackMint,
            onClick = onBudgetGoalsClick
        )

        Divider(color = Color.White.copy(alpha = 0.08f))

        DashboardMenuItem(
            icon = Icons.Default.PowerSettingsNew,
            title = "Logout",
            iconColor = Color(0xFFE04F5F),
            onClick = onLogoutClick
        )

        Divider(color = Color.White.copy(alpha = 0.08f))
    }
}
@Composable
private fun DashboardMenuItem(
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
        // I display the menu icon on the left, matching the hamburger menu reference.
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
private fun BudgetItem(label: String, value: String, modifier: Modifier) {
    Column(modifier = modifier) {
        Text(label, color = Color.White.copy(alpha = 0.75f), fontSize = 14.sp)
        Text(value, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun SpendingRow(icon: ImageVector, title: String, amount: String, progress: Float, color: Color) {
    Column(modifier = Modifier.padding(bottom = 12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(25.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text(title, color = Color.White, fontSize = 20.sp, modifier = Modifier.weight(1f))
            Text(amount, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
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
            .background(Brush.horizontalGradient(listOf(FinTrackLime, FinTrackTeal)), RoundedCornerShape(10.dp))
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(icon, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.width(8.dp))
        Text(label, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun BottomDashboardNav(
    navController: NavController,
    userId: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(76.dp)
            .background(Color(0xEE071827))
            .padding(horizontal = 10.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomItem(Icons.Default.Home, "Dashboard", true) {
            navController.navigate("dashboard/$userId") { launchSingleTop = true }
        }

        BottomItem(Icons.Outlined.CreditCard, "Expenses", false) {
            navController.navigate("expense_list/$userId") { launchSingleTop = true }
        }

        BottomItem(Icons.Outlined.Folder, "Categories", false) {
            navController.navigate("categories/$userId") { launchSingleTop = true }
        }

        BottomItem(Icons.Default.Settings, "Settings", false) {
            navController.navigate("settings/$userId") { launchSingleTop = true }
        }
    }
}

@Composable
private fun BottomItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (selected) FinTrackMint else Color.White.copy(alpha = 0.65f),
            modifier = Modifier.size(28.dp)
        )

        Text(
            text = label,
            color = if (selected) FinTrackMint else Color.White.copy(alpha = 0.65f),
            fontSize = 13.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
        )
    }
}