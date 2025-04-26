package io.github.skeletonxf.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import io.github.skeletonxf.engine.Budget
import io.github.skeletonxf.engine.ChallengeRating
import io.github.skeletonxf.ui.screens.Row
import io.github.skeletonxf.ui.state.MonsterData
import io.github.skeletonxf.ui.state.Monsters
import io.github.skeletonxf.ui.state.PlayerBudgetData
import io.github.skeletonxf.ui.state.Players
import io.github.skeletonxf.ui.strings.LocalStrings
import io.github.skeletonxf.ui.theme.CRCalculatorTheme
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
    val strings = LocalStrings.current.calculatorComponents
    Row(
        modifier = Modifier
            .sizeIn(maxWidth = 400.dp)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        NumberPicker(
            value = quantity,
            onValueChange = onQuantityChange,
            label = strings.quantity,
            options = (1..12).toList(),
        )
        Spacer(modifier = Modifier.width(8.dp))
        NumberPicker(
            value = level,
            onValueChange = onLevelChange,
            label = strings.level,
            options = (1..20).toList()
        )
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(
            onClick = onDelete,
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = strings.removeContentDescription,
            )
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
    val strings = LocalStrings.current.calculatorComponents
    Row(
        modifier = Modifier
            .sizeIn(maxWidth = 400.dp)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        NumberPicker(
            value = quantity,
            onValueChange = onQuantityChange,
            label = strings.quantity,
            options = (1..30).toList(),
        )
        Spacer(modifier = Modifier.width(8.dp))
        ValuePicker(
            value = challengeRating,
            onValueChange = onChallengeRatingChange,
            label = strings.challengeRating,
            options = ChallengeRating.entries,
            formatter = { it.number },
        )
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(
            onClick = onDelete,
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = strings.removeContentDescription,
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = strings.xpValue(challengeRating.xp() * quantity))
    }
}

@Composable
fun XPBudget(
    state: Players,
) {
    val strings = LocalStrings.current.calculatorComponents
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = strings.xpBudget, textAlign = TextAlign.Center)
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = strings.lowDifficulty,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    textAlign = TextAlign.Center
                )
                Text(text = state.lowBudget.toString(), textAlign = TextAlign.Center)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = strings.moderateDifficulty,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    textAlign = TextAlign.Center
                )
                Text(text = state.moderateBudget.toString(), textAlign = TextAlign.Center)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = strings.highDifficulty,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    textAlign = TextAlign.Center
                )
                Text(text = state.highBudget.toString(), textAlign = TextAlign.Center)
            }
        }
    }
}

