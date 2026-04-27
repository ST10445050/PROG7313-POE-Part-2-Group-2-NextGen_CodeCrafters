package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.expenses

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.database.ExpenseDatabase
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.entities.Expense
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun ExpenseListScreen(userId: Int = 1) {
    val context = LocalContext.current
    val dao = remember {
        ExpenseDatabase.getDatabase(context).expenseDao()
    }

    var selectedFilter by remember { mutableStateOf("This Month") }
    var showCustomDialog by remember { mutableStateOf(false) }

    var startDate by remember { mutableStateOf(getMonthStartDate()) }
    var endDate by remember { mutableStateOf(getTodayDate()) }

    val expenses by dao.getExpensesForUserByDateRange(
        userId = userId,
        startDate = startDate,
        endDate = endDate
    ).collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Expenses",
            style = MaterialTheme.typography.headlineLarge
        )

        Text(
            text = "Track and manage your spending.",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Filter by Date",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            FilterButton(
                text = "Today",
                selected = selectedFilter == "Today",
                modifier = Modifier.weight(1f)
            ) {
                selectedFilter = "Today"
                startDate = getTodayDate()
                endDate = getTodayDate()
            }

            FilterButton(
                text = "This Week",
                selected = selectedFilter == "This Week",
                modifier = Modifier.weight(1f)
            ) {
                selectedFilter = "This Week"
                startDate = getWeekStartDate()
                endDate = getTodayDate()
            }

            FilterButton(
                text = "This Month",
                selected = selectedFilter == "This Month",
                modifier = Modifier.weight(1f)
            ) {
                selectedFilter = "This Month"
                startDate = getMonthStartDate()
                endDate = getTodayDate()
            }

            FilterButton(
                text = "Custom",
                selected = selectedFilter == "Custom",
                modifier = Modifier.weight(1f)
            ) {
                selectedFilter = "Custom"
                showCustomDialog = true
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Showing: $startDate to $endDate",
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (expenses.isEmpty()) {
            Text("No expenses found for this period.")
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(expenses) { expense ->
                    ExpenseCard(
                        expense = expense,
                        onPhotoClick = { photoPath ->
                            openExpensePhoto(context, photoPath)
                        }
                    )
                }
            }
        }
    }

    if (showCustomDialog) {
        CustomDateDialog(
            startDate = startDate,
            endDate = endDate,
            onStartDateChange = { startDate = it },
            onEndDateChange = { endDate = it },
            onDismiss = { showCustomDialog = false },
            onSearch = {
                selectedFilter = "Custom"
                showCustomDialog = false
            }
        )
    }
}

@Composable
fun FilterButton(
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    if (selected) {
        Button(
            onClick = onClick,
            modifier = modifier
        ) {
            Text(text)
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            modifier = modifier
        ) {
            Text(text)
        }
    }
}

@Composable
fun ExpenseCard(
    expense: Expense,
    onPhotoClick: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Text(
                text = expense.description,
                style = MaterialTheme.typography.titleMedium
            )

            Text("Amount: R ${expense.amount}")
            Text("Date: ${expense.date}")
            Text("Time: ${expense.startTime} - ${expense.endTime}")
            Text("Category ID: ${expense.categoryId}")

            val photoPath = expense.photoPath

            if (!photoPath.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "View Photo",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        onPhotoClick(photoPath)
                    }
                )
            }
        }
    }
}

@Composable
fun CustomDateDialog(
    startDate: String,
    endDate: String,
    onStartDateChange: (String) -> Unit,
    onEndDateChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSearch: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Custom Date Range")
        },
        text = {
            Column {
                Text("Use format: yyyy-MM-dd")

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = startDate,
                    onValueChange = onStartDateChange,
                    label = { Text("Start Date") },
                    placeholder = { Text("2026-04-01") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = endDate,
                    onValueChange = onEndDateChange,
                    label = { Text("End Date") },
                    placeholder = { Text("2026-04-30") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = onSearch) {
                Text("Search")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

fun getTodayDate(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(Calendar.getInstance().time)
}

fun getWeekStartDate(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
    return formatter.format(calendar.time)
}

fun getMonthStartDate(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    return formatter.format(calendar.time)
}

fun openExpensePhoto(context: Context, photoPath: String) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(Uri.parse(photoPath), "image/*")
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    context.startActivity(intent)
}