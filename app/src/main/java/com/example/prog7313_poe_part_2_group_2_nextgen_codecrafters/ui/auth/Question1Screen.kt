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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.R
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.theme.*

@Composable
fun Question1Screen(navController: NavController, userId: Int) {

    val context = LocalContext.current
    var selectedOption by remember { mutableStateOf("") }

    val options = listOf(
        "Full-time employed",
        "Part-time employed",
        "Self-employed / Freelancer",
        "Student",
        "Currently unemployed",
        "Prefer not to say"
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp)
                .padding(top = 90.dp, bottom = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.fintrack_logo),
                contentDescription = null,
                modifier = Modifier.size(280.dp),
                contentScale = ContentScale.Fit
            )

            Text(
                text = "Question 1",
                color = FinTrackLime,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Employment Status",
                color = FinTrackMint,
                fontSize = 19.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "What best describes your\ncurrent employment?",
                color = Color.White,
                fontSize = 27.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                lineHeight = 34.sp
            )

            Spacer(modifier = Modifier.height(22.dp))

            options.forEach { option ->
                QuestionOptionRow(
                    text = option,
                    selected = selectedOption == option,
                    onClick = { selectedOption = option }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (selectedOption.isBlank()) {
                        Toast.makeText(context, "Please select an option", Toast.LENGTH_SHORT).show()
                    } else {
                        val safeEmploymentStatus = Uri.encode(selectedOption)
                        navController.navigate("question2/$userId/$safeEmploymentStatus")
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

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = {
                    Toast.makeText(context, "Skipping questionnaire", Toast.LENGTH_SHORT).show()
                    navController.navigate("dashboard/$userId")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Skip Questionnaire",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun QuestionOptionRow(
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
                .padding(vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = selected,
                onCheckedChange = { onClick() },
                colors = CheckboxDefaults.colors(
                    checkedColor = FinTrackMint,
                    uncheckedColor = Color(0xFF7A9AAD),
                    checkmarkColor = FinTrackNavy
                )
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = text,
                color = Color.White,
                fontSize = 19.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Divider(
            color = Color.White.copy(alpha = 0.22f),
            thickness = 1.dp
        )
    }
}