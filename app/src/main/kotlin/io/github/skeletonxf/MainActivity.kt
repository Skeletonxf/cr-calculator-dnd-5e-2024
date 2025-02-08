package io.github.skeletonxf

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.skeletonxf.engine.ChallengeRating
import io.github.skeletonxf.ui.MonsterBudgetRow

import io.github.skeletonxf.ui.PlayerBudgetRow
import io.github.skeletonxf.ui.XPBudget
import io.github.skeletonxf.ui.theme.CRCalculatorTheme

typealias Row = Int

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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
            }
            SideEffect {
                window.setTitle("CR Calculator")
            }
        }
    }
}

@Composable
@Preview
fun EmptyContent() {
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

@OptIn(ExperimentalLayoutApi::class)
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
                text = "CR Calculator",
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
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceContainer,
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 8.dp),
                ) {
                    Text(
                        text = "Players",
                        modifier = Modifier.padding(8.dp),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    state.players.list.forEachIndexed { index, row ->
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
                        verticalArrangement = Arrangement.Top,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Button(
                            onClick = onAddPlayerRow,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                        }
                        XPBudget(
                            state = state.players,
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 8.dp),
                ) {
                    Text(
                        text = "Monsters",
                        modifier = Modifier.padding(8.dp),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    state.monsters.list.forEachIndexed { index, row ->
                        MonsterBudgetRow(
                            quantity = row.quantity,
                            onQuantityChange = { onSetMonsterQuantity(it, index) },
                            challengeRating = row.challengeRating,
                            onChallengeRatingChange = { onSetMonsterChallengeRating(it, index) },
                            onDelete = { onRemoveMonsterRow(index) }
                        )
                    }
                    Button(
                        onClick = onAddMonsterRow,
                        modifier = Modifier
                            .padding(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                    }
                }
            }
        }
    }
}