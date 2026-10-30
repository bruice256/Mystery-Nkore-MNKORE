package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val GoldPeaceLightColorScheme = lightColorScheme(
    primary = GoldPrimary,
    onPrimary = TextOnGold,
    primaryContainer = GoldContainer,
    onPrimaryContainer = OnGoldContainer,
    secondary = PeaceOlive,
    onSecondary = OnPeaceOlive,
    secondaryContainer = PeaceOliveLight,
    onSecondaryContainer = TextPrimary,
    tertiary = SacredCrimson,
    onTertiary = TextOnGold,
    background = OffWhiteBg,
    onBackground = TextPrimary,
    surface = OffWhiteSurface,
    onSurface = TextPrimary,
    surfaceVariant = OffWhiteSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = OffWhiteBorder,
    outlineVariant = OffWhiteBorder
)

private val GoldPeaceDarkColorScheme = darkColorScheme(
    primary = GoldLight,
    onPrimary = DarkNightBg,
    primaryContainer = GoldDark,
    onPrimaryContainer = GoldContainer,
    secondary = PeaceOliveLight,
    onSecondary = DarkNightBg,
    secondaryContainer = DarkNightSurfaceVariant,
    onSecondaryContainer = DarkTextPrimary,
    tertiary = SacredAmber,
    onTertiary = DarkNightBg,
    background = DarkNightBg,
    onBackground = DarkTextPrimary,
    surface = DarkNightSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkNightSurfaceVariant,
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkNightBorder,
    outlineVariant = DarkNightBorder
)

@Composable
fun ShalomMinistryTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) GoldPeaceDarkColorScheme else GoldPeaceLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
