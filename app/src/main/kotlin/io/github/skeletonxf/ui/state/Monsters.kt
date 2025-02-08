package io.github.skeletonxf.ui.state

import io.github.skeletonxf.engine.ChallengeRating

data class Monsters(
    val list: List<MonsterData>,
) {
    val xp: Int = list.sumOf { row -> row.xp }

    fun remove(index: Int): Monsters = copy(
        list = list.toMutableList().apply {
            if (indexInRange(index)) {
                removeAt(index)
            }
        }
    )

    fun setQuantity(quantity: Int, index: Int): Monsters = copy(
        list = list.toMutableList().apply {
            if (indexInRange(index)) {
                set(index, list[index].copy(quantity = quantity))
            }
        }
    )

    fun setChallengeRating(challengeRating: ChallengeRating, index: Int): Monsters = copy(
        list = list.toMutableList().apply {
            if (indexInRange(index)) {
                set(index, list[index].copy(challengeRating = challengeRating))
            }
        }
    )

    private fun indexInRange(index: Int) = index >= 0 && index <= list.lastIndex
}

data class MonsterData(
    val quantity: Int,
    val challengeRating: ChallengeRating,
) {
    val xp: Int = quantity * challengeRating.xp()
}