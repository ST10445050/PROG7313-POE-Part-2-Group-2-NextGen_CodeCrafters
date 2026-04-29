package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.expense

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.R
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ExpenseListScreen(
    userId: Int,
    viewModel: ExpenseViewModel,
    navController: NavController
) {
    val expenseList by viewModel.getExpensesForUser(userId)
        .collectAsState(initial = emptyList())

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
            ExpenseTopBar(
                onBackClick = {
                    navController.navigate("dashboard/$userId") {
                        launchSingleTop = true
                    }
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 18.dp)
                    .padding(top = 22.dp)
            ) {
                Text(
                    text = "Expenses",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Track and manage your spending.",
                    color = Color(0xFF65D6D0),
                    fontSize = 17.sp,
                    modifier = Modifier.padding(top = 6.dp, bottom = 20.dp)
                )

                FilterCard()

                Spacer(modifier = Modifier.height(16.dp))

                if (expenseList.isEmpty()) {
                    EmptyExpenseCard()
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 18.dp)
                    ) {
                        items(expenseList) { expense ->
                            val formattedDate = remember(expense.date) {
                                try {
                                    val inputFormat = SimpleDateFormat("d/M/yyyy", Locale.getDefault())
                                    val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                                    val parsedDate = inputFormat.parse(expense.date)
                                    parsedDate?.let { outputFormat.format(it) } ?: expense.date
                                } catch (e: Exception) {
                                    expense.date
                                }
                            }

                            ExpenseCard(
                                title = expense.description.ifBlank { "Expense" },
                                amount = "R ${String.format("%.2f", expense.amount)}",
                                date = formattedDate,
                                icon = getExpenseIcon(expense.categoryId),
                                iconBg = getExpenseIconColor(expense.categoryId)
                            )
                        }
                    }
                }

                Button(
                    onClick = { navController.navigate("add_expense/$userId") },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 32.dp)
                        .height(58.dp)
                        .shadow(14.dp, RoundedCornerShape(28.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(0.dp),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color(0xFFB6F529), Color(0xFF39D58A))
                                ),
                                RoundedCornerShape(28.dp)
                            )
                            .padding(horizontal = 28.dp, vertical = 13.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "Add Expense",
                            color = Color.White,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        ExpenseBottomNav(
            navController = navController,
            userId = userId,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun ExpenseTopBar(onBackClick: () -> Unit) {
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
            contentDescription = "Menu",
            tint = Color.White,
            modifier = Modifier.size(34.dp)
        )
    }
}

@Composable
private fun FilterCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xCC101B2D), RoundedCornerShape(16.dp))
            .border(1.dp, Color.White.copy(alpha = 0.10f), RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = null,
                tint = Color(0xFF65D6D0),
                modifier = Modifier.size(26.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = "Filter by Date",
                color = Color.White,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChipText("Today", false, Modifier.weight(1f))
            FilterChipText("This Week", false, Modifier.weight(1f))
            FilterChipText("This Month", true, Modifier.weight(1f))
        }
    }
}

@Composable
private fun FilterChipText(text: String, selected: Boolean, modifier: Modifier) {
    Box(
        modifier = modifier
            .height(42.dp)
            .background(
                if (selected) Color(0xFF39D58A).copy(alpha = 0.85f)
                else Color(0xFF17263A).copy(alpha = 0.95f),
                RoundedCornerShape(12.dp)
            )
            .border(
                1.dp,
                if (selected) Color(0xFF65D6D0) else Color.White.copy(alpha = 0.08f),
                RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
private fun ExpenseCard(
    title: String,
    amount: String,
    date: String,
    icon: ImageVector,
    iconBg: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(84.dp)
            .background(Color(0xCC101B2D), RoundedCornerShape(16.dp))
            .border(1.dp, Color.White.copy(alpha = 0.10f), RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(iconBg, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = title,
            color = Color.White,
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = amount,
                color = Color.White,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End
            )

            Text(
                text = date,
                color = Color.White.copy(alpha = 0.70f),
                fontSize = 15.sp
            )
        }
    }
}

@Composable
private fun EmptyExpenseCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .background(Color(0xCC101B2D), RoundedCornerShape(16.dp))
            .border(1.dp, Color.White.copy(alpha = 0.10f), RoundedCornerShape(16.dp))
            .padding(18.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No expenses yet. Add your first expense to start tracking.",
            color = Color.White.copy(alpha = 0.75f),
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}

private fun getExpenseIcon(categoryId: Int): ImageVector {
    return when (categoryId) {
        1 -> Icons.Default.ShoppingCart
        2 -> Icons.Default.DirectionsCar
        3 -> Icons.Default.Movie
        4 -> Icons.Default.Receipt
        else -> Icons.Default.CreditCard
    }
}

private fun getExpenseIconColor(categoryId: Int): Color {
    return when (categoryId) {
        1 -> Color(0xFF1F4B5A)
        2 -> Color(0xFF185EA8)
        3 -> Color(0xFF5A3654)
        4 -> Color(0xFF26483B)
        else -> Color(0xFF2E3A46)
    }
}

@Composable
private fun ExpenseBottomNav(
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
            navController.navigate("dashboard/$userId") {
                launchSingleTop = true
            }
        }

        BottomNavItem(Icons.Outlined.CreditCard, "Expenses", true) {
            navController.navigate("expense_list/$userId") {
                launchSingleTop = true
            }
        }

        BottomNavItem(Icons.Outlined.Folder, "Categories", false) {
            navController.navigate("categories/$userId") {
                launchSingleTop = true
            }
        }

        BottomNavItem(Icons.Default.Settings, "Settings", false) {
            navController.navigate("settings/$userId") {
                launchSingleTop = true
            }
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