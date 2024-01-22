package com.clarkstoro.encryptionexample.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorScheme = darkColorScheme(
    primary = Purple200,
    secondary = Purple700,
    tertiary = Teal200,
    background = Color.Black,
    surface = Color.Black,
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onBackground = Color.Black,
    onSurface = Color.White,
)

private val LightColorScheme = lightColorScheme(
    primary = Purple500,
    secondary = Purple700,
    tertiary = Teal200,
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    outlineVariant = Color.White
)

// M3 stands for Material 3.

@Composable
fun BoilerplateComposeM3Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val configuration = LocalConfiguration.current
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val dimensions = if (configuration.screenWidthDp <= 360) smallDimensions else defaultDimensions

    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !isSystemInDarkTheme()

    DisposableEffect(systemUiController, useDarkIcons) {
        // Update all of the system bar colors to be transparent, and use
        // dark icons if we're in light theme
        systemUiController.setSystemBarsColor(
            color = colorScheme.onPrimary,
            darkIcons = useDarkIcons
        )

        // setStatusBarColor() and setNavigationBarColor() also exist

        onDispose {}
    }

    ProvideDimens(dimensions = dimensions) {
        // The shapes are the default ones for a Material 3 Theme.
        MaterialTheme(
            colorScheme = colorScheme,
            typography = TypographyM3,
            content = content
        )
    }
}