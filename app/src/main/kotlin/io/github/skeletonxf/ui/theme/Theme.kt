package io.github.skeletonxf.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.tooling.preview.Preview
import io.github.skeletonxf.ui.strings.English
import io.github.skeletonxf.ui.strings.LocalStrings

private val blue = Color(0xFF89a2eb)
private val red = Color(0xFFc421a3)
private val pale = Color(0xFFd8f5fa)

// Red starts off a little too dark for text against, so we create tones that are lighter
private val redLighter = Color(0xFFE566CC)

// Blue starts off on the light side for text against, so we create tones that are darker
private val blueDarker = Color(0xFF7894E8)

// Pale starts off on the light side for text against, so we create tones that are darker
private val paleDarker = Color(0xFFC9F0F7)
private val paleMoreDarker = Color(0xFF98E2EB)

// Needs white against instead of black
private val blueDark = Color(0xFF1D40AA)
private val redDark = Color(0xFF9D1B83)

private val purpleDark = Color(0xFF79369B)
private val purple = Color(0xFFB882D3)

private val LightColorScheme = lightColorScheme(
    // TODO: Want to use 89a2eb, c421a3, & d8f5fa as those are from icon, material theme builder
    // has dulled colours a lot and is barely different from default material theme
    primary = blueDark,
    onPrimary = Color.White,
    secondary = redDark,
    onSecondary = Color.White,
    tertiary = purpleDark,
    onTertiary = Color.White,
    primaryContainer = blue,
    onPrimaryContainer = Color.Black,
    surfaceDim = paleDarker,
    surface = pale,
    surfaceBright = pale,
    surfaceContainerLow = paleDarker,
    surfaceContainer = pale,
    surfaceContainerHigh = pale,
    surfaceVariant = redLighter,
    onSurface = Color.Black,
    onSurfaceVariant = Color.Black,
    scrim = Color(0xFF000000),
)

@Composable
fun CRCalculatorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // TODO
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme

    CompositionLocalProvider(LocalStrings provides English.strings) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content
        )
    }
}

private data class SurfacePreview(
    val surface: Color,
    val label: String,
)

@OptIn(ExperimentalStdlibApi::class)
@Composable
@Preview
fun Surfaces() = CRCalculatorTheme {
    Column {
        listOf(
            SurfacePreview(
                surface = MaterialTheme.colorScheme.primary,
                label = "Primary"
            ),
            SurfacePreview(
                surface = MaterialTheme.colorScheme.secondary,
                label = "Secondary"
            ),
            SurfacePreview(
                surface = MaterialTheme.colorScheme.surface,
                label = "Surface"
            ),
            SurfacePreview(
                surface = MaterialTheme.colorScheme.tertiary,
                label = "Tertiary"
            ),
            SurfacePreview(
                surface = MaterialTheme.colorScheme.surfaceVariant,
                label = "Surface variant"
            ),
            SurfacePreview(
                surface = MaterialTheme.colorScheme.primaryContainer,
                label = "Primary container"
            ),
            SurfacePreview(
                surface = MaterialTheme.colorScheme.surfaceContainerHigh,
                label = "Surface container high"
            )
        ).forEach { preview ->
            Surface(color = preview.surface) {
                Text(text = preview.label)
            }
        }
    }
}