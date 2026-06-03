package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.help

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.R
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.remoteModels.HelpFaqDto
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.repository.HelpRepository
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.repository.ProfileRepository
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.components.SharedBottomNav
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.components.SharedSideMenu
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.components.SharedTopBar
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.theme.FinTrackMint

@Composable
fun HelpScreen(
    navController: NavController,
    userId: String
) {
    val profileRepository = remember { ProfileRepository() }
    val helpRepository = remember { HelpRepository() }

    var userName by remember { mutableStateOf("User") }
    var showMenu by remember { mutableStateOf(false) }

    var faqs by remember { mutableStateOf<List<HelpFaqDto>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(userId) {
        try {
            isLoading = true
            errorMessage = ""

            userName = profileRepository.getProfile(userId)?.name ?: "User"
            faqs = helpRepository.getHelpFaqs()

        } catch (e: Exception) {
            errorMessage = e.message ?: "Could not load help information."
        } finally {
            isLoading = false
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
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 18.dp)
                    .padding(top = 20.dp, bottom = 22.dp)
            ) {
                Text(
                    text = "Help & Support",
                    color = Color.White,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Find answers to common questions about using FinTrack.",
                    color = Color(0xFFB7C3D5),
                    fontSize = 15.sp,
                    modifier = Modifier.padding(top = 8.dp, bottom = 20.dp)
                )

                Text(
                    text = "Frequently Asked Questions",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                when {
                    isLoading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = FinTrackMint)
                        }
                    }

                    errorMessage.isNotBlank() -> {
                        Text(
                            text = errorMessage,
                            color = Color.White.copy(alpha = 0.80f),
                            fontSize = 15.sp
                        )
                    }

                    faqs.isEmpty() -> {
                        Text(
                            text = "No help information is available yet.",
                            color = Color.White.copy(alpha = 0.80f),
                            fontSize = 15.sp
                        )
                    }

                    else -> {
                        faqs.forEach { faq ->
                            FAQItem(
                                question = faq.question,
                                answer = faq.answer
                            )
                        }
                    }
                }
            }
        }

        SharedBottomNav(
            navController = navController,
            userId = userId,
            currentScreen = "help",
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
private fun FAQItem(
    question: String,
    answer: String
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .shadow(8.dp, RoundedCornerShape(14.dp))
            .background(Color(0xFF101B2D).copy(alpha = 0.92f), RoundedCornerShape(14.dp))
            .border(1.dp, Color.White.copy(alpha = 0.10f), RoundedCornerShape(14.dp))
            .clickable {
                expanded = !expanded
            }
            .padding(15.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = question,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = null,
                tint = FinTrackMint,
                modifier = Modifier.size(28.dp)
            )
        }

        if (expanded) {
            Spacer(modifier = Modifier.height(10.dp))

            Divider(color = Color.White.copy(alpha = 0.08f))

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = answer,
                color = Color.White.copy(alpha = 0.78f),
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }
    }
}