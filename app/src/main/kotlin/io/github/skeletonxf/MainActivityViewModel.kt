package io.github.skeletonxf

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import io.github.skeletonxf.engine.ChallengeRating
import io.github.skeletonxf.ui.state.MonsterData
import io.github.skeletonxf.ui.state.Monsters
import io.github.skeletonxf.ui.state.PlayerBudgetData
import io.github.skeletonxf.ui.state.Players

class MainActivityViewModel : ViewModel() {
    val state = mutableStateOf(
        State(
            players = Players(
                list = listOf(
                    PlayerBudgetData(
                        level = 1,
                        quantity = 4
                    )
                ),
            ),
            monsters = Monsters(
                listOf(
                    MonsterData(
                        challengeRating = ChallengeRating.One,
                        quantity = 2,
                    )
                )
            )
        )
    )

    fun addPlayerRow() {
        state.value = state.value.let { s ->
            val previous = s.players.list.lastOrNull()
            s.copy(
                players = s.players.copy(
                    list = s.players.list + PlayerBudgetData(
                        level = previous?.level ?: 1,
                        quantity = previous?.quantity ?: 1
                    )
                )
            )
        }
    }

    fun removePlayerRow(index: Int) {
        state.value = state.value.let { s ->
            s.copy(players = s.players.remove(index))
        }
    }

    fun setPlayerQuantity(quantity: Int, index: Int) {
        state.value = state.value.let { s ->
            s.copy(players = s.players.setQuantity(quantity = quantity, index = index))
        }
    }

    fun setPlayerLevel(level: Int, index: Int) {
        state.value = state.value.let { s ->
            s.copy(players = s.players.setLevel(level = level, index = index))
        }
    }

    fun addMonsterRow() {
        state.value = state.value.let { s ->
            val previous = s.monsters.list.lastOrNull()
            s.copy(
                monsters = s.monsters.copy(
                    list = s.monsters.list + MonsterData(
                        challengeRating = previous?.challengeRating ?: ChallengeRating.One,
                        quantity = previous?.quantity ?: 1
                    )
                )
            )
        }
    }

    fun removeMonsterRow(index: Int) {
        state.value = state.value.let { s ->
            s.copy(monsters = s.monsters.remove(index))
        }
    }

    fun setMonsterQuantity(quantity: Int, index: Int) {
        state.value = state.value.let { s ->
            s.copy(monsters = s.monsters.setQuantity(quantity = quantity, index = index))
        }
    }

    fun setMonsterChallengeRating(challengeRating: ChallengeRating, index: Int) {
        state.value = state.value.let { s ->
            s.copy(
                monsters = s.monsters.setChallengeRating(
                    challengeRating = challengeRating,
                    index = index
                )
            )
        }
    }

    data class State(
        val players: Players,
        val monsters: Monsters,
    )
}