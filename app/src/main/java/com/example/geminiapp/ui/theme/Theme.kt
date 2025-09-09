package com.example.geminiapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.runtime.Composable

private val LightColors = lightColors()
private val DarkColors = darkColors()

@Composable
fun GeminiAppTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(colors = colors, typography = Typography(), shapes = Shapes(), content = content)
}
