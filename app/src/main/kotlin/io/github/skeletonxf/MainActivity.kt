package io.github.skeletonxf

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import io.github.skeletonxf.engine.ChallengeRating
import io.github.skeletonxf.ui.BudgetPlot
import io.github.skeletonxf.ui.MonstersCard
import io.github.skeletonxf.ui.PlayersCard
import io.github.skeletonxf.ui.strings.LocalStrings
import io.github.skeletonxf.ui.theme.CRCalculatorTheme

typealias Row = Int

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        viewModel = MainActivityViewModel()
        setContent {
            CRCalculatorTheme {
                val state by viewModel.state
                Content(
                    state = state,
                    onSetPlayerQuantity = viewModel::setPlayerQuantity,
                    onSetPlayerLevel = viewModel::setPlayerLevel,
                    onRemovePlayerRow = viewModel::removePlayerRow,
                    onAddPlayerRow = viewModel::addPlayerRow,
                    onSetMonsterQuantity = viewModel::setMonsterQuantity,
                    onSetMonsterChallengeRating = viewModel::setMonsterChallengeRating,
                    onRemoveMonsterRow = viewModel::removeMonsterRow,
                    onAddMonsterRow = viewModel::addMonsterRow,
                )
                val strings = LocalStrings.current.calculator
                val darkScrim = MaterialTheme.colorScheme.primary
                SideEffect {
                    window.setTitle(strings.title)
                    enableEdgeToEdge(
                        statusBarStyle = SystemBarStyle.dark(darkScrim.toArgb()),
                        navigationBarStyle = SystemBarStyle.light(
                            Color.Transparent.toArgb(),
                            Color.Transparent.toArgb(),
                        )
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun EmptyContent() = CRCalculatorTheme {
    val viewModel = remember { MainActivityViewModel() }
    val state by viewModel.state
    Content(
        state = state,
        onSetPlayerQuantity = viewModel::setPlayerQuantity,
        onSetPlayerLevel = viewModel::setPlayerLevel,
        onRemovePlayerRow = viewModel::removePlayerRow,
        onAddPlayerRow = viewModel::addPlayerRow,
        onSetMonsterQuantity = viewModel::setMonsterQuantity,
        onSetMonsterChallengeRating = viewModel::setMonsterChallengeRating,
        onRemoveMonsterRow = viewModel::removeMonsterRow,
        onAddMonsterRow = viewModel::addMonsterRow,
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
    Content(
        state = state,
        onSetPlayerQuantity = viewModel::setPlayerQuantity,
        onSetPlayerLevel = viewModel::setPlayerLevel,
        onRemovePlayerRow = viewModel::removePlayerRow,
        onAddPlayerRow = viewModel::addPlayerRow,
        onSetMonsterQuantity = viewModel::setMonsterQuantity,
        onSetMonsterChallengeRating = viewModel::setMonsterChallengeRating,
        onRemoveMonsterRow = viewModel::removeMonsterRow,
        onAddMonsterRow = viewModel::addMonsterRow,
    )
}

@Composable
fun Content(
    state: MainActivityViewModel.State,
    onSetPlayerQuantity: (Int, Row) -> Unit,
    onSetPlayerLevel: (Int, Row) -> Unit,
    onRemovePlayerRow: (Row) -> Unit,
    onAddPlayerRow: () -> Unit,
    onSetMonsterQuantity: (Int, Row) -> Unit,
    onSetMonsterChallengeRating: (ChallengeRating, Row) -> Unit,
    onRemoveMonsterRow: (Row) -> Unit,
    onAddMonsterRow: () -> Unit,
) {
    val strings = LocalStrings.current.calculator
    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .systemBarsPadding()
            .imePadding()
            .fillMaxSize()
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary,
        ) {
            Text(
                text = strings.title,
                modifier = Modifier
                    .padding(8.dp)
                    .semantics { heading() },
                style = MaterialTheme.typography.headlineMedium
            )
        }
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
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
}