package uk.co.btsda.syllabus.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val Indigo = Color(0xFF5B4BE7)
private val Magenta = Color(0xFFD81B7A)
private val Cyan = Color(0xFF12B5C9)
private val Amber = Color(0xFFFFB300)

private val LightColors = lightColorScheme(
    primary = Indigo,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE3E0FF),
    onPrimaryContainer = Color(0xFF160066),
    secondary = Magenta,
    onSecondary = Color.White,
    tertiary = Cyan,
    onTertiary = Color.White,
    background = Color(0xFFFBF8FF),
    surface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFFEDE9F7),
    onSurface = Color(0xFF1A1B22),
    onSurfaceVariant = Color(0xFF49454F),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFBCB4FF),
    onPrimary = Color(0xFF23107A),
    primaryContainer = Color(0xFF3B2CC0),
    onPrimaryContainer = Color(0xFFE3E0FF),
    secondary = Color(0xFFFF8AC4),
    onSecondary = Color(0xFF5A0033),
    tertiary = Color(0xFF6FE0F0),
    onTertiary = Color(0xFF00363D),
    background = Color(0xFF121018),
    surface = Color(0xFF1A1822),
    surfaceVariant = Color(0xFF2A2733),
    onSurface = Color(0xFFEDE9F7),
    onSurfaceVariant = Color(0xFFCAC4D0),
)

@Suppress("UNUSED_PARAMETER")
@Composable
fun TSDTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
