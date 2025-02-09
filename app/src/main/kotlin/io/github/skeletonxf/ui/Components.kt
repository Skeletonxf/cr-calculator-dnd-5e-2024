package io.github.skeletonxf.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import io.github.skeletonxf.engine.ChallengeRating
import io.github.skeletonxf.ui.state.MonsterData
import io.github.skeletonxf.ui.state.Monsters
import io.github.skeletonxf.ui.state.PlayerBudgetData
import io.github.skeletonxf.ui.state.Players
import kotlin.math.max

@Composable
fun NumberPicker(
    value: Int,
    onValueChange: (Int) -> Unit,
    label: String,
    options: List<Int>,
    modifier: Modifier = Modifier,
) = ValuePicker(
    value = value,
    onValueChange = onValueChange,
    label = label,
    options = options,
    formatter = { it.toString() },
    modifier = modifier,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <V : Any> ValuePicker(
    value: V,
    onValueChange: (V) -> Unit,
    label: String,
    options: List<V>,
    formatter: (V) -> String,
    modifier: Modifier = Modifier,
    padBelowToMatchLabel: Boolean = true,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val bottomPadding = if (padBelowToMatchLabel) {
        val textMeasurer = rememberTextMeasurer()
        val density = LocalDensity.current
        with(density) {
            textMeasurer
                // The floating text label in the OutlinedTextField is bodySmall
                // and half of it extends beyond the top of the text field borders
                // so we need to pad by half the size to vertically center the line
                // of text inside the text field
                .measure(text = label, style = MaterialTheme.typography.bodySmall)
                .size
                .height
                .let { it / 2 }
                .toDp()
        }
    } else {
        0.dp
    }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier,
    ) {
        OutlinedTextField(
            value = formatter(value),
            onValueChange = {},
            modifier = Modifier
                .width(100.dp)
                .menuAnchor()
                .padding(bottom = bottomPadding),
            readOnly = true,
            label = { Text(text = label) },
            maxLines = 1,
            minLines = 1,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(text = formatter(option))
                    },
                    onClick = {
                        onValueChange(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun PlayerBudgetRow(
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    level: Int,
    onLevelChange: (Int) -> Unit,
    onDelete: () -> Unit,
) {
    Row(
        modifier = Modifier
            .sizeIn(maxWidth = 400.dp)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        NumberPicker(
            value = quantity,
            onValueChange = onQuantityChange,
            label = "Quantity",
            options = (1..12).toList(),
        )
        Spacer(modifier = Modifier.width(8.dp))
        NumberPicker(
            value = level,
            onValueChange = onLevelChange,
            label = "Level",
            options = (1..20).toList()
        )
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(
            onClick = onDelete,
        ) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Remove")
        }
    }
}

@Composable
fun MonsterBudgetRow(
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    challengeRating: ChallengeRating,
    onChallengeRatingChange: (ChallengeRating) -> Unit,
    onDelete: () -> Unit,
) {
    Row(
        modifier = Modifier
            .sizeIn(maxWidth = 400.dp)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        NumberPicker(
            value = quantity,
            onValueChange = onQuantityChange,
            label = "Quantity",
            options = (1..30).toList(),
        )
        Spacer(modifier = Modifier.width(8.dp))
        ValuePicker(
            value = challengeRating,
            onValueChange = onChallengeRatingChange,
            label = "CR",
            options = ChallengeRating.entries,
            formatter = { it.number },
        )
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(
            onClick = onDelete,
        ) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Remove")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "${(challengeRating.xp() * quantity)} XP")
    }
}

@Composable
fun XPBudget(
    state: Players,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "XP Budget", textAlign = TextAlign.Center)
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Low",
                    modifier = Modifier.padding(horizontal = 8.dp),
                    textAlign = TextAlign.Center
                )
                Text(text = state.lowBudget.toString(), textAlign = TextAlign.Center)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Moderate",
                    modifier = Modifier.padding(horizontal = 8.dp),
                    textAlign = TextAlign.Center
                )
                Text(text = state.moderateBudget.toString(), textAlign = TextAlign.Center)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "High",
                    modifier = Modifier.padding(horizontal = 8.dp),
                    textAlign = TextAlign.Center
                )
                Text(text = state.highBudget.toString(), textAlign = TextAlign.Center)
            }
        }
    }
}

