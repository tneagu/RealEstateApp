package com.tneagu.realestateapp.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme =
    lightColorScheme(
        primary = Primary,
        onPrimary = OnPrimary,
        primaryContainer = PrimaryContainer,
        onPrimaryContainer = OnPrimaryContainer,
        secondary = Secondary,
        onSecondary = OnSecondary,
        secondaryContainer = SecondaryContainer,
        onSecondaryContainer = OnSecondaryContainer,
        // Using secondary for tertiary
        tertiary = Secondary,
        onTertiary = OnSecondary,
        background = Background,
        onBackground = OnBackground,
        surface = Surface,
        onSurface = OnSurface,
        onSurfaceVariant = OnSurfaceVariant,
        error = Error,
        onError = OnError,
        errorContainer = ErrorContainer,
        onErrorContainer = OnErrorContainer,
        outline = Outline,
        outlineVariant = OutlineVariant,
    )

private val DarkColorScheme =
    darkColorScheme(
        primary = PrimaryDark,
        onPrimary = OnPrimary,
        primaryContainer = OnPrimaryContainer,
        onPrimaryContainer = PrimaryContainer,
        secondary = SecondaryDark,
        onSecondary = OnSecondary,
        secondaryContainer = OnSecondaryContainer,
        onSecondaryContainer = SecondaryContainer,
        tertiary = SecondaryDark,
        onTertiary = OnSecondary,
        background = BackgroundDark,
        onBackground = Color(0xFFE0E0E0),
        surface = SurfaceDark,
        onSurface = Color(0xFFE0E0E0),
        onSurfaceVariant = Color(0xFFBDBDBD),
        error = Error,
        onError = OnError,
        errorContainer = OnErrorContainer,
        onErrorContainer = ErrorContainer,
        outline = Color(0xFF424242),
        outlineVariant = Color(0xFF303030),
    )

/**
 * Material3 theme for Real Estate App.
 *
 * @param darkTheme Whether to use dark theme. Defaults to system setting.
 * @param content The composable content to theme.
 */
@Composable
fun RealEstateAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme =
        if (darkTheme) {
            DarkColorScheme
        } else {
            LightColorScheme
        }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}
