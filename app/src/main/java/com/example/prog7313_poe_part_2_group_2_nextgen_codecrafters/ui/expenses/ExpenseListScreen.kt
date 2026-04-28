package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.expenses

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.database.ExpenseDatabase
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.entities.Expense
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseListScreen(userId: Int = 1) {
    val context = LocalContext.current
    val dao = remember { ExpenseDatabase.getDatabase(context).expenseDao() }

    var selectedFilter by remember { mutableStateOf("This Month") }
    val showDatePickerState = remember { mutableStateOf(false) }

    var startDate by remember { mutableStateOf(getMonthStartDate()) }
    var endDate by remember { mutableStateOf(getTodayDate()) }

    val expenses by remember(userId, startDate, endDate) {
        dao.getExpensesForUserByDateRange(
            userId = userId,
            startDate = startDate,
            endDate = endDate
        )
    }.collectAsState(initial = emptyList())

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF0A0E14))) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("FinTrack", color = Color(0xFF4DB6AC), fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Surface(
                    shape = androidx.compose.foundation.shape.CircleShape,
                    color = Color.Gray.copy(alpha = 0.3f),
                    modifier = Modifier.size(32.dp)
                ) {}
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Expenses", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Text("Track and manage your spending", color = Color.Gray, fontSize = 14.sp)
            
            Text(
                text = "Period: $startDate to $endDate",
                color = Color(0xFF4DB6AC).copy(alpha = 0.7f),
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Filter Section
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.DateRange, contentDescription = null, tint = Color(0xFF4DB6AC))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Filter by Date", color = Color.White, modifier = Modifier.weight(1f))
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Color.Gray)
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Today", "This Week", "This Month", "Custom").forEach { filter ->
                            FilterChip(
                                selected = selectedFilter == filter,
                                onClick = {
                                    if (filter == "Custom") {
                                        showDatePickerState.value = true
                                    } else {
                                        selectedFilter = filter
                                        when (filter) {
                                            "Today" -> {
                                                startDate = getTodayDate()
                                                endDate = getTodayDate()
                                            }
                                            "This Week" -> {
                                                startDate = getWeekStartDate()
                                                endDate = getTodayDate()
                                            }
                                            "This Month" -> {
                                                startDate = getMonthStartDate()
                                                endDate = getTodayDate()
                                            }
                                        }
                                    }
                                },
                                label = { Text(filter) },
                                colors = FilterChipDefaults.filterChipColors(
                                    containerColor = Color.Transparent,
                                    labelColor = Color.Gray,
                                    selectedContainerColor = Color(0xFF4DB6AC).copy(alpha = 0.2f),
                                    selectedLabelColor = Color(0xFF4DB6AC)
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = selectedFilter == filter,
                                    borderColor = Color.Gray.copy(alpha = 0.3f),
                                    selectedBorderColor = Color(0xFF4DB6AC)
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Expenses List
            if (expenses.isEmpty()) {
                Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text("No expenses found for this period", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(expenses) { expense ->
                        ExpenseItem(expense)
                    }
                }
            }
        }

        // Floating Action Button
        Button(
            onClick = { /* Add Expense Action */ },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
                .height(50.dp)
                .width(200.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8BC34A)),
            shape = RoundedCornerShape(25.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add Expense", fontWeight = FontWeight.Bold)
        }
    }

    if (showDatePickerState.value) {
        // Initialize the state with the current selection if possible
        val dateRangePickerState = rememberDateRangePickerState(
            initialSelectedStartDateMillis = parseDateToMillis(startDate),
            initialSelectedEndDateMillis = parseDateToMillis(endDate)
        )
        
        DatePickerDialog(
            onDismissRequest = { showDatePickerState.value = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val startMillis = dateRangePickerState.selectedStartDateMillis
                        val endMillis = dateRangePickerState.selectedEndDateMillis
                        if (startMillis != null && endMillis != null) {
                            startDate = formatDate(startMillis)
                            endDate = formatDate(endMillis)
                            selectedFilter = "Custom"
                            showDatePickerState.value = false
                        }
                    },
                    enabled = dateRangePickerState.selectedEndDateMillis != null
                ) {
                    Text("Search", color = if (dateRangePickerState.selectedEndDateMillis != null) Color(0xFF4DB6AC) else Color.Gray)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePickerState.value = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DateRangePicker(
                state = dateRangePickerState,
                modifier = Modifier.weight(1f).padding(16.dp),
                title = { Text("Select Date Range", modifier = Modifier.padding(bottom = 8.dp)) },
                headline = { 
                    val start = dateRangePickerState.selectedStartDateMillis?.let { formatDate(it) } ?: "Start"
                    val end = dateRangePickerState.selectedEndDateMillis?.let { formatDate(it) } ?: "End"
                    Text("$start - $end", fontSize = 18.sp)
                },
                showModeToggle = true, // Allows user to switch between calendar and text input
                colors = DatePickerDefaults.colors(
                    containerColor = Color(0xFF161B22),
                    titleContentColor = Color.White,
                    headlineContentColor = Color.White,
                    selectedDayContainerColor = Color(0xFF4DB6AC),
                    todayContentColor = Color(0xFF4DB6AC),
                    todayDateBorderColor = Color(0xFF4DB6AC),
                    dayContentColor = Color.White,
                    selectedDayContentColor = Color.Black
                )
            )
        }
    }
}

@Composable
fun ExpenseItem(expense: Expense) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22).copy(alpha = 0.5f)),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFF1E88E5).copy(alpha = 0.2f),
                modifier = Modifier.size(40.dp)
            ) {}
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(expense.description, color = Color.White, fontWeight = FontWeight.Medium)
                Text(expense.date, color = Color.Gray, fontSize = 12.sp)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("R ${expense.amount.toInt()}", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

fun formatDate(millis: Long): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    // Ensure UTC for consistency with DatePicker
    formatter.timeZone = TimeZone.getTimeZone("UTC")
    return formatter.format(Date(millis))
}

fun parseDateToMillis(dateStr: String): Long? {
    return try {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        formatter.parse(dateStr)?.time
    } catch (_: Exception) {
        null
    }
}

fun getTodayDate(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(Date())
}

fun getWeekStartDate(): String {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(calendar.time)
}

fun getMonthStartDate(): String {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(calendar.time)
}
