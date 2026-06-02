package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocalHospital
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.ShoppingBasket
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
<<<<<<< Updated upstream
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.database.AppDatabase
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.entities.QuestionnaireAnswers
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.entities.User
=======
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.remoteModels.ProfileDto
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.remoteModels.QuestionnaireAnswersDto
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.repository.ProfileRepository
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.repository.QuestionnaireRepository
>>>>>>> Stashed changes
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.components.SharedBottomNav
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.components.SharedSideMenu
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.components.SharedTopBar
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.expense.ExpenseViewModel
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.theme.*
<<<<<<< Updated upstream
=======
import java.util.Locale
>>>>>>> Stashed changes

@Composable
fun DashboardScreen(
    navController: NavController,
    userId: String,
    expenseViewModel: ExpenseViewModel
) {
    val profileRepository = remember { ProfileRepository() }
    val questionnaireRepository = remember { QuestionnaireRepository() }

    var profile by remember { mutableStateOf<ProfileDto?>(null) }
    var answers by remember { mutableStateOf<QuestionnaireAnswersDto?>(null) }
    var showMenu by remember { mutableStateOf(false) }

<<<<<<< Updated upstream
    // Loads all expenses for the logged-in user.
    val expenses by expenseViewModel
        .getExpensesForUser(userId)
        .collectAsState(initial = emptyList())

    // Loads user details and questionnaire answers when the screen opens.
=======
>>>>>>> Stashed changes
    LaunchedEffect(userId) {
        profile = profileRepository.getProfile(userId)
        answers = questionnaireRepository.getQuestionnaireAnswers(userId)
    }

<<<<<<< Updated upstream
    // Safely displays the user's name, or "User" if the database has no name.
    val userName = user?.name ?: "User"

    // Pulls budget values from the questionnaire answers.
    val monthlyBudget = answers?.monthlyIncome ?: 0.0
    val savingsGoal = answers?.monthlySavingsGoal ?: 0.0

    // Calculates how much the user has spent.
    val amountSpent = expenses.sumOf { it.amount }

    // Calculates the remaining amount from the monthly budget.
=======
    val userName = profile?.name ?: "User"

    val monthlyBudget = answers?.monthlyIncome ?: 0.0
    val savingsGoal = answers?.monthlySavingsGoal ?: 0.0

    val amountSpent = 0.0
>>>>>>> Stashed changes
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

<<<<<<< Updated upstream
    // Converts the stored comma-separated spending categories into a clean list.
    val categories = answers?.spendingCategories
=======
    val recentExpenses = emptyList<String>()

    val selectedCategories = answers?.selectedCategories
>>>>>>> Stashed changes
        ?.split(",")
        ?.map { it.trim() }
        ?.filter { it.isNotBlank() }
        ?: emptyList()
<<<<<<< Updated upstream

    // Shows the latest 3 expenses on the dashboard.
    val recentExpenses = expenses.takeLast(3).reversed()
=======
>>>>>>> Stashed changes

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

        Column(modifier = Modifier.fillMaxSize()) {
            SharedTopBar(
                onMenuClick = { showMenu = true },
                showBackButton = false
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 18.dp)
                    .padding(top = 22.dp, bottom = 110.dp)
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

<<<<<<< Updated upstream
                    if (categories.isEmpty()) {
=======
                    if (selectedCategories.isEmpty()) {
>>>>>>> Stashed changes
                        Text(
                            text = "No spending categories selected yet.",
                            color = Color.White.copy(alpha = 0.75f),
                            fontSize = 16.sp
                        )
                    } else {
<<<<<<< Updated upstream
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
=======
                        selectedCategories.take(4).forEachIndexed { index, category ->
                            SpendingRow(
                                icon = getDashboardCategoryIcon(category),
                                title = category,
                                amount = "R0",
                                progress = 0f,
                                color = getDashboardCategoryColor(category, index)
>>>>>>> Stashed changes
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
                            navController.navigate("budget_goals/$userId") {
                                launchSingleTop = true
                            }
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
                    }
                }
            }
        }

        SharedBottomNav(
            navController = navController,
            userId = userId,
            currentScreen = "dashboard",
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
<<<<<<< Updated upstream
=======
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
>>>>>>> Stashed changes
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