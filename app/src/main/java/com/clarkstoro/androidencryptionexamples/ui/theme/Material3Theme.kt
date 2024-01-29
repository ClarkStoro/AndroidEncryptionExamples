package com.clarkstoro.androidencryptionexamples.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalConfiguration
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorScheme = darkColorScheme()

private val LightColorScheme = lightColorScheme()

// M3 stands for Material 3.

@Composable
fun AppM3Theme(
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
            color = colorScheme.background,
            darkIcons = useDarkIcons
        )

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

object AppM3Theme {
    val dimens: Dimensions
        @Composable
        get() = LocalAppDimens.current
}


private val LocalAppDimens = staticCompositionLocalOf { defaultDimensions }
val Dimens: Dimensions
    @Composable
    get() = AppM3Theme.dimens

@Composable
fun ProvideDimens(dimensions: Dimensions, content: @Composable () -> Unit) {
    val dimensionSet = remember { dimensions }
    CompositionLocalProvider(LocalAppDimens provides dimensionSet, content = content)
}