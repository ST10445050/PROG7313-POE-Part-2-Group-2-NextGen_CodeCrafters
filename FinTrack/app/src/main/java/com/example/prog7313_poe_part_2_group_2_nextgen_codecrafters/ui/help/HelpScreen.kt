package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.help

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HelpScreen() {

    val faqs = listOf(
        "What is FinTrack?" to
                "FinTrack is a budgeting and expense tracking application that helps users manage finances and monitor spending habits.",

        "How do I add a new expense?" to
                "Navigate to the Expenses section and click Add Expense. Enter the amount, category, date and description before saving.",

        "Can I upload a receipt for my expenses?" to
                "Yes. When adding an expense, select Upload Receipt and choose an image file from your device.",

        "How do I set my monthly budget?" to
                "Go to the Budget section and enter your desired monthly spending limit.",

        "How are achievements and badges earned?" to
                "Achievements are awarded when users meet financial goals and consistently track expenses.",

        "Can I edit my account details?" to
                "Yes. Open Settings and select Edit Profile to update your information.",

        "What should I do if I forget my password?" to
                "Use the Forgot Password option on the login screen and follow the instructions sent to your email."
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Text(
            text = "Help & Support",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Find answers to common questions about FinTrack."
        )

        Spacer(modifier = Modifier.height(20.dp))

        faqs.forEach { faq ->
            FAQItem(
                question = faq.first,
                answer = faq.second
            )
        }
    }
}

@Composable
fun FAQItem(
    question: String,
    answer: String
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {

        Column(
            modifier = Modifier.padding(12.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        expanded = !expanded
                    },
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = question,
                    style = MaterialTheme.typography.titleMedium
                )

                Icon(
                    imageVector =
                        if (expanded)
                            Icons.Default.ExpandLess
                        else
                            Icons.Default.ExpandMore,
                    contentDescription = null
                )
            }

            if (expanded) {

                Spacer(modifier = Modifier.height(8.dp))

                Text(answer)
            }
        }
    }
}