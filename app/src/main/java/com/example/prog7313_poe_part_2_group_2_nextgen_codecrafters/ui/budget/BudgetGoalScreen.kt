package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.budget

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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingFlat
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import androidx.navigation.NavController
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.R
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.dao.BudgetGoalDao
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.dao.ExpenseDao
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.database.AppDatabase
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.entities.BudgetGoal
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.components.SharedBottomNav
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.components.SharedSideMenu
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.components.SharedTopBar
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetGoalScreen(
    userId: Int,
    budgetGoalDao: BudgetGoalDao,
    expenseDao: ExpenseDao,
    navController: NavController
) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val scope = rememberCoroutineScope()

    var userName by remember { mutableStateOf("User") }
    var showMenu by remember { mutableStateOf(false) }

    LaunchedEffect(userId) {
        userName = db.userDao().getUserById(userId)?.name ?: "User"
    }

    val months = listOf(
        "January", "February", "March", "April",
        "May", "June", "July", "August",
        "September", "October", "November", "December"
    )

    val currentCalendar = Calendar.getInstance()
    val currentYear = currentCalendar.get(Calendar.YEAR)
    val currentMonthIndex = currentCalendar.get(Calendar.MONTH)

    var selectedMonth by remember { mutableStateOf(months[currentMonthIndex]) }
    var selectedYear by remember { mutableStateOf(currentYear) }
    var monthExpanded by remember { mutableStateOf(false) }

    var minimumBudget by remember { mutableStateOf("") }
    var maximumBudget by remember { mutableStateOf("") }
    var feedbackMessage by remember { mutableStateOf("") }

    var savedGoal by remember { mutableStateOf<BudgetGoal?>(null) }

    val selectedMonthNumber = months.indexOf(selectedMonth) + 1
    val startDate = getMonthStartDate(selectedYear, selectedMonthNumber)
    val endDate = getMonthEndDate(selectedYear, selectedMonthNumber)

    // Pulls ALL expenses for this user in the selected month.
    // This is not category-specific. It includes every category.
    val monthlyExpenses by expenseDao.getExpensesForUserByDateRange(
        userId = userId,
        startDate = startDate,
        endDate = endDate
    ).collectAsState(initial = emptyList())

    // Adds all expense amounts together to get the total spent.
    val totalSpent = monthlyExpenses.sumOf { expense ->
        expense.amount
    }

    LaunchedEffect(userId, selectedMonthNumber, selectedYear) {
        val goal = budgetGoalDao.getBudgetGoal(
            userId = userId,
            month = selectedMonthNumber,
            year = selectedYear
        )

        savedGoal = goal

        if (goal != null) {
            minimumBudget = goal.minimumGoal.toString()
            maximumBudget = goal.maximumGoal.toString()
            feedbackMessage = "Saved goal loaded for $selectedMonth $selectedYear."
        } else {
            minimumBudget = ""
            maximumBudget = ""
            feedbackMessage = ""
        }
    }

    val minGoal = savedGoal?.minimumGoal ?: minimumBudget.toDoubleOrNull()
    val maxGoal = savedGoal?.maximumGoal ?: maximumBudget.toDoubleOrNull()

    val insight = buildBudgetInsight(
        totalSpent = totalSpent,
        minimumGoal = minGoal,
        maximumGoal = maxGoal
    )

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
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 18.dp)
                    .padding(top = 22.dp, bottom = 24.dp)
            ) {
                Text(
                    text = "Budget Goals",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Set a minimum and maximum spending goal for each month.",
                    color = Color(0xFF65D6D0),
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 6.dp, bottom = 18.dp)
                )

                BudgetCard {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.TrackChanges,
                            contentDescription = null,
                            tint = Color(0xFF65D6D0),
                            modifier = Modifier.size(28.dp)
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Text(
                            text = "Monthly Goal Setup",
                            color = Color.White,
                            fontSize = 21.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    FieldLabel("Select Month")

                    ExposedDropdownMenuBox(
                        expanded = monthExpanded,
                        onExpandedChange = { monthExpanded = !monthExpanded }
                    ) {
                        OutlinedTextField(
                            value = selectedMonth,
                            onValueChange = {},
                            readOnly = true,
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
                            shape = RoundedCornerShape(10.dp)
                        )

                        ExposedDropdownMenu(
                            expanded = monthExpanded,
                            onDismissRequest = { monthExpanded = false }
                        ) {
                            months.forEach { month ->
                                DropdownMenuItem(
                                    text = { Text(month) },
                                    onClick = {
                                        selectedMonth = month
                                        monthExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    FieldLabel("Minimum Monthly Goal")

                    OutlinedTextField(
                        value = minimumBudget,
                        onValueChange = { minimumBudget = it },
                        placeholder = {
                            Text(
                                text = "Example: 1000",
                                color = Color.White.copy(alpha = 0.45f)
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = fieldColors(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    FieldLabel("Maximum Monthly Goal")

                    OutlinedTextField(
                        value = maximumBudget,
                        onValueChange = { maximumBudget = it },
                        placeholder = {
                            Text(
                                text = "Example: 3000",
                                color = Color.White.copy(alpha = 0.45f)
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = fieldColors(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    if (feedbackMessage.isNotBlank()) {
                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = feedbackMessage,
                            color = Color.White.copy(alpha = 0.78f),
                            fontSize = 13.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Button(
                        onClick = {
                            val minValue = minimumBudget.toDoubleOrNull()
                            val maxValue = maximumBudget.toDoubleOrNull()

                            when {
                                selectedMonthNumber !in 1..12 -> {
                                    feedbackMessage = "Please select a valid month."
                                }

                                minValue == null || maxValue == null -> {
                                    feedbackMessage = "Please enter valid numeric goal amounts."
                                }

                                minValue < 0 || maxValue < 0 -> {
                                    feedbackMessage = "Goal amounts cannot be negative."
                                }

                                minValue > maxValue -> {
                                    feedbackMessage = "Minimum goal cannot be greater than maximum goal."
                                }

                                else -> {
                                    scope.launch {
                                        val goal = BudgetGoal(
                                            userId = userId,
                                            month = selectedMonthNumber,
                                            year = selectedYear,
                                            minimumGoal = minValue,
                                            maximumGoal = maxValue
                                        )

                                        budgetGoalDao.insertBudgetGoal(goal)
                                        savedGoal = goal
                                        feedbackMessage = "Budget goal saved for $selectedMonth $selectedYear."
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .height(56.dp)
                            .shadow(10.dp, RoundedCornerShape(28.dp)),
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
                                .padding(horizontal = 28.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Save,
                                contentDescription = null,
                                tint = Color.White
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = "Save Goal",
                                color = Color.White,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                BudgetSummaryCard(
                    selectedMonth = selectedMonth,
                    selectedYear = selectedYear,
                    totalSpent = totalSpent,
                    minimumGoal = minGoal,
                    maximumGoal = maxGoal
                )

                Spacer(modifier = Modifier.height(18.dp))

                BudgetInsightCard(
                    insightTitle = insight.title,
                    insightMessage = insight.message,
                    insightTips = insight.tips,
                    icon = insight.icon,
                    iconColor = insight.iconColor
                )
            }
        }

        SharedBottomNav(
            navController = navController,
            userId = userId,
            currentScreen = "settings",
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
private fun BudgetCard(
    content: @Composable ColumnScope.() -> Unit
) {
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
private fun BudgetSummaryCard(
    selectedMonth: String,
    selectedYear: Int,
    totalSpent: Double,
    minimumGoal: Double?,
    maximumGoal: Double?
) {
    BudgetCard {
        Text(
            text = "$selectedMonth $selectedYear Summary",
            color = Color.White,
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(14.dp))

        SummaryRow("Total Spent", "R ${String.format("%.2f", totalSpent)}")
        SummaryRow("Minimum Goal", minimumGoal?.let { "R ${String.format("%.2f", it)}" } ?: "Not set")
        SummaryRow("Maximum Goal", maximumGoal?.let { "R ${String.format("%.2f", it)}" } ?: "Not set")
    }
}

@Composable
private fun SummaryRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.72f),
            fontSize = 15.sp
        )

        Text(
            text = value,
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun BudgetInsightCard(
    insightTitle: String,
    insightMessage: String,
    insightTips: List<String>,
    icon: ImageVector,
    iconColor: Color
) {
    BudgetCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(30.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = insightTitle,
                color = Color.White,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = insightMessage,
            color = Color.White.copy(alpha = 0.85f),
            fontSize = 15.sp,
            lineHeight = 21.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        insightTips.forEach { tip ->
            Text(
                text = "• $tip",
                color = Color(0xFF65D6D0),
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 6.dp)
            )
        }
    }
}

private data class BudgetInsight(
    val title: String,
    val message: String,
    val tips: List<String>,
    val icon: ImageVector,
    val iconColor: Color
)

private fun buildBudgetInsight(
    totalSpent: Double,
    minimumGoal: Double?,
    maximumGoal: Double?
): BudgetInsight {
    return when {
        minimumGoal == null || maximumGoal == null -> {
            BudgetInsight(
                title = "Goal Not Set Yet",
                message = "Set your minimum and maximum monthly spending goals to receive personalised feedback based on your actual expenses.",
                tips = listOf(
                    "Choose a realistic minimum amount for essential spending.",
                    "Choose a maximum amount that prevents overspending.",
                    "Review your expenses weekly after saving your goal."
                ),
                icon = Icons.Default.TrackChanges,
                iconColor = Color(0xFF65D6D0)
            )
        }

        totalSpent < minimumGoal -> {
            BudgetInsight(
                title = "Below Minimum Goal",
                message = "You have spent less than your minimum goal for this month. This means your spending is currently very low compared to your planned range.",
                tips = listOf(
                    "Check whether all expenses for this month have been recorded.",
                    "Keep saving where possible, but make sure essential needs are covered.",
                    "Use the remaining room carefully for planned spending."
                ),
                icon = Icons.Default.TrendingDown,
                iconColor = Color(0xFFA6F22E)
            )
        }

        totalSpent in minimumGoal..maximumGoal -> {
            BudgetInsight(
                title = "Within Goal Range",
                message = "You are currently within your monthly spending goal range. This means your spending is controlled and aligned with your budget plan.",
                tips = listOf(
                    "Continue tracking each new expense.",
                    "Avoid unnecessary spending near the end of the month.",
                    "Compare weekly spending to stay consistent."
                ),
                icon = Icons.Default.TrendingFlat,
                iconColor = Color(0xFF65D6D0)
            )
        }

        else -> {
            BudgetInsight(
                title = "Above Maximum Goal",
                message = "You have exceeded your maximum monthly spending goal. This means your spending is higher than planned for the selected month.",
                tips = listOf(
                    "Review non-essential expenses first.",
                    "Reduce spending on categories such as takeaways, shopping, or entertainment.",
                    "Use your expense list to identify where most of the money went."
                ),
                icon = Icons.Default.TrendingUp,
                iconColor = Color(0xFFE04F5F)
            )
        }
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

private fun getMonthStartDate(year: Int, month: Int): String {
    return "%04d-%02d-01".format(year, month)
}

private fun getMonthEndDate(year: Int, month: Int): String {
    val calendar = Calendar.getInstance()

    calendar.set(Calendar.YEAR, year)
    calendar.set(Calendar.MONTH, month - 1)
    calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))

    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(calendar.time)
}