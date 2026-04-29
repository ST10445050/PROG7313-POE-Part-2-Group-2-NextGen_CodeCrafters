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
import androidx.compose.ui.draw.clip
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
import coil.compose.AsyncImage
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.R
import androidx.compose.ui.platform.LocalContext
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.database.AppDatabase
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

    // I use this state to control whether the hamburger menu is open or closed.
    var showMenu by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)

// I store the logged-in user's name for the hamburger menu.
    var userName by remember { mutableStateOf("User") }

// I load the real user name from RoomDB using the current userId.
    LaunchedEffect(userId) {
        userName = db.userDao().getUserById(userId)?.name ?: "User"
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
                .padding(bottom = 78.dp)
        ) {
            ExpenseTopBar(
                onBackClick = {
                    navController.navigate("dashboard/$userId") {
                        launchSingleTop = true
                    }
                },
                onMenuClick = {
                    // I open the side menu when the user taps the hamburger icon.
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
                                icon = getExpenseIconFromText(expense.description),
                                iconBg = getExpenseIconColorFromText(expense.description),
                                photoPath = expense.photoPath
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
                            // I use the same bright ombré style as the rest of the app buttons.
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color(0xFFA6F22E), Color(0xFF38D6A5))
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

        // I show the menu overlay only when the hamburger menu is open.
        if (showMenu) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.45f))
                    .clickable {
                        // I close the menu when the user taps outside it.
                        showMenu = false
                    }
            )

            SharedSideMenu(
                modifier = Modifier.align(Alignment.TopEnd),
                userName = userName,
                onBudgetGoalsClick = {
                    // I close this for now because Budget Goals will be connected later.
                    showMenu = false
                },
                onLogoutClick = {
                    showMenu = false

                    // I send the user back to the landing page and clear the back stack.
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
private fun ExpenseTopBar(
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
            // I start the menu below the top bar and stop it above the bottom nav.
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
            icon = Icons.Default.TrackChanges,
            title = "Budget Goals",
            iconColor = Color(0xFF65D6D0),
            onClick = onBudgetGoalsClick
        )

        Divider(color = Color.White.copy(alpha = 0.08f))

        SharedMenuItem(
            icon = Icons.Default.PowerSettingsNew,
            title = "Logout",
            iconColor = Color(0xFFE04F5F),
            onClick = onLogoutClick
        )

        Divider(color = Color.White.copy(alpha = 0.08f))
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
private fun ExpenseCard(
    title: String,
    amount: String,
    date: String,
    icon: ImageVector,
    iconBg: Color,
    photoPath: String?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(Color(0xCC101B2D), RoundedCornerShape(16.dp))
            .border(1.dp, Color.White.copy(alpha = 0.10f), RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // I show the category icon first on the left side.
        Box(
            modifier = Modifier
                .size(54.dp)
                .background(iconBg, RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(30.dp)
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        // I show the description next to the icon.
        Text(
            text = title,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f),
            maxLines = 2
        )

        Spacer(modifier = Modifier.width(10.dp))

        // I keep the optional receipt photo separate from the icon.
        if (!photoPath.isNullOrBlank()) {
            AsyncImage(
                model = photoPath,
                contentDescription = "Receipt photo",
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(10.dp))
                    .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(10.dp))
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        // I display the amount on the right, with the date underneath it.
        Column(
            modifier = Modifier.width(92.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = amount,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End,
                maxLines = 1
            )

            Text(
                text = date,
                color = Color.White,
                fontSize = 12.sp,
                textAlign = TextAlign.End,
                maxLines = 1
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

private fun getExpenseIconFromText(description: String): ImageVector {
    val text = description.lowercase(Locale.getDefault())

    return when {
        text.contains("uber") ||
                text.contains("bolt") ||
                text.contains("taxi") ||
                text.contains("transport") -> Icons.Default.DirectionsCar

        text.contains("kfc") ||
                text.contains("mcdonald") ||
                text.contains("steers") ||
                text.contains("burger") ||
                text.contains("king") ||
                text.contains("nandos") ||
                text.contains("pizza") ||
                text.contains("pasta") ||
                text.contains("food") -> Icons.Default.Fastfood

        text.contains("netflix") ||
                text.contains("prime") ||
                text.contains("video") ||
                text.contains("dstv") ||
                text.contains("entertainment") -> Icons.Default.Tv

        text.contains("pick n pay") ||
                text.contains("picknpay") ||
                text.contains("take n pay") ||
                text.contains("spar") ||
                text.contains("checkers") ||
                text.contains("grocery") ||
                text.contains("groceries") -> Icons.Default.ShoppingCart

        text.contains("clothing") ||
                text.contains("clothes") ||
                text.contains("edgars") ||
                text.contains("shirt") -> Icons.Default.Checkroom

        text.contains("petrol") ||
                text.contains("diesel") ||
                text.contains("fuel") ||
                text.contains("shell") ||
                text.contains("bp") ||
                text.contains("engen") ||
                text.contains("total") -> Icons.Default.LocalGasStation

        else -> Icons.Default.CreditCard
    }
}

private fun getExpenseIconColorFromText(description: String): Color {
    val text = description.lowercase(Locale.getDefault())

    return when {
        text.contains("uber") ||
                text.contains("bolt") ||
                text.contains("taxi") ||
                text.contains("transport") -> Color(0xFF185EA8)

        text.contains("kfc") ||
                text.contains("mcdonald") ||
                text.contains("steers") ||
                text.contains("burger") ||
                text.contains("king") ||
                text.contains("nandos") ||
                text.contains("pizza") ||
                text.contains("pasta") ||
                text.contains("food") -> Color(0xFFD83A3A)

        text.contains("netflix") ||
                text.contains("prime") ||
                text.contains("video") ||
                text.contains("dstv") ||
                text.contains("entertainment") -> Color(0xFF6932B8)

        text.contains("pick n pay") ||
                text.contains("picknpay") ||
                text.contains("take n pay") ||
                text.contains("spar") ||
                text.contains("checkers") ||
                text.contains("grocery") ||
                text.contains("groceries") -> Color(0xFF1F8A4C)

        text.contains("clothing") ||
                text.contains("clothes") ||
                text.contains("edgars") ||
                text.contains("shirt") -> Color(0xFFE0A800)

        text.contains("petrol") ||
                text.contains("diesel") ||
                text.contains("fuel") ||
                text.contains("shell") ||
                text.contains("bp") ||
                text.contains("engen") ||
                text.contains("total") -> Color(0xFFE87500)

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