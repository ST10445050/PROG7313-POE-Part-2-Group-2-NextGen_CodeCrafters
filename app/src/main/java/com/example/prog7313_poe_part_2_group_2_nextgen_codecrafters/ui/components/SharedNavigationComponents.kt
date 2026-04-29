package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.theme.FinTrackMint

// ---------------- TOP BAR ----------------

@Composable
fun SharedTopBar(
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier,
    showBackButton: Boolean = false,
    onBackClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp)
            .background(Color(0xEE071827))
            .padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Optional back button
        if (showBackButton) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier
                    .size(34.dp)
                    .clickable { onBackClick?.invoke() }
            )

            Spacer(modifier = Modifier.width(14.dp))
        }

        // App title
        Text("Fin", color = Color.White, fontSize = 27.sp, fontWeight = FontWeight.Bold)
        Text("Track", color = FinTrackMint, fontSize = 27.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.weight(1f))

        // Hamburger menu
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

// ---------------- BOTTOM NAV ----------------

@Composable
fun SharedBottomNav(
    navController: NavController,
    userId: Int,
    currentScreen: String,
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

        SharedBottomNavItem(Icons.Default.Home, "Dashboard", currentScreen == "dashboard") {
            navController.navigate("dashboard/$userId") { launchSingleTop = true }
        }

        SharedBottomNavItem(Icons.Outlined.CreditCard, "Expenses", currentScreen == "expenses") {
            navController.navigate("expense_list/$userId") { launchSingleTop = true }
        }

        SharedBottomNavItem(Icons.Outlined.Folder, "Categories", currentScreen == "categories") {
            navController.navigate("categories/$userId") { launchSingleTop = true }
        }

        // Optional future screen
        SharedBottomNavItem(Icons.Default.Settings, "Settings", currentScreen == "settings") {
            navController.navigate("settings/$userId") { launchSingleTop = true }
        }
    }
}

@Composable
private fun SharedBottomNavItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val inactiveColor = Color.White.copy(alpha = 0.65f)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (selected) FinTrackMint else inactiveColor,
            modifier = Modifier.size(27.dp)
        )

        Text(
            text = label,
            color = if (selected) FinTrackMint else inactiveColor,
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

// ---------------- SIDE MENU ----------------

@Composable
fun SharedSideMenu(
    modifier: Modifier = Modifier,
    userName: String,
    onBudgetGoalsClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Column(
        modifier = modifier
            .padding(top = 72.dp, bottom = 78.dp)
            .width(278.dp)
            .fillMaxHeight()
            .background(Color(0xF0111C2D))
            .border(1.dp, Color.White.copy(alpha = 0.10f))
    ) {

        // Header section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xDD071827))
                .padding(horizontal = 24.dp, vertical = 22.dp)
        ) {
            Row {
                Text(
                    text = "Hello ",
                    color = FinTrackMint,
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

        // Budget Goals navigation item
        SharedMenuItem(
            icon = Icons.Default.TrackChanges,
            title = "Budget Goals",
            iconColor = FinTrackMint,
            onClick = onBudgetGoalsClick
        )

        Divider(color = Color.White.copy(alpha = 0.08f))

        // Logout item
        SharedMenuItem(
            icon = Icons.Default.PowerSettingsNew,
            title = "Logout",
            iconColor = Color(0xFFE04F5F),
            onClick = onLogoutClick
        )

        Divider(color = Color.White.copy(alpha = 0.08f))
    }
}

// ---------------- MENU ITEM ----------------

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