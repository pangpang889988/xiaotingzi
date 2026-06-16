package com.fayin.pronunciation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val Light = lightColorScheme(
    primary = ios_light_primary, onPrimary = ios_light_surface,
    background = ios_light_background, onBackground = ios_light_onSurface,
    surface = ios_light_surface, onSurface = ios_light_onSurface,
    surfaceVariant = ios_light_surfaceSecondary, onSurfaceVariant = ios_light_onSurfaceSecondary,
    error = ios_light_destructive, outline = ios_light_separator, outlineVariant = ios_light_separator,
)
private val Dark = darkColorScheme(
    primary = ios_dark_primary, onPrimary = ios_dark_surface,
    background = ios_dark_background, onBackground = ios_dark_onSurface,
    surface = ios_dark_surface, onSurface = ios_dark_onSurface,
    surfaceVariant = ios_dark_surfaceSecondary, onSurfaceVariant = ios_dark_onSurfaceSecondary,
    error = ios_dark_destructive, outline = ios_dark_separator, outlineVariant = ios_dark_separator,
)

@Composable
fun FayinTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = if (darkTheme) Dark else Light, typography = Typography, content = content)
}
