package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.auth

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
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
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.database.AppDatabase
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.entities.QuestionnaireAnswers
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun Question5Screen(
    navController: NavController,
    userId: Int,
    employmentStatus: String,
    monthlyIncome: Double,
    categories: String,
    financialGoal: String
) {
    val context = LocalContext.current
    val questionnaireDao = AppDatabase.getDatabase(context).questionnaireDao()
    val scope = rememberCoroutineScope()

    var savingsGoal by remember { mutableStateOf(5000f) }

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
                text = "Question 5",
                color = FinTrackLime,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Monthly Savings Goal",
                color = FinTrackMint,
                fontSize = 21.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(42.dp))

            Text(
                text = "How much would you like\nto save each month?",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                lineHeight = 36.sp
            )

            Spacer(modifier = Modifier.height(36.dp))

            Text(
                text = "R${savingsGoal.toInt()}",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .shadow(14.dp, RoundedCornerShape(10.dp))
                    .padding(bottom = 6.dp)
            )

            Slider(
                value = savingsGoal,
                onValueChange = { savingsGoal = it },
                valueRange = 0f..10000f,
                steps = 9,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                colors = SliderDefaults.colors(
                    thumbColor = FinTrackLime,
                    activeTrackColor = FinTrackLime,
                    inactiveTrackColor = FinTrackMint.copy(alpha = 0.55f),
                    activeTickColor = FinTrackLime,
                    inactiveTickColor = FinTrackMint.copy(alpha = 0.35f)
                )
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "R0",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = "R10 000+",
                    color = FinTrackLime,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    scope.launch {
                        val answers = QuestionnaireAnswers(
                            userId = userId,
                            employmentStatus = employmentStatus,
                            monthlyIncome = monthlyIncome,
                            spendingCategories = categories,
                            financialGoal = financialGoal,
                            monthlySavingsGoal = savingsGoal.toDouble(),
                            dashboardType = "personalized"
                        )

                        questionnaireDao.deleteAnswersByUserId(userId)
                        questionnaireDao.insertAnswers(answers)

                        Toast.makeText(
                            context,
                            "Questionnaire completed successfully",
                            Toast.LENGTH_SHORT
                        ).show()

                        Log.d(
                            "Question5Screen",
                            "Saved questionnaire answers for user ID: $userId"
                        )

                        navController.navigate("dashboard/$userId")
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