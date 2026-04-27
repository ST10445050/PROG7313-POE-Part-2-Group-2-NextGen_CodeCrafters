package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.expenses.ExpenseListScreen
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.theme.PROG7313POEPart2Group2NextGen_CodeCraftersTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PROG7313POEPart2Group2NextGen_CodeCraftersTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ExpenseListScreen(userId = 1)
                }
            }
        }
    }
}