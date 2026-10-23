package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val MobaDarkColorScheme = darkColorScheme(
    primary = MobaGold,
    onPrimary = Color.Black,
    primaryContainer = MobaGoldDark,
    onPrimaryContainer = Color.White,
    secondary = MobaBlueTeam,
    onSecondary = Color.Black,
    secondaryContainer = MobaSurfaceVariant,
    onSecondaryContainer = Color.White,
    tertiary = MobaRedTeam,
    onTertiary = Color.White,
    background = MobaDarkBackground,
    onBackground = TextPrimary,
    surface = MobaSurface,
    onSurface = TextPrimary,
    surfaceVariant = MobaSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = MobaCardBorder,
    error = MobaRedTeam,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // MOBA esports always looks best in dark gaming style
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = MobaDarkColorScheme,
        typography = Typography,
        content = content
    )
}
