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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.R
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.database.AppDatabase
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.components.SharedBottomNav
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.components.SharedSideMenu
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.components.SharedTopBar

@Composable
fun CategoryScreen(
    navController: NavController,
    userId: Int,
    viewModel: CategoryViewModel = viewModel()
) {
    // Loads the saved categories from the CategoryViewModel.
    val categories by viewModel.categories.collectAsState()

    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)

    // Stores the logged-in user's name for the shared hamburger menu greeting.
    var userName by remember { mutableStateOf("User") }

    // Controls whether the shared side menu is open or closed.
    var showMenu by remember { mutableStateOf(false) }

    // Loads the real user name from RoomDB using the current userId.
    LaunchedEffect(userId) {
        userName = db.userDao().getUserById(userId)?.name ?: "User"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF06121A))
    ) {
        // Background image used behind the full screen.
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
            // Shared fixed top bar.
            // Categories has a back button because it is reached from the dashboard/nav flow.
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

                Spacer(modifier = Modifier.height(10.dp))

                // Opens the category totals screen.
                Button(
                    onClick = {
                        navController.navigate("category_totals/$userId")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color(0xFFA6F22E), Color(0xFF38D6A5))
                                ),
                                RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "VIEW CATEGORY TOTALS",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Displays all saved categories.
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 12.dp)
                ) {
                    items(categories) { category ->
                        CategoryCard(category.name)
                    }
                }

                // Allows the user to add a new category at the bottom of the page.
                AddCategoryBox(viewModel)
            }
        }

        // Shared bottom navbar.
        SharedBottomNav(
            navController = navController,
            userId = userId,
            currentScreen = "categories",
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        // Dark overlay and shared side menu.
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
private fun CategoryCard(categoryName: String) {
    // Gets the emoji icon and background colour based on the category name.
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
            Text(
                text = iconData.icon,
                fontSize = 27.sp
            )
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

// Holds the selected emoji and background colour for a category.
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

        name.contains("bills") || name.contains("electricity") || name.contains("water") ->
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
                    Text(
                        text = "Enter category name",
                        color = Color(0xFF9BAEC5)
                    )
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
                onClick = {
                    viewModel.saveCategory()
                },
                shape = RoundedCornerShape(9.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
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