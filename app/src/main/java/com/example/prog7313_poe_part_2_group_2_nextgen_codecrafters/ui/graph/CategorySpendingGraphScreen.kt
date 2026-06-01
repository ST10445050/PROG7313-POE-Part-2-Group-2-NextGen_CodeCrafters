package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.graph

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.max

data class CategorySpendingGraphData(
    val categoryName: String,
    val totalSpent: Double
)

@Composable
fun CategorySpendingGraphScreen(
    userId: Int,
    expenseDao: ExpenseDao,
    budgetGoalDao: BudgetGoalDao,
    navController: NavController
) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)

    var userName by remember { mutableStateOf("User") }
    var showMenu by remember { mutableStateOf(false) }

    var selectedFilter by remember { mutableStateOf("This Month") }

    // The default analytics view shows the full current month.
    var startDate by remember { mutableStateOf(getMonthStartDate()) }
    var endDate by remember { mutableStateOf(getMonthEndDate()) }

    var showCustomDatePicker by remember { mutableStateOf(false) }

    var graphData by remember { mutableStateOf<List<CategorySpendingGraphData>>(emptyList()) }
    var selectedBarIndex by remember { mutableIntStateOf(-1) }
    var zoomScale by remember { mutableStateOf(1f) }

    var budgetGoal by remember { mutableStateOf<BudgetGoal?>(null) }

    LaunchedEffect(userId) {
        userName = db.userDao().getUserById(userId)?.name ?: "User"
    }

    // Reloads category totals and the correct monthly budget goal whenever the date filter changes.
    LaunchedEffect(userId, startDate, endDate) {
        val totals = withContext(Dispatchers.IO) {
            expenseDao.getTotalSpentByCategory(
                userId = userId,
                startDate = startDate,
                endDate = endDate
            )
        }

        graphData = totals.map {
            CategorySpendingGraphData(
                categoryName = it.categoryName,
                totalSpent = it.totalAmount.toDouble()
            )
        }

        selectedBarIndex = -1

        // Budget goals are monthly.
        // If the selected filter range includes today, the app uses today's month/year.
        // This fixes "This Week" when the week starts in the previous month but today is in the current month.
        // If the user selects a custom range in another month, the app uses that selected range month.
        val monthYear = getBudgetGoalMonthAndYearForRange(startDate, endDate)

        budgetGoal = if (monthYear != null) {
            withContext(Dispatchers.IO) {
                budgetGoalDao.getBudgetGoal(
                    userId = userId,
                    month = monthYear.first,
                    year = monthYear.second
                )
            }
        } else {
            null
        }
    }

    val totalSpent = graphData.sumOf { it.totalSpent }
    val highestCategory = graphData.maxByOrNull { it.totalSpent }
    val selectedCategory = graphData.getOrNull(selectedBarIndex)

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
                    .padding(top = 16.dp, bottom = 18.dp)
            ) {
                Text(
                    text = "Analytics",
                    color = Color.White,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Explore your spending by category.",
                    color = Color(0xFF65D6D0),
                    fontSize = 15.sp,
                    modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                )

                DateFilterCard(
                    selectedFilter = selectedFilter,
                    startDate = startDate,
                    endDate = endDate,
                    onFilterSelected = { filter ->
                        when (filter) {
                            "All" -> {
                                selectedFilter = "All"
                                startDate = "2020-01-01"
                                endDate = "2030-12-31"
                            }

                            "Today" -> {
                                selectedFilter = "Today"
                                startDate = getTodayDate()
                                endDate = getTodayDate()
                            }

                            "This Week" -> {
                                selectedFilter = "This Week"

                                // Always Sunday to Saturday based on the device/emulator system date.
                                startDate = getWeekStartDate()
                                endDate = getWeekEndDate()
                            }

                            "This Month" -> {
                                selectedFilter = "This Month"

                                // Full current month: first day to last day.
                                startDate = getMonthStartDate()
                                endDate = getMonthEndDate()
                            }

                            "Custom" -> {
                                showCustomDatePicker = true
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                CompactAnalyticsSummaryCard(
                    totalSpent = totalSpent,
                    highestCategory = highestCategory,
                    selectedCategory = selectedCategory
                )

                Spacer(modifier = Modifier.height(8.dp))

                GraphCard(
                    graphData = graphData,
                    selectedBarIndex = selectedBarIndex,
                    onBarSelected = { selectedBarIndex = it },
                    zoomScale = zoomScale,
                    onZoomIn = {
                        zoomScale = (zoomScale + 0.25f).coerceAtMost(2.5f)
                    },
                    onZoomOut = {
                        zoomScale = (zoomScale - 0.25f).coerceAtLeast(1f)
                    },
                    minGoal = budgetGoal?.minimumGoal,
                    maxGoal = budgetGoal?.maximumGoal
                )
            }
        }

        SharedBottomNav(
            navController = navController,
            userId = userId,
            currentScreen = "analytics",
            modifier = Modifier.align(Alignment.BottomCenter)
        )

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
                onLogoutClick = {
                    showMenu = false
                    navController.navigate("landing") {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        if (showCustomDatePicker) {
            CustomAnalyticsDateRangeDialog(
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
private fun AnalyticsCard(
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xCC101B2D), RoundedCornerShape(16.dp))
            .border(1.dp, Color.White.copy(alpha = 0.10f), RoundedCornerShape(16.dp))
            .padding(12.dp),
        content = content
    )
}

@Composable
private fun DateFilterCard(
    selectedFilter: String,
    startDate: String,
    endDate: String,
    onFilterSelected: (String) -> Unit
) {
    AnalyticsCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = null,
                tint = Color(0xFF65D6D0),
                modifier = Modifier.size(21.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Filter by Date",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = if (selectedFilter == "All") {
                "Showing all recorded expenses"
            } else {
                "Showing $startDate to $endDate"
            },
            color = Color.White.copy(alpha = 0.70f),
            fontSize = 11.sp
        )

        Spacer(modifier = Modifier.height(7.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            listOf("All", "Today", "This Week", "This Month", "Custom").forEach { filter ->
                FilterChip(
                    selected = selectedFilter == filter,
                    onClick = { onFilterSelected(filter) },
                    label = {
                        Text(
                            text = filter,
                            fontSize = 11.sp,
                            fontWeight = if (selectedFilter == filter) {
                                FontWeight.Bold
                            } else {
                                FontWeight.Medium
                            }
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

@Composable
private fun CompactAnalyticsSummaryCard(
    totalSpent: Double,
    highestCategory: CategorySpendingGraphData?,
    selectedCategory: CategorySpendingGraphData?
) {
    AnalyticsCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Analytics,
                contentDescription = null,
                tint = Color(0xFF65D6D0),
                modifier = Modifier.size(22.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Summary",
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(7.dp))

        SummaryRow(
            label = "Total Spent",
            value = "R ${String.format(Locale.getDefault(), "%.2f", totalSpent)}"
        )

        SummaryRow(
            label = "Highest",
            value = highestCategory?.let {
                "${it.categoryName} - R ${String.format(Locale.getDefault(), "%.2f", it.totalSpent)}"
            } ?: "No data yet"
        )

        if (selectedCategory != null) {
            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Selected: ${selectedCategory.categoryName} - R ${
                    String.format(Locale.getDefault(), "%.2f", selectedCategory.totalSpent)
                }",
                color = Color(0xFFA6F22E),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }
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
            .padding(vertical = 1.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.72f),
            fontSize = 12.sp
        )

        Text(
            text = value,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End,
            modifier = Modifier.widthIn(max = 220.dp)
        )
    }
}

@Composable
private fun GraphCard(
    graphData: List<CategorySpendingGraphData>,
    selectedBarIndex: Int,
    onBarSelected: (Int) -> Unit,
    zoomScale: Float,
    onZoomIn: () -> Unit,
    onZoomOut: () -> Unit,
    minGoal: Double?,
    maxGoal: Double?
) {
    AnalyticsCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Amount Spent per Category",
                color = Color.White,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            Button(
                onClick = onZoomOut,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ZoomOut,
                    contentDescription = "Zoom out",
                    tint = Color.White
                )
            }

            Button(
                onClick = onZoomIn,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ZoomIn,
                    contentDescription = "Zoom in",
                    tint = Color(0xFF65D6D0)
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Tap a bar to inspect it. Zoom in and drag sideways if needed.",
            color = Color.White.copy(alpha = 0.68f),
            fontSize = 11.sp
        )

        Spacer(modifier = Modifier.height(7.dp))

        // Small goal panel inside the graph card.
        // The graph still shows goal indicators, but this panel guarantees goals are always visible.
        GoalRangePanel(
            minGoal = minGoal,
            maxGoal = maxGoal
        )

        Spacer(modifier = Modifier.height(9.dp))

        if (graphData.isEmpty()) {
            EmptyGraphState()
        } else {
            val chartWidthDp = max(420f, graphData.size * 90f * zoomScale)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(430.dp)
                    .background(Color(0xAA07111F), RoundedCornerShape(14.dp))
                    .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(14.dp))
                    .padding(10.dp)
                    .horizontalScroll(rememberScrollState())
            ) {
                InteractiveCategoryBarChart(
                    data = graphData,
                    selectedBarIndex = selectedBarIndex,
                    onBarSelected = onBarSelected,
                    minGoal = minGoal,
                    maxGoal = maxGoal,
                    modifier = Modifier
                        .width(chartWidthDp.dp)
                        .fillMaxHeight()
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                GoalLegend(
                    color = Color(0xFF65D6D0),
                    label = "Minimum goal"
                )

                GoalLegend(
                    color = Color(0xFFE04F5F),
                    label = "Maximum goal"
                )
            }
        }
    }
}

@Composable
private fun GoalRangePanel(
    minGoal: Double?,
    maxGoal: Double?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0x9907111F), RoundedCornerShape(10.dp))
            .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(10.dp))
            .padding(horizontal = 8.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        GoalValueBadge(
            title = "Min",
            value = minGoal,
            color = Color(0xFF65D6D0),
            modifier = Modifier.weight(1f)
        )

        GoalValueBadge(
            title = "Max",
            value = maxGoal,
            color = Color(0xFFE04F5F),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun GoalValueBadge(
    title: String,
    value: Double?,
    color: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(color.copy(alpha = 0.11f), RoundedCornerShape(8.dp))
            .border(1.dp, color.copy(alpha = 0.55f), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            color = color,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = value?.let {
                "R${it.toInt()}"
            } ?: "Not set",
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun EmptyGraphState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(330.dp)
            .background(Color(0xAA07111F), RoundedCornerShape(14.dp))
            .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(14.dp))
            .padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = Color(0xFF65D6D0),
            modifier = Modifier.size(42.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "No spending data found for this period.",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Try changing the date filter or add expenses first.",
            color = Color.White.copy(alpha = 0.70f),
            fontSize = 13.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun GoalLegend(
    color: Color,
    label: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(9.dp)
                .background(color, RoundedCornerShape(50))
        )

        Spacer(modifier = Modifier.width(5.dp))

        Text(
            text = label,
            color = Color.White.copy(alpha = 0.72f),
            fontSize = 11.sp
        )
    }
}

@Composable
private fun InteractiveCategoryBarChart(
    data: List<CategorySpendingGraphData>,
    selectedBarIndex: Int,
    onBarSelected: (Int) -> Unit,
    minGoal: Double?,
    maxGoal: Double?,
    modifier: Modifier = Modifier
) {
    var canvasSize by remember { mutableStateOf(IntSize.Zero) }

    val selectedBarColor = Color(0xFFA6F22E)
    val axisColor = Color.White.copy(alpha = 0.38f)
    val gridColor = Color.White.copy(alpha = 0.10f)
    val minGoalColor = Color(0xFF65D6D0)
    val maxGoalColor = Color(0xFFE04F5F)

    Canvas(
        modifier = modifier
            .onSizeChanged { canvasSize = it }
            .pointerInput(data, canvasSize, minGoal, maxGoal) {
                detectTapGestures(
                    onTap = { offset ->
                        if (data.isEmpty() || canvasSize.width == 0 || canvasSize.height == 0) {
                            onBarSelected(-1)
                            return@detectTapGestures
                        }

                        // These values must match the chart drawing padding below.
                        val leftPadding = 92f
                        val rightPadding = 30f
                        val topPadding = 44f
                        val bottomPadding = 64f

                        val chartWidth = canvasSize.width - leftPadding - rightPadding
                        val chartHeight = canvasSize.height - topPadding - bottomPadding

                        val maxSpent = data.maxOfOrNull { it.totalSpent } ?: 0.0
                        val maxGoalValue = maxOf(minGoal ?: 0.0, maxGoal ?: 0.0)

                        val shouldIncludeGoalsInAxis =
                            maxGoalValue > 0.0 && maxGoalValue <= maxSpent * 2.5

                        val rawMax = when {
                            maxSpent <= 0.0 && maxGoalValue > 0.0 -> maxGoalValue
                            shouldIncludeGoalsInAxis -> maxOf(maxSpent, maxGoalValue)
                            else -> maxOf(maxSpent, 1.0)
                        }

                        val axisStep = getNiceAxisStep(rawMax)
                        val maxValue = axisStep * 5

                        val slotWidth = chartWidth / data.size
                        val barWidth = slotWidth * 0.50f

                        var tappedBarIndex = -1

                        data.forEachIndexed { index, item ->
                            val barHeight = ((item.totalSpent / maxValue).toFloat() * chartHeight)

                            val barLeft = leftPadding + (slotWidth * index) + ((slotWidth - barWidth) / 2f)
                            val barRight = barLeft + barWidth
                            val barTop = topPadding + chartHeight - barHeight
                            val barBottom = topPadding + chartHeight

                            val tappedInsideBar =
                                offset.x >= barLeft &&
                                        offset.x <= barRight &&
                                        offset.y >= barTop &&
                                        offset.y <= barBottom

                            if (tappedInsideBar) {
                                tappedBarIndex = index
                            }
                        }

                        // If the user tapped a real bar, select it.
                        // If the user tapped empty graph space, clear the selection.
                        onBarSelected(tappedBarIndex)
                    }
                )
            }
    ) {
        val leftPadding = 92f
        val rightPadding = 30f
        val topPadding = 44f
        val bottomPadding = 64f

        val chartWidth = size.width - leftPadding - rightPadding
        val chartHeight = size.height - topPadding - bottomPadding

        val maxSpent = data.maxOfOrNull { it.totalSpent } ?: 0.0
        val maxGoalValue = maxOf(minGoal ?: 0.0, maxGoal ?: 0.0)

        // The chart mainly scales according to spending.
        // Goals only stretch the axis if they are close enough to the spending values.
        val shouldIncludeGoalsInAxis = maxGoalValue > 0.0 && maxGoalValue <= maxSpent * 2.5

        val rawMax = when {
            maxSpent <= 0.0 && maxGoalValue > 0.0 -> maxGoalValue
            shouldIncludeGoalsInAxis -> maxOf(maxSpent, maxGoalValue)
            else -> maxOf(maxSpent, 1.0)
        }

        val axisStep = getNiceAxisStep(rawMax)
        val maxValue = axisStep * 5

        val nativePaint = android.graphics.Paint().apply {
            color = android.graphics.Color.WHITE
            textSize = 25f
            isAntiAlias = true
            textAlign = android.graphics.Paint.Align.CENTER
        }

        val smallPaint = android.graphics.Paint().apply {
            color = android.graphics.Color.WHITE
            textSize = 22f
            isAntiAlias = true
            textAlign = android.graphics.Paint.Align.RIGHT
        }

        val goalLabelPaint = android.graphics.Paint().apply {
            color = android.graphics.Color.WHITE

            // Smaller goal label text so labels do not cover the bar values.
            textSize = 18f

            isAntiAlias = true
            textAlign = android.graphics.Paint.Align.CENTER
            isFakeBoldText = true
        }

        repeat(6) { index ->
            val y = topPadding + (chartHeight / 5f) * index
            val value = maxValue - (axisStep * index)

            drawLine(
                color = gridColor,
                start = Offset(leftPadding, y),
                end = Offset(size.width - rightPadding, y),
                strokeWidth = 1.5f
            )

            drawContext.canvas.nativeCanvas.drawText(
                "R${value.toInt()}",
                leftPadding - 14f,
                y + 8f,
                smallPaint
            )
        }

        drawLine(
            color = axisColor,
            start = Offset(leftPadding, topPadding),
            end = Offset(leftPadding, topPadding + chartHeight),
            strokeWidth = 2f
        )

        drawLine(
            color = axisColor,
            start = Offset(leftPadding, topPadding + chartHeight),
            end = Offset(size.width - rightPadding, topPadding + chartHeight),
            strokeWidth = 2f
        )

        fun drawGoalIndicator(
            goal: Double?,
            color: Color,
            label: String,
            verticalOffset: Float
        ) {
            if (goal == null || goal <= 0.0) return

            val goalFitsInsideChart = goal <= maxValue

            val y = if (goalFitsInsideChart) {
                topPadding + chartHeight - ((goal / maxValue).toFloat() * chartHeight)
            } else {
                // If the goal is above the visible chart scale, show a badge near the top.
                topPadding - 30f + verticalOffset
            }

            if (goalFitsInsideChart) {
                drawLine(
                    color = color,
                    start = Offset(leftPadding, y),
                    end = Offset(size.width - rightPadding, y),
                    strokeWidth = 5.5f
                )

                drawCircle(
                    color = color,
                    radius = 7f,
                    center = Offset(leftPadding, y)
                )
            }

            val finalLabel = if (goalFitsInsideChart) label else "$label ↑"

            // Smaller label badge, moved further right so it does not cover the bars or bar values.
            val labelWidth = 104f
            val labelHeight = 26f
            val labelLeft = size.width - labelWidth - 6f
            val labelTop = y.coerceIn(4f, topPadding + chartHeight - labelHeight)

            drawRoundRect(
                color = color.copy(alpha = 0.95f),
                topLeft = Offset(labelLeft, labelTop),
                size = Size(labelWidth, labelHeight),
                cornerRadius = CornerRadius(9f, 9f)
            )

            drawRoundRect(
                color = Color.White.copy(alpha = 0.75f),
                topLeft = Offset(labelLeft, labelTop),
                size = Size(labelWidth, labelHeight),
                cornerRadius = CornerRadius(9f, 9f),
                style = Stroke(width = 1.5f)
            )

            drawContext.canvas.nativeCanvas.drawText(
                finalLabel,
                labelLeft + labelWidth / 2f,

                // Adjusted because the label badge is smaller.
                labelTop + 18f,

                goalLabelPaint
            )
        }

        // Minimum goal is light blue; maximum goal is red.
        // Both are always displayed as indicators, either as actual lines or top badges.
        drawGoalIndicator(
            goal = minGoal,
            color = minGoalColor,
            label = "MIN R${minGoal?.toInt() ?: 0}",
            verticalOffset = 0f
        )

        drawGoalIndicator(
            goal = maxGoal,
            color = maxGoalColor,
            label = "MAX R${maxGoal?.toInt() ?: 0}",
            verticalOffset = 34f
        )

        val slotWidth = chartWidth / data.size
        val barWidth = slotWidth * 0.50f

        data.forEachIndexed { index, item ->
            val barHeight = ((item.totalSpent / maxValue).toFloat() * chartHeight)
            val left = leftPadding + (slotWidth * index) + ((slotWidth - barWidth) / 2f)
            val top = topPadding + chartHeight - barHeight

            val normalBarColor = getCategoryGraphColor(item.categoryName, index)
            val barColorToUse = if (index == selectedBarIndex) {
                selectedBarColor
            } else {
                normalBarColor
            }

            drawRoundRect(
                brush = Brush.verticalGradient(
                    listOf(
                        barColorToUse,
                        barColorToUse.copy(alpha = 0.55f)
                    )
                ),
                topLeft = Offset(left, top),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(12f, 12f)
            )

            if (index == selectedBarIndex) {
                drawRoundRect(
                    color = Color.White.copy(alpha = 0.82f),
                    topLeft = Offset(left - 4f, top - 4f),
                    size = Size(barWidth + 8f, barHeight + 8f),
                    cornerRadius = CornerRadius(14f, 14f),
                    style = Stroke(width = 3f)
                )
            }

            drawContext.canvas.nativeCanvas.drawText(
                "R${item.totalSpent.toInt()}",
                left + barWidth / 2f,
                top - 10f,
                nativePaint
            )

            drawContext.canvas.nativeCanvas.save()

            drawContext.canvas.nativeCanvas.rotate(
                -28f,
                left + barWidth / 2f,
                topPadding + chartHeight + 44f
            )

            drawContext.canvas.nativeCanvas.drawText(
                item.categoryName.take(10),
                left + barWidth / 2f,
                topPadding + chartHeight + 44f,
                nativePaint
            )

            drawContext.canvas.nativeCanvas.restore()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CustomAnalyticsDateRangeDialog(
    startDate: String,
    endDate: String,
    onDismiss: () -> Unit,
    onSearch: (String, String) -> Unit
) {
    val dateRangePickerState = rememberDateRangePickerState(
        initialSelectedStartDateMillis = parseDateToMillis(startDate),
        initialSelectedEndDateMillis = parseDateToMillis(endDate),
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
                .heightIn(min = 480.dp, max = 560.dp)
                .padding(12.dp),
            title = {
                Text(
                    text = "Select Analytics Date Range",
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

private fun getTodayDate(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(Date())
}

private fun getWeekStartDate(): String {
    val calendar = Calendar.getInstance()
    calendar.firstDayOfWeek = Calendar.SUNDAY
    calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)

    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(calendar.time)
}

private fun getWeekEndDate(): String {
    val calendar = Calendar.getInstance()
    calendar.firstDayOfWeek = Calendar.SUNDAY
    calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY)

    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(calendar.time)
}

private fun getMonthStartDate(): String {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.DAY_OF_MONTH, 1)

    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(calendar.time)
}

private fun getMonthEndDate(): String {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))

    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(calendar.time)
}

private fun parseDateToMillis(date: String): Long? {
    return try {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            .parse(date)
            ?.time
    } catch (_: Exception) {
        null
    }
}

private fun formatDateForDatabase(milliseconds: Long): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(Date(milliseconds))
}

private fun getMonthAndYearFromDate(date: String): Pair<Int, Int>? {
    return try {
        val parsedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(date)
        val calendar = Calendar.getInstance()
        calendar.time = parsedDate ?: return null

        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)

        Pair(month, year)
    } catch (_: Exception) {
        null
    }
}

private fun getBudgetGoalMonthAndYearForRange(
    startDate: String,
    endDate: String
): Pair<Int, Int>? {
    val today = getTodayDate()

    val dateForGoal = if (today >= startDate && today <= endDate) {
        today
    } else {
        startDate
    }

    return getMonthAndYearFromDate(dateForGoal)
}

private fun getNiceAxisStep(maxAmount: Double): Double {
    return when {
        maxAmount <= 500 -> 100.0
        maxAmount <= 1000 -> 200.0
        maxAmount <= 2500 -> 500.0
        maxAmount <= 5000 -> 1000.0
        maxAmount <= 10000 -> 2000.0
        else -> 5000.0
    }
}

private fun getCategoryGraphColor(
    categoryName: String,
    index: Int
): Color {
    val name = categoryName.lowercase(Locale.getDefault())

    return when {
        name.contains("food") -> Color(0xFF65D6D0)
        name.contains("transport") -> Color(0xFFA6F22E)
        name.contains("grocery") || name.contains("groceries") -> Color(0xFFB075D6)
        name.contains("rent") || name.contains("housing") -> Color(0xFFE85FA3)
        name.contains("utility") || name.contains("electricity") || name.contains("water") -> Color(0xFFFFB547)
        name.contains("gym") || name.contains("fitness") -> Color(0xFF4DA3FF)
        name.contains("entertainment") || name.contains("movie") || name.contains("game") -> Color(0xFFFF6B6B)
        name.contains("shopping") || name.contains("clothing") || name.contains("clothes") -> Color(0xFFFF9F1C)
        name.contains("health") || name.contains("medical") -> Color(0xFF2EC4B6)
        name.contains("school") || name.contains("education") -> Color(0xFF9B5DE5)

        else -> listOf(
            Color(0xFF65D6D0),
            Color(0xFFA6F22E),
            Color(0xFFB075D6),
            Color(0xFFE85FA3),
            Color(0xFFFFB547),
            Color(0xFF4DA3FF),
            Color(0xFFFF6B6B),
            Color(0xFFFF9F1C)
        )[index % 8]
    }
}