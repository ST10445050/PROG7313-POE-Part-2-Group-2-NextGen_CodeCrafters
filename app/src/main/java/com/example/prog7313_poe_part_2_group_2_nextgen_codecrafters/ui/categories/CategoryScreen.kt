package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.categories

import android.widget.Toast
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
import androidx.compose.material.icons.filled.Delete
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
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.remoteModels.CategoryDto
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.repository.ProfileRepository
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.components.SharedBottomNav
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.components.SharedSideMenu
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.components.SharedTopBar
import kotlinx.coroutines.launch

@Composable
fun CategoryScreen(
    navController: NavController,
    userId: String,
    viewModel: CategoryViewModel = viewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val profileRepository = remember { ProfileRepository() }

    var userName by remember { mutableStateOf("User") }
    var showMenu by remember { mutableStateOf(false) }

    LaunchedEffect(userId) {
        viewModel.loadCategories(userId)
        userName = profileRepository.getProfile(userId)?.name ?: "User"
    }

    LaunchedEffect(viewModel.errorMessage) {
        viewModel.errorMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            viewModel.clearError()
        }
    }

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

                if (viewModel.isLoading && viewModel.categories.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color(0xFF38D6A5))
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 12.dp)
                    ) {
                        items(viewModel.categories) { category ->
                            CategoryCard(
                                category = category,
                                onDeleteClick = {
                                    val categoryId = category.categoryId
                                    if (categoryId != null) {
                                        viewModel.deleteCategory(userId, categoryId)
                                    }
                                }
                            )
                        }
                    }
                }

                AddCategoryBox(
                    viewModel = viewModel,
                    userId = userId
                )
            }
        }

        SharedBottomNav(
            navController = navController,
            userId = userId,
            currentScreen = "categories",
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
private fun CategoryCard(
    category: CategoryDto,
    onDeleteClick: () -> Unit
) {
    val iconData = getCategoryIconData(category.name)

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
            text = category.name,
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )

        IconButton(onClick = onDeleteClick) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete category",
                tint = Color(0xFFE04F5F),
                modifier = Modifier.size(26.dp)
            )
        }

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
private fun AddCategoryBox(
    viewModel: CategoryViewModel,
    userId: String
) {
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
                    viewModel.saveCategory(userId)
                },
                enabled = !viewModel.isLoading,
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
                        text = if (viewModel.isLoading) "..." else "SAVE",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}