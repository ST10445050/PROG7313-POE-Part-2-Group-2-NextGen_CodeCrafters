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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.R
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.theme.*

@Composable
fun Question4Screen(
    navController: NavController,
    userId: Int,
    employmentStatus: String,
    monthlyIncome: Double,
    categories: String
) {
    val context = LocalContext.current
    var selectedGoal by remember { mutableStateOf("") }

    val options = listOf(
        "Save more money",
        "Reduce spending",
        "Pay off debt",
        "Track my finances better"
    )

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
                .padding(horizontal = 28.dp)
                .padding(top = 90.dp, bottom = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.fintrack_logo),
                contentDescription = null,
                modifier = Modifier.size(300.dp),
                contentScale = ContentScale.Fit
            )

            Text(
                text = "Question 4",
                color = FinTrackLime,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Main Financial Goal",
                color = FinTrackMint,
                fontSize = 21.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(34.dp))

            Text(
                text = "What is your main\nfinancial goal right now?",
                color = Color.White,
                fontSize = 29.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                lineHeight = 38.sp
            )

            Spacer(modifier = Modifier.height(26.dp))

            options.forEach { option ->
                Question4OptionRow(
                    text = option,
                    selected = selectedGoal == option,
                    onClick = { selectedGoal = option }
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (selectedGoal.isBlank()) {
                        Toast.makeText(context, "Please select a goal", Toast.LENGTH_SHORT).show()
                    } else {
                        val safeEmploymentStatus = Uri.encode(employmentStatus)
                        val safeCategories = Uri.encode(categories)
                        val safeGoal = Uri.encode(selectedGoal)

                        navController.navigate(
                            "question5/$userId/$safeEmploymentStatus/$monthlyIncome/$safeCategories/$safeGoal"
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

            Spacer(modifier = Modifier.height(36.dp))
        }
    }
}

@Composable
private fun Question4OptionRow(
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
                .padding(vertical = 12.dp),
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

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = text,
                color = Color.White,
                fontSize = 21.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Divider(
            color = Color.White.copy(alpha = 0.22f),
            thickness = 1.dp
        )
    }
}