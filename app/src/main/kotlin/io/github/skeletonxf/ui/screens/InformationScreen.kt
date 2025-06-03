package io.github.skeletonxf.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.skeletonxf.engine.Version
import io.github.skeletonxf.ui.strings.LocalStrings
import io.github.skeletonxf.ui.theme.CRCalculatorTheme

private const val HOMEPAGE = "https://github.com/Skeletonxf/cr-calculator-dnd-5e-2024"

@Composable
fun InformationScreen(
    onClose: () -> Unit,
) {
    val uriHandler = LocalUriHandler.current
    val strings = LocalStrings.current.information
    Screen(
        title = strings.title,
        onClose = titleCloseButton(onClose = onClose),
        onAction = null,
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        SelectionContainer {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = strings.appVersion(Version.VERSION)
                )
                Text(
                    text = strings.homepage(HOMEPAGE),
                )
                Button(
                    onClick = {
                        try {
                            uriHandler.openUri(HOMEPAGE)
                        } catch (error: IllegalArgumentException) {
                            // Swallow for now till have error UI
                        }
                    }
                ) {
                    Text(
                        text = strings.homepageButton
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun InformationContent() = CRCalculatorTheme {
    InformationScreen(onClose = {})
}