package io.github.skeletonxf.ui.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.skeletonxf.MainActivityViewModel
import io.github.skeletonxf.engine.ChallengeRating
import io.github.skeletonxf.ui.BudgetPlot
import io.github.skeletonxf.ui.MonstersCard
import io.github.skeletonxf.ui.PlayersCard
import io.github.skeletonxf.ui.strings.LocalStrings
import io.github.skeletonxf.ui.theme.CRCalculatorTheme

typealias Row = Int

@Composable
fun MainContentScreen(
    state: MainActivityViewModel.State,
    onSetPlayerQuantity: (Int, Row) -> Unit,
    onSetPlayerLevel: (Int, Row) -> Unit,
    onRemovePlayerRow: (Row) -> Unit,
    onAddPlayerRow: () -> Unit,
    onSetMonsterQuantity: (Int, Row) -> Unit,
    onSetMonsterChallengeRating: (ChallengeRating, Row) -> Unit,
    onRemoveMonsterRow: (Row) -> Unit,
    onAddMonsterRow: () -> Unit,
    onInfo: () -> Unit,
) {
    val strings = LocalStrings.current.calculator
    Screen(
        title = strings.title,
        onClose = null,
        onAction = TitleButton(
            text = strings.infoButton,
            onClick = onInfo,
            icon = Icons.Default.Info
        ),
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        // TODO: Flow Row these two surfaces so they can go side by side on wide screens
        PlayersCard(
            players = state.players,
            onSetPlayerQuantity = onSetPlayerQuantity,
            onSetPlayerLevel = onSetPlayerLevel,
            onRemovePlayerRow = onRemovePlayerRow,
            onAddPlayerRow = onAddPlayerRow,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        MonstersCard(
            players = state.players,
            monsters = state.monsters,
            onSetMonsterQuantity = onSetMonsterQuantity,
            onSetMonsterChallengeRating = onSetMonsterChallengeRating,
            onRemoveMonsterRow = onRemoveMonsterRow,
            onAddMonsterRow = onAddMonsterRow,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        if (state.players.lowBudget > 0 || state.monsters.list.isNotEmpty()) {
            BudgetPlot(
                low = state.players.lowBudget,
                moderate = state.players.moderateBudget,
                high = state.players.highBudget,
                monsters = state.monsters,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
@Preview
fun EmptyContent() = CRCalculatorTheme {
    val viewModel = remember { MainActivityViewModel() }
    val state by viewModel.state
    MainContentScreen(
        state = state,
        onSetPlayerQuantity = viewModel::setPlayerQuantity,
        onSetPlayerLevel = viewModel::setPlayerLevel,
        onRemovePlayerRow = viewModel::removePlayerRow,
        onAddPlayerRow = viewModel::addPlayerRow,
        onSetMonsterQuantity = viewModel::setMonsterQuantity,
        onSetMonsterChallengeRating = viewModel::setMonsterChallengeRating,
        onRemoveMonsterRow = viewModel::removeMonsterRow,
        onAddMonsterRow = viewModel::addMonsterRow,
        onInfo = {},
    )
}

@Composable
@Preview
fun FilledContent() = CRCalculatorTheme {
    val viewModel = remember {
        val viewModel = MainActivityViewModel()
        viewModel.setPlayerLevel(3, 0)
        viewModel.setPlayerQuantity(4, 0)
        viewModel.setMonsterQuantity(2, 0)
        viewModel.addMonsterRow()
        viewModel.setMonsterQuantity(3, 1)
        viewModel.setMonsterChallengeRating(ChallengeRating.Two, 0)
        viewModel.setMonsterChallengeRating(ChallengeRating.One, 1)
        viewModel
    }
    val state by viewModel.state
    MainContentScreen(
        state = state,
        onSetPlayerQuantity = viewModel::setPlayerQuantity,
        onSetPlayerLevel = viewModel::setPlayerLevel,
        onRemovePlayerRow = viewModel::removePlayerRow,
        onAddPlayerRow = viewModel::addPlayerRow,
        onSetMonsterQuantity = viewModel::setMonsterQuantity,
        onSetMonsterChallengeRating = viewModel::setMonsterChallengeRating,
        onRemoveMonsterRow = viewModel::removeMonsterRow,
        onAddMonsterRow = viewModel::addMonsterRow,
        onInfo = {},
    )
}

