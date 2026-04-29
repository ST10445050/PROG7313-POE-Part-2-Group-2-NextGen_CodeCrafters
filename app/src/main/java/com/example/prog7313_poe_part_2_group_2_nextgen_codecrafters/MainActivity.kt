package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters

import android.graphics.Color
import android.os.Bundle
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.navigation.AppNavGraph
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.theme.FinTrackTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT

        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = false
            isAppearanceLightNavigationBars = false
        }

        setContent {
            FinTrackTheme {
                AppNavGraph()
            }
        }
    }
}