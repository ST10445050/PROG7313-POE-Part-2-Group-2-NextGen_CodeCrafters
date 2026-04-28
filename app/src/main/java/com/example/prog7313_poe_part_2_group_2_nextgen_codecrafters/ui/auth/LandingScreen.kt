package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.auth

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.R
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.theme.*

@Composable
fun LandingScreen(navController: NavController) {

    Box(modifier = Modifier.fillMaxSize()) {

        // BACKGROUND
        Image(
            painter = painterResource(id = R.drawable.fintrack_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            //  BIGGER LOGO
            Image(
                painter = painterResource(id = R.drawable.fintrack_logo),
                contentDescription = null,
                modifier = Modifier.size(240.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "FinTrack",
                color = FinTrackWhite,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Budget Right, Live Bright",
                color = FinTrackLime,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            //  GLOW LINE
            Box(
                modifier = Modifier
                    .padding(top = 6.dp)
                    .width(160.dp)
                    .height(3.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            listOf(FinTrackTeal, FinTrackLime, FinTrackTeal)
                        ),
                        shape = RoundedCornerShape(50)
                    )
                    .shadow(12.dp, RoundedCornerShape(50))
            )

            Spacer(modifier = Modifier.height(26.dp))

            //  CENTERED TEXT
            Text(
                text = "Take control of your money before\nit takes control of you.",
                color = FinTrackWhite,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(34.dp))

            //  REAL GLOW BUTTON (SOLID LIME + GLOW)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
                    .shadow(
                        elevation = 25.dp,
                        shape = RoundedCornerShape(30.dp),
                        ambientColor = FinTrackLime,
                        spotColor = FinTrackLime
                    )
            ) {
                Button(
                    onClick = {
                        Log.d("LandingScreen", "Get Started clicked")
                        navController.navigate("register")
                    },
                    modifier = Modifier.fillMaxSize(),
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = FinTrackLime,
                        contentColor = FinTrackNavy
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 10.dp,
                        pressedElevation = 6.dp
                    )
                ) {
                    Text(
                        text = "GET STARTED",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // OUTLINED BUTTON
            OutlinedButton(
                onClick = {
                    Log.d("LandingScreen", "Already have account clicked")
                    navController.navigate("login")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(28.dp),
                border = BorderStroke(1.dp, FinTrackTeal),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = FinTrackMint
                )
            ) {
                Text(
                    "I ALREADY HAVE A FINTRACK ACCOUNT",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}