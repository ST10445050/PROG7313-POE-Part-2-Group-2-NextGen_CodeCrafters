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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberUpdatedState
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.R
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.remoteModels.ProfileDto
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.remoteModels.QuestionnaireAnswersDto
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.repository.ProfileRepository
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.repository.QuestionnaireRepository
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.components.SharedBottomNav
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.components.SharedSideMenu
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.components.SharedTopBar
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.expense.ExpenseViewModel
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.theme.FinTrackLime
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.theme.FinTrackMint
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.theme.FinTrackTeal
import java.util.Locale

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

    val lifecycleOwner = LocalLifecycleOwner.current
    val currentUserId by rememberUpdatedState(userId)

    LaunchedEffect(userId) {
        profile = profileRepository.getProfile(userId)
        answers = questionnaireRepository.getQuestionnaireAnswers(userId)
        expenseViewModel.loadExpenses(userId)
    }

    DisposableEffect(lifecycleOwner, userId) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                expenseViewModel.loadExpenses(currentUserId)
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val userName = profile?.name ?: "User"

    val monthlyBudget = answers?.monthlyIncome ?: 0.0
    val savingsGoal = answers?.monthlySavingsGoal ?: 0.0

    val expenseList = expenseViewModel.expenses

    val amountSpent = expenseList.sumOf { it.amount }
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

    val categoryTotals = expenseList
        .groupBy { it.categoryName }
        .mapValues { entry -> entry.value.sumOf { it.amount } }
        .toList()
        .sortedByDescending { it.second }

    val recentExpenses = expenseList.take(3)

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
                            text = "R${String.format("%.0f", monthlyBudget)}",
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
                        BudgetItem(
                            label = "Budget",
                            value = "R${String.format("%.0f", monthlyBudget)}",
                            modifier = Modifier.weight(1f)
                        )

                        BudgetItem(
                            label = "Spent",
                            value = "R${String.format("%.0f", amountSpent)}",
                            modifier = Modifier.weight(1f)
                        )

                        BudgetItem(
                            label = "Remaining",
                            value = "R${String.format("%.0f", remaining)}",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Savings Goal: R${String.format("%.0f", savingsGoal)}",
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

                    if (categoryTotals.isEmpty()) {
                        Text(
                            text = "No spending data yet. Add expenses to see your summary.",
                            color = Color.White.copy(alpha = 0.75f),
                            fontSize = 16.sp
                        )
                    } else {
                        categoryTotals.take(4).forEachIndexed { index, item ->
                            val categoryName = item.first
                            val totalAmount = item.second

                            SpendingRow(
                                icon = getDashboardCategoryIcon(categoryName),
                                title = categoryName,
                                amount = "R${String.format("%.2f", totalAmount)}",
                                progress = if (amountSpent > 0) {
                                    (totalAmount / amountSpent).toFloat().coerceIn(0f, 1f)
                                } else {
                                    0f
                                },
                                color = getDashboardCategoryColor(categoryName, index)
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
                            navController.navigate("analytics/$userId") {
                                launchSingleTop = true
                            }
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
                    } else {
                        recentExpenses.forEach { expense ->
                            RecentExpenseRow(
                                description = expense.description,
                                categoryName = expense.categoryName,
                                amount = expense.amount
                            )
                        }
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
private fun RecentExpenseRow(
    description: String,
    categoryName: String,
    amount: Double
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 7.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = description.ifBlank { "Expense" },
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )

            Text(
                text = categoryName,
                color = FinTrackMint,
                fontSize = 13.sp,
                maxLines = 1
            )
        }

        Text(
            text = "R${String.format("%.2f", amount)}",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
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

private fun getDashboardCategoryIcon(categoryName: String): ImageVector {
    val name = categoryName.lowercase(Locale.getDefault())

    return when {
        name.contains("grocery") || name.contains("groceries") ->
            Icons.Outlined.ShoppingBasket

        name.contains("transport") ||
                name.contains("uber") ||
                name.contains("taxi") ||
                name.contains("fuel") ->
            Icons.Outlined.DirectionsCar

        name.contains("food") ||
                name.contains("restaurant") ||
                name.contains("meal") ||
                name.contains("eating") ->
            Icons.Outlined.Restaurant

        name.contains("rent") ||
                name.contains("housing") ||
                name.contains("home") ->
            Icons.Outlined.Home

        name.contains("subscription") ||
                name.contains("bill") ||
                name.contains("receipt") ->
            Icons.Outlined.Receipt

        name.contains("shopping") ||
                name.contains("clothing") ||
                name.contains("clothes") ->
            Icons.Outlined.ShoppingCart

        name.contains("health") ||
                name.contains("medical") ->
            Icons.Outlined.LocalHospital

        name.contains("gym") ||
                name.contains("fitness") ->
            Icons.Outlined.FitnessCenter

        else ->
            Icons.Outlined.Category
    }
}

private fun getDashboardCategoryColor(
    categoryName: String,
    index: Int
): Color {
    val name = categoryName.lowercase(Locale.getDefault())

    return when {
        name.contains("food") -> FinTrackMint
        name.contains("transport") -> FinTrackLime
        name.contains("grocery") || name.contains("groceries") -> Color(0xFFB075D6)
        name.contains("rent") || name.contains("housing") -> Color(0xFFE85FA3)
        name.contains("subscription") || name.contains("bill") -> Color(0xFFFFB547)
        name.contains("gym") || name.contains("fitness") -> Color(0xFF4DA3FF)
        else -> listOf(
            FinTrackLime,
            FinTrackMint,
            Color(0xFFB075D6),
            Color(0xFFE85FA3),
            Color(0xFFFFB547),
            Color(0xFF4DA3FF)
        )[index % 6]
    }
}