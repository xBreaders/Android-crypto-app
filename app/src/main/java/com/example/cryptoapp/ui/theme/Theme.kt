package com.example.cryptoapp.ui.theme

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

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF202124),       // Dark gray
    onPrimary = Color(0xFFFFFFFF),     // White for text/icons on primary color
    secondary = Color(0xFFBB86FC),     // Vibrant purple
    onSecondary = Color(0xFFFFFFFF),   // White for text/icons on secondary color
    tertiary = Color(0xFF3700B3),      // Deeper shade of purple
    onTertiary = Color(0xFFFFFFFF),    // White for text/icons on tertiary color
    background = Color(0xFF202124),    // Dark gray for background
    onBackground = Color(0xDEFFFFFF),  // White with transparency for text/icons on background
    surface = Color(0xFF37373D),       // Intermediate shade for surface
    onSurface = Color(0xDEFFFFFF),     // White with transparency for text/icons on surface
    error = Color(0xFFCF6679),         // Material design error color
    onError = Color(0xFFFFFFFF)        // White for text/icons on error color
    // Define other colors like surface variant, error container, etc., if needed.
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF202124),       // Dark gray
    onPrimary = Color(0xFFFFFFFF),     // White for text/icons on primary color
    secondary = Color(0xFFBB86FC),     // Vibrant purple
    onSecondary = Color(0xFFFFFFFF),   // White for text/icons on secondary color
    tertiary = Color(0xFF3700B3),      // Deeper shade of purple
    onTertiary = Color(0xFFFFFFFF),    // White for text/icons on tertiary color
    background = Color(0xFF202124),    // Dark gray for background
    onBackground = Color(0xDEFFFFFF),  // White with transparency for text/icons on background
    surface = Color(0xFF37373D),       // Intermediate shade for surface
    onSurface = Color(0xDEFFFFFF),     // White with transparency for text/icons on surface
    error = Color(0xFFCF6679),         // Material design error color
    onError = Color(0xFFFFFFFF)        // White for text/icons on error color
    // Define other colors like surface variant, error container, etc., if needed.

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun CryptoAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> DarkColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}