package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.auth

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.R
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.theme.*

@Composable
fun Question3Screen(
    navController: NavController,
    userId: Int,
    employmentStatus: String,
    monthlyIncome: Double
) {
    val context = LocalContext.current

    val options = listOf(
        "Groceries",
        "Rent / Housing",
        "Transport",
        "Eating out",
        "Shopping",
        "Subscriptions",
        "Other"
    )

    var selectedCategories by remember { mutableStateOf(listOf<String>()) }

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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp)
                .padding(top = 90.dp, bottom = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.fintrack_logo),
                contentDescription = null,
                modifier = Modifier.size(290.dp),
                contentScale = ContentScale.Fit
            )

            Text(
                text = "Question 3",
                color = FinTrackLime,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Biggest Spending Categories",
                color = FinTrackMint,
                fontSize = 19.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "What do you spend most of\nyour money on?",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                lineHeight = 36.sp
            )

            Text(
                text = "(Select up to 3)",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(22.dp))

            options.forEach { option ->
                Question3OptionRow(
                    text = option,
                    selected = selectedCategories.contains(option),
                    onClick = {
                        selectedCategories =
                            if (selectedCategories.contains(option)) {
                                selectedCategories - option
                            } else {
                                if (selectedCategories.size < 3) {
                                    selectedCategories + option
                                } else {
                                    Toast.makeText(
                                        context,
                                        "You can select up to 3 categories",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    selectedCategories
                                }
                            }
                    }
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = {
                    if (selectedCategories.isEmpty()) {
                        Toast.makeText(
                            context,
                            "Please select at least one category",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val safeEmploymentStatus = Uri.encode(employmentStatus)
                        val safeCategories = Uri.encode(selectedCategories.joinToString(","))

                        navController.navigate(
                            "question4/$userId/$safeEmploymentStatus/$monthlyIncome/$safeCategories"
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
                    .shadow(20.dp, RoundedCornerShape(30.dp)),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = FinTrackLime,
                    contentColor = FinTrackNavy
                )
            ) {
                Text(
                    text = "NEXT",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(34.dp))
        }
    }
}

@Composable
private fun Question3OptionRow(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = selected,
                onCheckedChange = { onClick() },
                colors = CheckboxDefaults.colors(
                    checkedColor = FinTrackLime,
                    uncheckedColor = FinTrackLime,
                    checkmarkColor = FinTrackNavy
                )
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = text,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Divider(
            color = Color.White.copy(alpha = 0.22f),
            thickness = 1.dp
        )
    }
}