@Composable
private fun MonsterBlock(
    color: Color,
    contentColor: Color,
    challengeRating: ChallengeRating,
) = Surface(
    color = color,
    contentColor = contentColor,
) {
    val strings = LocalStrings.current.calculatorComponents
    Column(
        modifier = Modifier
            .padding(2.dp)
            .sizeIn(minHeight = 56.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = challengeRating.number,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
        )
        Text(
            text = strings.xpValue(challengeRating.xp()),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Composable
private fun UnspentBlock(
    color: Color,
    xp: Int,
    type: Budget.Type,
) = Surface(
    color = color,
) {
    val strings = LocalStrings.current.calculatorComponents
    Column(
        modifier = Modifier
            .padding(2.dp)
            .sizeIn(minHeight = 56.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = strings.unspent,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall,
        )
        Text(
            text = when (type) {
                Budget.Type.Low -> strings.lowDifficulty
                Budget.Type.Moderate -> strings.moderateDifficulty
                Budget.Type.High -> strings.highDifficulty
            },
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall,
        )
        Text(
            text = strings.xpValue(xp),
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

/**
 * Plots the budgets, either the xp budgets must be non zero or the monsters must be non
 * empty.
 */
@Composable
fun BudgetPlot(
    low: Int,
    moderate: Int,
    high: Int,
    monsters: Monsters,
    modifier: Modifier = Modifier,
) {
    val strings = LocalStrings.current.calculatorComponents
    val colors = MaterialTheme.colorScheme.let { colors ->
        listOf(
            colors.primary,
            colors.secondary,
            colors.tertiary,
        )
    }
    val alphas = listOf(
        1.0F,
        0.9F,
        0.8F,
        0.7F,
        0.6F,
    )
    val unspentLowXP = low - monsters.xp
    val unspentModerateXP = moderate - monsters.xp
    val unspentHighXP = high - monsters.xp
    val unspentBlocks = if (unspentLowXP > 0) {
        3
    } else if (unspentModerateXP > 0) {
        2
    } else {
        1
    }
    Layout(
        content = {
            monsters.descending.forEachIndexed { index, monsters ->
                repeat(monsters.quantity) { i ->
                    MonsterBlock(
                        color = colors[index % colors.size].copy(alpha = alphas[i % 5]),
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        challengeRating = monsters.challengeRating,
                    )
                }
            }
            if (unspentLowXP > 0) {
                UnspentBlock(
                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                    xp = unspentLowXP,
                    type = Budget.Type.Low,
                )
            }
            if (unspentModerateXP > 0) {
                if (unspentLowXP > 0) {
                    // If the low budget hasn't been met we've already included an unspent block
                    // for it so the remaining xp to reach moderate is just the difference between
                    // it and low
                    UnspentBlock(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        xp = moderate - low,
                        type = Budget.Type.Moderate,
                    )
                } else {
                    UnspentBlock(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        xp = unspentModerateXP,
                        type = Budget.Type.Moderate,
                    )
                }
            }
            if (unspentHighXP > 0) {
                if (unspentModerateXP > 0) {
                    // If the moderate budget hasn't been met we've already included an unspent
                    // block for it so the remaining xp to reach high is just the difference between
                    // it and moderate
                    UnspentBlock(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        xp = high - moderate,
                        type = Budget.Type.High,
                    )
                } else {
                    UnspentBlock(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        xp = unspentHighXP,
                        type = Budget.Type.High,
                    )
                }
            }
            Surface(
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Text(
                        text = strings.twoLineLowXP(low),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Text(
                        text = strings.twoLineModerateXP(moderate),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Text(
                        text = strings.twoLineHighXP(high),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        modifier = modifier.sizeIn(minWidth = 400.dp, minHeight = 100.dp),
    ) { measurables, constraints ->
        val maxPerRow = 4
        val budgetLinesPixels = 80.dp.toPx().toInt()
        val unspentBlockFraction = 1.0 / 3.0
        // TODO: Pick a max based on our width available to support landscape and big screens
        val rowMarginPixels = 8.dp.toPx().toInt()
        val grid = monsters.descendingAsGrid(
            lowBudget = low,
            moderateBudget = moderate,
            highBudget = high,
            maxPerRow = maxPerRow
        )
        var gridRowIndex = 0
        var gridColumn = 0
        val totalWidthPixels = if (constraints.maxWidth != Constraints.Infinity) {
            constraints.maxWidth - budgetLinesPixels
        } else {
            throw IllegalStateException("Not handling non max width constraints yet")
        }
        if (totalWidthPixels <= 0) {
            throw IllegalStateException("Need more width available")
        }
        val placeables = mutableListOf<Placeable>()
        var heightUsedPixels = 0
        val rowHeightsPixels = mutableListOf<Int>()
        var rowMaxHeightPixels = 0
        val marginTotalPixels = max(0, maxPerRow - 1) * rowMarginPixels
        measurables.dropLast(3).forEach { measurable ->
            if (gridRowIndex > grid.lastIndex) {
                val rowFractionPixels = (totalWidthPixels * unspentBlockFraction).toInt() -
                    marginTotalPixels
                val placeable = measurable.measure(
                    Constraints(
                        minWidth = rowFractionPixels,
                        maxWidth = rowFractionPixels,
                        maxHeight = constraints.maxHeight,
                    )
                )
                gridColumn += 1
                rowMaxHeightPixels = max(rowMaxHeightPixels, placeable.height)
                if (gridColumn > unspentBlocks - 1) {
                    heightUsedPixels += rowMaxHeightPixels
                    rowHeightsPixels.add(rowMaxHeightPixels)
                }
                placeables.add(placeable)
                // We don't need to abort here because we control the input so will only have
                // as many loops of this as unspent blocks to display
            } else {
                val gridRow = grid.get(gridRowIndex)
                val monster = gridRow[gridColumn]
                // Subtract total margin even if a particular row doesn't need all of it
                // as this keeps all blocks for a given CR the same size and better aligns
                // everything
                val rowFractionPixels = (totalWidthPixels * monster.width).toInt() -
                        marginTotalPixels
                val placeable = measurable.measure(
                    // FIXME: We really want to make all boxes on the same row the same height
                    // or at least fake it if that's not possible
                    Constraints(
                        minWidth = rowFractionPixels,
                        maxWidth = rowFractionPixels,
                        maxHeight = constraints.maxHeight,
                    )
                )
                gridColumn += 1
                rowMaxHeightPixels = max(rowMaxHeightPixels, placeable.height)
                if (gridColumn > gridRow.lastIndex) {
                    gridColumn = 0
                    gridRowIndex += 1
                    heightUsedPixels += rowMaxHeightPixels
                    rowHeightsPixels.add(rowMaxHeightPixels)
                    rowMaxHeightPixels = 0
                }
                placeables.add(placeable)
            }
        }
        val budgets = measurables.takeLast(3)
        val lowBudgetHeight = rowHeightsPixels.take(grid.lowBudget.row).sum() +
                (rowHeightsPixels[grid.lowBudget.row] * grid.lowBudget.fraction).toInt()
        val lowBudget = budgets[0].measure(
            Constraints(
                minWidth = budgetLinesPixels,
                maxWidth = budgetLinesPixels,
                minHeight = lowBudgetHeight,
                maxHeight = lowBudgetHeight
            )
        )
        val moderateBudgetHeight = rowHeightsPixels.take(grid.moderateBudget.row).sum() +
                (rowHeightsPixels[grid.moderateBudget.row] * grid.moderateBudget.fraction).toInt()
        val moderateBudget = budgets[1].measure(
            Constraints(
                minWidth = budgetLinesPixels,
                maxWidth = budgetLinesPixels,
                minHeight = moderateBudgetHeight,
                maxHeight = moderateBudgetHeight
            )
        )
        val highBudgetHeight = rowHeightsPixels.take(grid.highBudget.row).sum() +
                (rowHeightsPixels[grid.highBudget.row] * grid.highBudget.fraction).toInt()
        val highBudget = budgets[2].measure(
            Constraints(
                minWidth = budgetLinesPixels,
                maxWidth = budgetLinesPixels,
                minHeight = highBudgetHeight,
                maxHeight = highBudgetHeight
            )
        )
        val height = max(heightUsedPixels, constraints.minHeight)
        layout(
            width = totalWidthPixels + budgetLinesPixels,
            height = height,
        ) {
            var row = 0
            var column = 0
            var heightClaimed = 0
            var highestInRow = 0
            var widthClaimed = 0
            // FIXME: Calculation to center rows is still a little off for some items
            // Should just convert to iterating through each row at a time so we can
            // literally count the pixels needed by each row ourselves
            placeables.forEach { placeable ->
                if (row <= grid.lastIndex) {
                    val gridRow = grid.get(row)
                    // We want to center items within a row, so add half the unused width of the row
                    // as a starting position
                    val usedFraction = gridRow.sumOf { it.width.toDouble() }
                    val usedMargins = (gridRow.size - 1) * rowMarginPixels
                    val unused = totalWidthPixels - (usedFraction * (totalWidthPixels - usedMargins))
                    placeable.placeRelative(
                        x = (unused / 2).toInt() + widthClaimed,
                        y = height - heightClaimed - placeable.height
                    )
                    column += 1
                    widthClaimed += placeable.width + rowMarginPixels
                    highestInRow = max(highestInRow, placeable.height)
                    if (column > gridRow.lastIndex) {
                        row += 1
                        column = 0
                        heightClaimed += highestInRow
                        highestInRow = 0
                        widthClaimed = 0
                    }
                } else {
                    val usedFraction = unspentBlockFraction * unspentBlocks
                    val usedMargins = (unspentBlocks - 1) * rowMarginPixels
                    val unused = totalWidthPixels - (usedFraction * (totalWidthPixels - usedMargins))
                    placeable.placeRelative(
                        x = (unused / 2).toInt() + widthClaimed,
                        y = height - heightClaimed - placeable.height
                    )
                    column += 1
                    widthClaimed += placeable.width + rowMarginPixels
                    highestInRow = max(highestInRow, placeable.height)
                    if (column > unspentBlocks - 1) {
                        row += 1
                        column = 0
                        heightClaimed += highestInRow
                        highestInRow = 0
                        widthClaimed = 0
                    }
                }
            }
            highBudget.placeRelative(x =  totalWidthPixels, y = height - highBudget.height)
            moderateBudget.placeRelative(x = totalWidthPixels, y = height - moderateBudget.height)
            lowBudget.placeRelative(x = totalWidthPixels, y = height - lowBudget.height)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PlayersCard(
    players: Players,
    onSetPlayerQuantity: (Int, Row) -> Unit,
    onSetPlayerLevel: (Int, Row) -> Unit,
    onRemovePlayerRow: (Row) -> Unit,
    onAddPlayerRow: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val strings = LocalStrings.current.calculatorComponents
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceContainer,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp),
        ) {
            Text(
                text = strings.players,
                modifier = Modifier.padding(8.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
            )
            players.list.forEachIndexed { index, row ->
                PlayerBudgetRow(
                    quantity = row.quantity,
                    onQuantityChange = { onSetPlayerQuantity(it, index) },
                    level = row.level,
                    onLevelChange = { onSetPlayerLevel(it, index) },
                    onDelete = { onRemovePlayerRow(index) }
                )
            }
            FlowRow(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Button(
                    onClick = onAddPlayerRow,
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = strings.addContentDescription
                    )
                }
                XPBudget(
                    state = players,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MonstersCard(
    players: Players,
    monsters: Monsters,
    onSetMonsterQuantity: (Int, Row) -> Unit,
    onSetMonsterChallengeRating: (ChallengeRating, Row) -> Unit,
    onRemoveMonsterRow: (Row) -> Unit,
    onAddMonsterRow: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val strings = LocalStrings.current.calculatorComponents
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp),
        ) {
            Text(
                text = strings.monsters,
                modifier = Modifier.padding(8.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
            )
            monsters.list.forEachIndexed { index, row ->
                MonsterBudgetRow(
                    quantity = row.quantity,
                    onQuantityChange = { onSetMonsterQuantity(it, index) },
                    challengeRating = row.challengeRating,
                    onChallengeRatingChange = { onSetMonsterChallengeRating(it, index) },
                    onDelete = { onRemoveMonsterRow(index) }
                )
            }
            FlowRow(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Button(
                    onClick = onAddMonsterRow,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = strings.addContentDescription,
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if (players.list.isNotEmpty()) {
                        when {
                            monsters.xp > players.highBudget -> {
                                Text(
                                    text = strings.xpFraction(
                                        monsters.xp,
                                        players.highBudget
                                    ),
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = strings.pastHighDifficulty,
                                    textAlign = TextAlign.Center
                                )
                            }

                            monsters.xp > players.moderateBudget -> {
                                Text(
                                    text = strings.xpFraction(
                                        monsters.xp,
                                        players.highBudget
                                    ),
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = strings.highDifficulty,
                                    textAlign = TextAlign.Center
                                )
                            }

                            monsters.xp > players.lowBudget -> {
                                Text(
                                    text = strings.xpFraction(
                                        monsters.xp,
                                        players.moderateBudget
                                    ),
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = strings.moderateDifficulty,
                                    textAlign = TextAlign.Center
                                )
                            }

                            monsters.xp > 0 -> {
                                Text(
                                    text = strings.xpFraction(
                                        monsters.xp,
                                        players.lowBudget
                                    ),
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = strings.lowDifficulty,
                                    textAlign = TextAlign.Center
                                )
                            }

                            else -> Text(text = strings.noXP)
                        }
                    } else {
                        Text(text = strings.xpValue(monsters.xp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
@Preview
fun PlayerBudgetRowPreview() = CRCalculatorTheme {
    PlayerBudgetRow(
        quantity = 6,
        onQuantityChange = {},
        level = 3,
        onLevelChange = {},
        onDelete = {},
    )
}

@Composable
@Preview
fun MonsterBudgetRowPreview()= CRCalculatorTheme {
    MonsterBudgetRow(
        quantity = 2,
        onQuantityChange = {},
        challengeRating = ChallengeRating.Six,
        onChallengeRatingChange = {},
        onDelete = {},
    )
}

@Composable
@Preview
fun XPBudgetPreview() = CRCalculatorTheme {
    XPBudget(
        state = Players(list = listOf(PlayerBudgetData(level = 5, quantity = 4)))
    )
}

@Composable
@Preview(widthDp = 500)
fun BudgetPlotPreview() = CRCalculatorTheme {
    BudgetPlot(
        low = 2000,
        moderate = 3000,
        high = 4400,
        monsters = Monsters(
            listOf(
                MonsterData(quantity = 1, challengeRating = ChallengeRating.Four),
                MonsterData(quantity = 1, challengeRating = ChallengeRating.Three),
                MonsterData(quantity = 1, challengeRating = ChallengeRating.Two),
                MonsterData(quantity = 3, challengeRating = ChallengeRating.One),
                MonsterData(quantity = 5, challengeRating = ChallengeRating.Half),
                MonsterData(quantity = 1, challengeRating = ChallengeRating.Quarter),
            )
        ),
    )
}

@Composable
@Preview(widthDp = 400)
fun BudgetPlot2Preview() = CRCalculatorTheme {
    BudgetPlot(
        low = 1000,
        moderate = 1500,
        high = 2000,
        monsters = Monsters(
            listOf(
                MonsterData(quantity = 2, challengeRating = ChallengeRating.One),
                MonsterData(quantity = 2, challengeRating = ChallengeRating.Two),
                MonsterData(quantity = 1, challengeRating = ChallengeRating.Three),
            )
        ),
    )
}