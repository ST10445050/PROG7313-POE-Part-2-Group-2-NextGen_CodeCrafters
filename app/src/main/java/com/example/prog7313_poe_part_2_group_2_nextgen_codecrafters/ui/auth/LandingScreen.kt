package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.auth

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun LandingScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "FinTrack")
        Text(text = "Budget Right, Live Bright")

        Button(
            onClick = {
                Log.d("LandingScreen", "Get Started clicked")
                navController.navigate("register")
            },
            modifier = Modifier.padding(top = 24.dp)
        ) {
            Text("GET STARTED")
        }

        TextButton(
            onClick = {
                Log.d("LandingScreen", "Already have account clicked")
                navController.navigate("login")
            }
        ) {
            Text("I ALREADY HAVE A FINTRACK ACCOUNT")
        }
    }
}