package io.github.skeletonxf.ui.state

import io.github.skeletonxf.engine.ChallengeRating
import kotlin.math.max
import kotlin.math.pow

data class Monsters(
    val list: List<MonsterData>,
) {
    val xp: Int = list.sumOf { row -> row.xp }
    val descending = list.sortedByDescending { it.challengeRating.xp() }

    fun descendingAsGrid(
        lowBudget: Int,
        moderateBudget: Int,
        highBudget: Int,
        maxPerRow: Int = 4
    ) = MonsterGrid.from(this, lowBudget, moderateBudget, highBudget, maxPerRow)

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

data class BudgetThreshold(val row: Int, val fraction: Float, val xp: Int)

data class MonsterGrid(
    private val data: List<List<MonsterGridItem>>,
    val lowBudget: BudgetThreshold,
    val moderateBudget: BudgetThreshold,
    val highBudget: BudgetThreshold,
) {
    data class MonsterGridItem(
        val cr: ChallengeRating,
        val width: Float,
    )

    val rows: Int = data.size
    val lastIndex: Int = data.lastIndex
    fun get(row: Int): List<MonsterGridItem> = data[row]

    companion object {
        fun from(
            monsters: Monsters,
            lowBudget: Int,
            moderateBudget: Int,
            highBudget: Int,
            maxPerRow: Int = 4
        ): MonsterGrid {
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
            val grid = mutableListOf<MutableList<MonsterGridItem>>()
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
            var row = 0
            var column = 0
            var lowBudgetThreshold: BudgetThreshold? = null
            var moderateBudgetThreshold: BudgetThreshold? = null
            var highBudgetThreshold: BudgetThreshold? = null
            var xpSpent = 0
            while (individualMonsters.isNotEmpty() && row <= grid.lastIndex) {
                val gridRow = grid[row]
                val monsterRowFraction = 1.0F / gridRow.size
                val xp = gridRow[column].cr.xp()
                xpSpent += xp
                // A large XP monster might shoot past the XP budget threshold, in
                // which case we want to assign a lower fraction to the threshold than
                // the fraction the monster takes the XP spent to
                val start = (column.toFloat()) / gridRow.size
                if (lowBudgetThreshold == null && xpSpent >= lowBudget) {
                    val xpOver = xpSpent - lowBudget
                    val fractionalOvershoot = xpOver.toFloat() / xp.toFloat()
                    lowBudgetThreshold = BudgetThreshold(
                        row = row,
                        fraction = start + (monsterRowFraction * (1.0F - fractionalOvershoot)),
                        xp = lowBudget,
                    )
                }
                if (moderateBudgetThreshold == null && xpSpent >= moderateBudget) {
                    val xpOver = xpSpent - moderateBudget
                    val fractionalOvershoot = xpOver.toFloat() / xp.toFloat()
                    moderateBudgetThreshold = BudgetThreshold(
                        row = row,
                        fraction = start + (monsterRowFraction * (1.0F - fractionalOvershoot)),
                        xp = moderateBudget,
                    )
                }
                if (highBudgetThreshold == null && xpSpent >= highBudget) {
                    val xpOver = xpSpent - highBudget
                    val fractionalOvershoot = xpOver.toFloat() / xp.toFloat()
                    highBudgetThreshold = BudgetThreshold(
                        row = row,
                        fraction = start + (monsterRowFraction * (1.0F - fractionalOvershoot)),
                        xp = highBudget,
                    )
                }
                column += 1
                if (column > gridRow.lastIndex) {
                    row += 1
                    column = 0
                }
            }
            // If we've not spent an xp budget then its row is 1 beyond our data for monsters
            val thresholdBeyondXPSpent = BudgetThreshold(
                row = grid.lastIndex + 1, fraction = 1.0F, xp = 0
            )
            if (highBudgetThreshold != null && moderateBudgetThreshold != null && lowBudgetThreshold != null) {
                return MonsterGrid(
                    data = grid,
                    lowBudget = lowBudgetThreshold,
                    moderateBudget = moderateBudgetThreshold,
                    highBudget = highBudgetThreshold,
                )
            } else if (lowBudgetThreshold != null && moderateBudgetThreshold != null) {
                // If only high budget hasn't been met, the fraction is 1.0 as its the only
                // item in the row beyond our monster data.
                return MonsterGrid(
                    data = grid,
                    lowBudget = lowBudgetThreshold,
                    moderateBudget = moderateBudgetThreshold,
                    highBudget = thresholdBeyondXPSpent.copy(xp = highBudget),
                )
            } else if (lowBudgetThreshold != null) {
                // If moderate hasn't been met, then neither has high, so both are items in
                // the row beyond our monster data and the fraction for moderate won't be 1.0
                val unspentModerate = (moderateBudget - xpSpent).toFloat()
                val moderateToHigh = highBudget - moderateBudget
                val total = unspentModerate + moderateToHigh
                return MonsterGrid(
                    data = grid,
                    lowBudget = lowBudgetThreshold,
                    moderateBudget = thresholdBeyondXPSpent.copy(
                        fraction = unspentModerate / total,
                        xp = moderateBudget
                    ),
                    highBudget = thresholdBeyondXPSpent.copy(xp = highBudget),
                )
            } else {
                // If low hasn't been met then neither have moderate or high, so all three are
                // items in the row beyond our monster data and the fractions for low and
                // moderate won't be 1.0
                val unspentLow = (lowBudget - xpSpent).toFloat()
                val lowToModerate = moderateBudget - lowBudget
                val moderateToHigh = highBudget - moderateBudget
                val total = unspentLow + lowToModerate + moderateToHigh
                return MonsterGrid(
                    data = grid,
                    lowBudget = thresholdBeyondXPSpent.copy(
                        fraction = unspentLow / total,
                        xp = moderateBudget
                    ),
                    moderateBudget = thresholdBeyondXPSpent.copy(
                        fraction = (unspentLow + moderateToHigh) / total,
                        xp = moderateBudget
                    ),
                    highBudget = thresholdBeyondXPSpent.copy(xp = highBudget),
                )
            }
        }
    }
}