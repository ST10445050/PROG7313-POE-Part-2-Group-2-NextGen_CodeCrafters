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
/*
-------------------------------------------------------------------------------------------
MODULE: Programming 3C (PROG7313)
ASSESSMENT: POE Part 3 – FinTrack Mobile Application

DEVELOPERS:
- Keona Mackan (ST10445050)
- Teah Andrew (ST10440926)
- Ethan Govender (ST10250993)
- Kiara Israel (ST10277747)

DESCRIPTION:
This application is a mobile budgeting system developed using
Android Studio with Jetpack Compose and Supabase for cloud data
storage. It allows users to manage expenses, categories, and
budget goals, while providing insights into monthly spending
through analytics, progress tracking, and graph visualisations.

---------------------------------------------------------------------------------------------
REFERENCES:

Android Developers (2019) *Save data in a local database using Room*.
Available at: https://developer.android.com/training/data-storage/room
(Accessed: 17 April 2026).

Codex Creator (2022) *Add Images in Android Studio with Jetpack Compose | PostCard App for Beginners*.
Available at: https://www.youtube.com/watch?v=drwXb5RqRuo
(Accessed: 22 April 2026).

Coding with Dev (2023) *Android date picker dialog example | DatePickerDialog - Android Studio Tutorial | Kotlin*.
Available at: https://www.youtube.com/watch?v=DpL8DhCNKdE
(Accessed: 25 April 2026).

Easy Tuto (2024) *Room Database in Android | Android Tutorial | 2024*.
Available at: https://www.youtube.com/watch?v=sWOmlDvz_3U
(Accessed: 17 April 2026).

Supabase (2026) *Use Supabase with Android Kotlin*.
Available at: https://supabase.com/docs/guides/getting-started/quickstarts/kotlin
(Accessed: 29 May 2026).

YouTube (2026a) *Android Studio / Kotlin tutorial video*.
Available at: https://www.youtube.com/watch?v=5XOabSpW_Os
(Accessed: 31 May 2026).

YouTube (2026b) *Android Studio / Kotlin tutorial video*.
Available at: https://www.youtube.com/watch?v=MY290aW8hMQ
(Accessed: 1 June 2026).

YouTube (2026c) *Android Studio / Kotlin tutorial video*.
Available at: https://www.youtube.com/watch?v=SaTx-gLLxWQ
(Accessed: 1 June 2026).

YouTube (2026d) *Android Studio / Kotlin tutorial video*.
Available at: https://www.youtube.com/watch?v=_iXUVJ6HTHU
(Accessed: 30 May 2026).

---------------------------------------------------------------------------------------------
*/