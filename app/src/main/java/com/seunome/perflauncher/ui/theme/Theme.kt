package com.seunome.perflauncher.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val GreenPrimary = Color(0xFF00C853)
private val GreenDark = Color(0xFF009624)
private val BackgroundDark = Color(0xFF121212)
private val SurfaceDark = Color(0xFF1E1E1E)
private val OnSurfaceDark = Color(0xFFE0E0E0)

private val DarkColors = darkColorScheme(
    primary = GreenPrimary,
    secondary = GreenDark,
    background = BackgroundDark,
    surface = SurfaceDark,
    onBackground = OnSurfaceDark,
    onSurface = OnSurfaceDark
)

@Composable
fun PerformanceTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColors,
        content = content
    )
}
