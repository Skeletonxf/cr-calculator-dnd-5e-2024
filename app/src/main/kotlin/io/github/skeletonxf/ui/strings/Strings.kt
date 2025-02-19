package io.github.skeletonxf.ui.strings

import androidx.compose.runtime.staticCompositionLocalOf

val LocalStrings = staticCompositionLocalOf { English.strings }

data class Strings(
    val calculator: Calculator,
    val calculatorComponents: CalculatorComponents,
)

data class CalculatorComponents(
    val players: String,
    val monsters: String,
    val lowDifficulty: String,
    val moderateDifficulty: String,
    val highDifficulty: String,
    val pastHighDifficulty: String,
    val xpFraction: (xp: Int, budget: Int) -> String,
    val xpValue: (xp: Int) -> String,
    val noXP: String = xpValue(0),
    val xpBudget: String,
    val twoLineLowXP: (xp: Int) -> String = { xp -> "$lowDifficulty\n${xpValue(xp)}" },
    val twoLineModerateXP: (xp: Int) -> String = { xp -> "$moderateDifficulty\n${xpValue(xp)}" },
    val twoLineHighXP: (xp: Int) -> String = { xp -> "$highDifficulty\n${xpValue(xp)}" },
    val unspent: String,
    val challengeRating: String,
    val quantity: String,
    val level: String,
    val addContentDescription: String,
    val removeContentDescription: String,
)

data class Calculator(
    val title: String,
)

object English {
    private val xpValue = { xp: Int -> "$xp XP" }

    val strings: Strings = Strings(
        calculator = Calculator(title = "CR Calculator"),
        calculatorComponents = CalculatorComponents(
            players = "Players",
            monsters = "Monsters",
            lowDifficulty = "Low",
            moderateDifficulty = "Moderate",
            highDifficulty = "High",
            pastHighDifficulty = "TPK?",
            xpFraction = { xp, budget -> "${xpValue(xp)} / ${xpValue(budget)}" },
            xpValue = xpValue,
            xpBudget = "XP Budget",
            unspent = "Unspent",
            challengeRating = "CR",
            quantity = "Quantity",
            level = "Level",
            addContentDescription = "Add",
            removeContentDescription = "Remove",
        )
    )
}