@Composable
fun BudgetPlot(
    low: Int,
    moderate: Int,
    high: Int,
    monsters: Monsters,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme.let { colors ->
        listOf(
            colors.primary,
            colors.secondary,
            colors.outline,
            colors.outlineVariant,
        )
    }
    val alphas = listOf(
        1.0F,
        0.9F,
        0.8F,
        0.7F,
        0.6F,
    )
    Layout(
        content = {
            monsters.descending.forEachIndexed { index, monsters ->
                repeat(monsters.quantity) { i ->
                    Surface(
                        color = colors[index % colors.size].copy(alpha = alphas[i % 5]),
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(8.dp)
                                .sizeIn(minWidth = 48.dp, minHeight = 48.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text = monsters.challengeRating.number,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyLarge,
                            )
                            Text(
                                text = "${monsters.challengeRating.xp()} XP",
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                    }
                }
            }
            // TODO: Need 3 lines for low, moderate and high XP budgets, and fourth for
            // where the total XP spent ends up
        },
        modifier = modifier.sizeIn(minWidth = 400.dp, minHeight = 100.dp),
    ) { measurables, constraints ->
        val maxPerRow = 4
        // TODO: Pick a max based on our width available to support landscape and big screens
        val rowMarginPixels = 8.dp.toPx().toInt()
        val grid = monsters.descendingAsGrid(maxPerRow = maxPerRow)
        var gridRowIndex = 0
        var gridColumn = 0
        val totalWidthPixels = if (constraints.maxWidth != Constraints.Infinity) {
            constraints.maxWidth
        } else {
            throw IllegalStateException("Not handling non max width constraints yet")
        }
        val placeables = mutableListOf<Placeable>()
        var heightUsedPixels = 0
        val marginTotalPixels = max(0, maxPerRow - 1) * rowMarginPixels
        measurables.forEach { measurable ->
            if (gridRowIndex <= grid.lastIndex) {
                val gridRow = grid.get(gridRowIndex)
                val monster = gridRow[gridColumn]
                // Subtract total margin even if a particular row doesn't need all of it
                // as this keeps all blocks for a given CR the same size and better aligns
                // everything
                val rowFractionPixels = (totalWidthPixels * monster.width).toInt() -
                        marginTotalPixels
                val placeable = measurable.measure(
                    Constraints(
                        minWidth = rowFractionPixels,
                        maxWidth = rowFractionPixels,
                        maxHeight = constraints.maxHeight,
                    )
                )
                gridColumn += 1
                if (gridColumn > gridRow.lastIndex) {
                    gridColumn = 0
                    gridRowIndex += 1
                    heightUsedPixels += placeable.height
                }
                placeables.add(placeable)
            } else {
                // Handle non monster composable items here
            }
        }
        val height = max(heightUsedPixels, constraints.minHeight)
        layout(
            width = totalWidthPixels,
            height = height,
        ) {
            var row = 0
            var column = 0
            var heightClaimed = 0
            var widthClaimed = 0
            placeables.forEach { placeable ->
                if (row <= grid.lastIndex) {
                    val gridRow = grid.get(row)
                    // We want to center items within a row, so add half the unused width of the row
                    // as a starting position
                    val unused = ((1.0 - gridRow.sumOf { it.width.toDouble() }) * totalWidthPixels) -
                            ((gridRow.size - 1) * rowMarginPixels)
                    // FIXME: reserving space for unused parts of the row isn't horizontally
                    // centering rows where final items are a shorter width due to a lower CR
                    placeable.placeRelative(
                        x = (unused/2).toInt() + widthClaimed,
                        y = height - heightClaimed - placeable.height
                    )
                    column += 1
                    widthClaimed += placeable.width + rowMarginPixels
                    if (column > gridRow.lastIndex) {
                        row += 1
                        column = 0
                        heightClaimed += placeable.height
                        widthClaimed = 0
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun PlayerBudgetRowPreview() = PlayerBudgetRow(
    quantity = 6,
    onQuantityChange = {},
    level = 3,
    onLevelChange = {},
    onDelete = {},
)

@Composable
@Preview
fun MonsterBudgetRowPreview() = MonsterBudgetRow(
    quantity = 2,
    onQuantityChange = {},
    challengeRating = ChallengeRating.Six,
    onChallengeRatingChange = {},
    onDelete = {},
)

@Composable
@Preview
fun XPBudgetPreview() = XPBudget(
    state = Players(list = listOf(PlayerBudgetData(level = 5, quantity = 4)))
)

@Composable
@Preview
fun BudgetPlotPreview() = BudgetPlot(
    low = 0,
    moderate = 0,
    high = 0,
    monsters = Monsters(
        listOf(
            MonsterData(quantity = 1, challengeRating = ChallengeRating.Four),
            MonsterData(quantity = 1, challengeRating = ChallengeRating.Three),
            MonsterData(quantity = 1, challengeRating = ChallengeRating.Two),
            MonsterData(quantity = 3, challengeRating = ChallengeRating.One),
            MonsterData(quantity = 5, challengeRating = ChallengeRating.Half),
            MonsterData(quantity = 1, challengeRating = ChallengeRating.Quarter),
        )
    )
)