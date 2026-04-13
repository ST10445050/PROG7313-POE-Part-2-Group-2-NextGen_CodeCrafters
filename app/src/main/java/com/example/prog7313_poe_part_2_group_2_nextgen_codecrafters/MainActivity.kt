package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.navigation.AppNavGraph
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.theme.PROG7313POEPart2Group2NextGen_CodeCraftersTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PROG7313POEPart2Group2NextGen_CodeCraftersTheme {
                AppNavGraph()
            }
        }
    }
}