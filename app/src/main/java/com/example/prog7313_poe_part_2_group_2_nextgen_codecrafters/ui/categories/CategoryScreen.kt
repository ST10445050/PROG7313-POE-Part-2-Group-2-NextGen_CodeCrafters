package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.categories

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.database.AppDatabase
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.R

@Composable
fun CategoryScreen(
    navController: NavController,
    userId: Int,
    viewModel: CategoryViewModel = viewModel()
) {
    val categories by viewModel.categories.collectAsState()
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)

// I store the logged-in user's name for the hamburger menu greeting.
    var userName by remember { mutableStateOf("User") }

// I load the real user name from RoomDB using the current userId.
    LaunchedEffect(userId) {
        userName = db.userDao().getUserById(userId)?.name ?: "User"
    }

    // I use this state to open and close the hamburger menu.
    var showMenu by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF06121A))
    ) {
        Image(
            painter = painterResource(id = R.drawable.fintrack_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.45f
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 78.dp)
        ) {
            TopBar(
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
                    .padding(horizontal = 18.dp, vertical = 18.dp)
            ) {
                Text(
                    text = "Categories",
                    color = Color.White,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Manage your expense categories.",
                    color = Color(0xFFB7C3D5),
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 6.dp, bottom = 18.dp)
                )

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 12.dp)
                ) {
                    items(categories) { category ->
                        CategoryCard(category.name)
                    }
                }

                AddCategoryBox(viewModel)
            }
        }

        BottomNavBar(
            navController = navController,
            userId = userId,
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        // I display the same hamburger menu overlay used across the app.
        if (showMenu) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.45f))
                    .clickable {
                        // I close the menu when the user taps outside the menu panel.
                        showMenu = false
                    }
            )

            SharedSideMenu(
                modifier = Modifier.align(Alignment.TopEnd),
                userName = userName,
                onBudgetGoalsClick = {
                    // I close the menu for now because Budget Goals will be added later.
                    showMenu = false
                },
                onLogoutClick = {
                    showMenu = false

                    // I return the user to the landing page and clear the back stack.
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
private fun TopBar(
    onBackClick: () -> Unit,
    onMenuClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .background(Color(0xFF07111F).copy(alpha = 0.88f))
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            tint = Color.White,
            modifier = Modifier
                .size(32.dp)
                .clickable { onBackClick() }
        )

        Spacer(modifier = Modifier.width(14.dp))

        Text("Fin", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Text("Track", color = Color(0xFF65D6D0), fontSize = 26.sp, fontWeight = FontWeight.Bold)

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
            // I start the menu below the top bar so it lines up neatly with the navbar.
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

                // I display the actual logged-in user's name in white.
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
private fun CategoryCard(categoryName: String) {
    val iconData = getCategoryIconData(categoryName)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(78.dp)
            .shadow(8.dp, RoundedCornerShape(14.dp))
            .background(Color(0xFF101B2D).copy(alpha = 0.90f), RoundedCornerShape(14.dp))
            .border(1.dp, Color.White.copy(alpha = 0.10f), RoundedCornerShape(14.dp))
            .padding(horizontal = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(iconData.backgroundColor, RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = iconData.icon, fontSize = 27.sp)
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = categoryName,
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.75f),
            modifier = Modifier.size(32.dp)
        )
    }
}

private data class CategoryIconData(
    val icon: String,
    val backgroundColor: Color
)

private fun getCategoryIconData(categoryName: String): CategoryIconData {
    val name = categoryName.lowercase()

    return when {
        name.contains("grocery") || name.contains("groceries") ->
            CategoryIconData("🛒", Color(0xFF26483B))

        name.contains("food") || name.contains("meal") || name.contains("restaurant") ->
            CategoryIconData("🍗", Color(0xFF5A3A1F))

        name.contains("transport") || name.contains("car") || name.contains("taxi") || name.contains("fuel") ->
            CategoryIconData("🚗", Color(0xFF1F4B5A))

        name.contains("clothing") || name.contains("clothes") || name.contains("shirt") ->
            CategoryIconData("👕", Color(0xFF285A70))

        name.contains("entertainment") || name.contains("game") || name.contains("movie") ->
            CategoryIconData("🎮", Color(0xFF47375F))

        name.contains("bill") || name.contains("electricity") || name.contains("water") ->
            CategoryIconData("🧾", Color(0xFF5A3654))

        name.contains("school") || name.contains("education") || name.contains("book") ->
            CategoryIconData("📚", Color(0xFF514A28))

        name.contains("health") || name.contains("medical") || name.contains("doctor") ->
            CategoryIconData("💊", Color(0xFF4A2F3D))

        else ->
            CategoryIconData("💡", Color(0xFF2E3A46))
    }
}

@Composable
private fun AddCategoryBox(viewModel: CategoryViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(10.dp, RoundedCornerShape(16.dp))
            .background(Color(0xFF101B2D).copy(alpha = 0.92f), RoundedCornerShape(16.dp))
            .border(1.dp, Color(0xFF65D6D0).copy(alpha = 0.35f), RoundedCornerShape(16.dp))
            .padding(15.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = Color(0xFF8EEBFF),
                modifier = Modifier.size(28.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = "Add Category",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .background(Color(0xFF07111F).copy(alpha = 0.85f), RoundedCornerShape(10.dp))
                .border(1.dp, Color(0xFF65D6D0).copy(alpha = 0.50f), RoundedCornerShape(10.dp)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = viewModel.categoryName,
                onValueChange = { viewModel.onCategoryNameChange(it) },
                placeholder = {
                    Text("Enter category name", color = Color(0xFF9BAEC5))
                },
                singleLine = true,
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color(0xFF65D6D0),
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Button(
                onClick = { viewModel.saveCategory() },
                shape = RoundedCornerShape(9.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier
                    .height(42.dp)
                    .padding(end = 7.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFFA6F22E), Color(0xFF38D6A5))
                            ),
                            RoundedCornerShape(9.dp)
                        )
                        .padding(horizontal = 22.dp, vertical = 9.dp)
                ) {
                    Text(
                        text = "SAVE",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun BottomNavBar(
    navController: NavController,
    userId: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(78.dp)
            .background(Color(0xFF07111F).copy(alpha = 0.97f)),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomNavItem(Icons.Default.Home, "Dashboard", selected = false) {
            navController.navigate("dashboard/$userId") {
                launchSingleTop = true
            }
        }

        BottomNavItem(Icons.Default.List, "Expenses", selected = false) {
            navController.navigate("expense_list/$userId") {
                launchSingleTop = true
            }
        }

        BottomNavItem(Icons.Default.Folder, "Categories", selected = true) {
            navController.navigate("categories/$userId") {
                launchSingleTop = true
            }
        }

        BottomNavItem(Icons.Default.Settings, "Settings", selected = false) {
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
    val tintColor = if (selected) Color(0xFF65D6D0) else Color(0xFFB8F6FF)
    val textColor = if (selected) Color(0xFF65D6D0) else Color(0xFFB7C3D5)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = tintColor,
            modifier = Modifier.size(27.dp)
        )

        Text(
            text = label,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
    }
}