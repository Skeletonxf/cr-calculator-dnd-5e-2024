package io.github.skeletonxf.ui.state

import io.github.skeletonxf.engine.Budget

data class Players(
    val list: List<PlayerBudgetData>,
) {
    val lowBudget = budget(type = Budget.Type.Low)
    val moderateBudget = budget(type = Budget.Type.Moderate)
    val highBudget = budget(type = Budget.Type.High)

    fun remove(index: Int): Players = copy(
        list = list.toMutableList().apply {
            if (indexInRange(index)) {
                removeAt(index)
            }
        }
    )

    fun setQuantity(quantity: Int, index: Int) = copy(
        list = list.toMutableList().apply {
            if (indexInRange(index)) {
                set(index, list[index].copy(quantity = quantity))
            }
        }
    )

    fun setLevel(level: Int, index: Int) = copy(
        list = list.toMutableList().apply {
            if (indexInRange(index)) {
                set(index, list[index].copy(level = level))
            }
        }
    )

    private fun indexInRange(index: Int) = index >= 0 && index <= list.lastIndex

    private fun budget(type: Budget.Type): Int = list.sumOf { row ->
        Budget.get(level = row.level, type = type) * row.quantity
    }
}

data class PlayerBudgetData(
    val level: Int,
    val quantity: Int,
)