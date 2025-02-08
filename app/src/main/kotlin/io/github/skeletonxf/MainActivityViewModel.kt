package io.github.skeletonxf

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
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
                )
            )
        )
    )

    fun addRow() {
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

    fun removeRow(index: Int) {
        state.value = state.value.let { s ->
            s.copy(players = s.players.remove(index))
        }
    }

    fun setQuantity(quantity: Int, index: Int) {
        state.value = state.value.let { s ->
            s.copy(players = s.players.setQuantity(quantity = quantity, index = index))
        }
    }

    fun setLevel(level: Int, index: Int) {
        state.value = state.value.let { s ->
            s.copy(players = s.players.setLevel(level = level, index = index))
        }
    }

    data class State(
        val players: Players,
    )
}