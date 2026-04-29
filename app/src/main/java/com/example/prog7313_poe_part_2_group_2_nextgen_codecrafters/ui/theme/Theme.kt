package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = FinTrackLime,
    onPrimary = FinTrackNavy,

    secondary = FinTrackMint,
    onSecondary = FinTrackNavy,

    tertiary = FinTrackTeal,
    onTertiary = FinTrackWhite,

    background = FinTrackNavy,
    onBackground = FinTrackWhite,

    surface = FinTrackNavy,
    onSurface = FinTrackWhite,

    surfaceVariant = FinTrackNavy,
    onSurfaceVariant = FinTrackBorder
)

private val LightColorScheme = lightColorScheme(
    primary = FinTrackTeal,
    onPrimary = FinTrackWhite,

    secondary = FinTrackMint,
    onSecondary = FinTrackNavy,

    tertiary = FinTrackLime,
    onTertiary = FinTrackNavy,

    background = FinTrackWhite,
    onBackground = FinTrackNavy,

    surface = FinTrackWhite,
    onSurface = FinTrackNavy,

    surfaceVariant = FinTrackWhite,
    onSurfaceVariant = FinTrackBorder
)

@Composable
fun FinTrackTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}