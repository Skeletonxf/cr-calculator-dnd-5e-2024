package io.github.skeletonxf.ui.state

import io.github.skeletonxf.engine.ChallengeRating
import kotlin.math.pow

data class Monsters(
    val list: List<MonsterData>,
) {
    val xp: Int = list.sumOf { row -> row.xp }
    val descending = list.sortedByDescending { it.challengeRating.xp() }

    fun descendingAsGrid(maxPerRow: Int = 4) = MonsterGrid.from(this, maxPerRow)

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

data class MonsterGrid(
    private val data: List<List<MonsterGridItem>>,
) {
    data class MonsterGridItem(
        val cr: ChallengeRating,
        val width: Float,
    )

    val rows: Int = data.size
    val lastIndex: Int = data.lastIndex
    fun get(row: Int): List<MonsterGridItem> = data[row]

    companion object {
        fun from(monsters: Monsters, maxPerRow: Int = 4): MonsterGrid {
            val descending = monsters.descending
            val crs = descending.map { it.challengeRating }.toSet().sortedDescending()
            // Create equally decreasing fractions from 100% down to 1/x where x is number of different
            // crs, then shift them closer to 0 so we get to small fractions after the highest CR
            // quickly
            // We don't want any widths less than 1/maxPerRow, so need to coerce the widths
            // from 0 to 1 to 1/max to 1
            val minWidth = 1 / maxPerRow.toFloat()
            val widths = List(crs.size) { index ->
                (1.0F - ((index.toFloat()) / crs.size.toFloat())).pow(3F)
            }.map { w -> minWidth + (w * (1 - minWidth)) }
            val widthMap = crs.mapIndexed { index, cr -> cr to widths[index] }.toMap()
            val individualMonsters = descending
                .map { monsterRow -> List(monsterRow.quantity) { monsterRow.challengeRating } }
                .flatten()
            // Assign each monster in turn to the grid
            var widthUsed = 0.0F
            val grid = mutableListOf<MutableList<MonsterGridItem>>(mutableListOf())
            for (monster in individualMonsters) {
                val widthNeeded = widthMap[monster]!!
                val item = MonsterGridItem(cr = monster, width = widthNeeded)
                if (widthUsed + widthNeeded > 1.0) {
                    // doesn't fit on this row, add next row
                    widthUsed = widthNeeded
                    grid.add(mutableListOf(item))
                } else {
                    widthUsed += widthNeeded
                    // Insert monster on existing row or create row if grid hasn't been given one
                    // yet
                    grid.lastOrNull()?.add(item) ?: grid.add(mutableListOf(item))
                }
            }
            return MonsterGrid(data = grid)
        }
    }
}