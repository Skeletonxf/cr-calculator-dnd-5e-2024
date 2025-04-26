package io.github.skeletonxf.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import io.github.skeletonxf.ui.strings.LocalStrings
import io.github.skeletonxf.ui.theme.CRCalculatorTheme
import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen {
    @Serializable
    data object MainContent : Screen
    @Serializable
    data object Information : Screen
}

data class TitleButton(
    val text: String,
    val onClick: () -> Unit,
    val icon: ImageVector,
)

@Composable
fun titleCloseButton(
    onClose: () -> Unit,
) = TitleButton(
    text = LocalStrings.current.calculatorComponents.backButton,
    onClick = onClose,
    icon = Icons.Default.Close
)

@Composable
fun Screen(
    title: String,
    onClose: TitleButton?,
    onAction: TitleButton?,
    content: @Composable ColumnScope.() -> Unit,
) = CRCalculatorTheme {
//    SideEffect {
//        window.setTitle(title)
//    }
    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .systemBarsPadding()
            .imePadding()
            .fillMaxSize()
    ) {
        Surface(
            color = MaterialTheme.colorScheme.primary,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                onClose?.let {
                    IconButton(
                        onClick = onClose.onClick,
                    ) {
                        Icon(
                            imageVector = onClose.icon,
                            contentDescription = onClose.text,
                        )
                    }
                }
                Text(
                    text = title,
                    modifier = Modifier
                        .padding(8.dp)
                        .semantics { heading() },
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.weight(1F))
                onAction?.let {
                    IconButton(
                        onClick = onAction.onClick,
                    ) {
                        Icon(
                            imageVector = onAction.icon,
                            contentDescription = onAction.text,
                        )
                    }
                }
            }
        }
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            content = content
        )
    }
}
