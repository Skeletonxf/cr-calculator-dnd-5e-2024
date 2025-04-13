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
import androidx.compose.ui.tooling.preview.Preview
import io.github.skeletonxf.ui.strings.English
import io.github.skeletonxf.ui.strings.LocalStrings

private val LightColorScheme = lightColorScheme(
    // TODO: Want to use 89a2eb, c421a3, & d8f5fa as those are from icon, material theme builder
    // has dulled colours a lot and is barely different from default material theme
    primary = Color(0xFF4a5c92),
    onPrimary = Color.White,
    secondary = Color(0xFF844c72),
    onSecondary = Color.White,
    tertiary = Color(0xFF715188),
    onTertiary = Color.White,
    primaryContainer = Color(0xFFdbe1ff),
    onPrimaryContainer = Color(0xFF314578),
    secondaryContainer = Color(0xFFffd8ed),
    onSecondaryContainer = Color(0xFF693459),
    tertiaryContainer = Color(0xFFf3daff),
    onTertiaryContainer = Color(0xFF583a6f),
    error = Color(0xFFba1a1a),
    onError = Color.White,
    errorContainer = Color(0xFFffdad6),
    onErrorContainer = Color(0xFF93000a),
    surfaceDim = Color(0xFFd5dbdc),
    surface = Color(0xFFf5fafb),
    surfaceBright = Color(0xFFf5fafb),
    surfaceContainerLowest = Color(0xFFffffff),
    surfaceContainerLow = Color(0xFFeff5f6),
    surfaceContainer = Color(0xFFe9eff0),
    surfaceContainerHigh = Color(0xFFe3e9ea),
    surfaceContainerHighest = Color(0xFFdee3e4),
    onSurface = Color(0xFF171d1e),
    onSurfaceVariant = Color(0xFF3f484a),
    outline = Color(0xFF6f797a),
    outlineVariant = Color(0xFFbfc8ca),
    inverseSurface = Color(0xFFdee3e4),
    inverseOnSurface = Color(0xFF2b3133),
    inversePrimary = Color(0xFF4a5c92),
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
                surface = MaterialTheme.colorScheme.tertiary,
                label = "Tertiary"
            ),
            SurfacePreview(
                surface = MaterialTheme.colorScheme.surface,
                label = "Surface"
            ),
            SurfacePreview(
                surface = MaterialTheme.colorScheme.surfaceVariant,
                label = "Surface variant"
            ),
            SurfacePreview(
                surface = MaterialTheme.colorScheme.outline,
                label = "Outline"
            ),
            SurfacePreview(
                surface = MaterialTheme.colorScheme.outlineVariant,
                label = "Outline variant"
            )
        ).forEach { preview ->
            Surface(color = preview.surface) {
                Text(text = preview.label)
            }
        }
    }
}