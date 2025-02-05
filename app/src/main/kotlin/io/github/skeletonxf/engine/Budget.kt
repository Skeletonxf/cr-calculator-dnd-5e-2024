package io.github.skeletonxf.engine

object Budget {
    enum class Type {
        Low, Moderate, High
    }

    fun get(level: Int, type: Type): Int = when (type) {
        Type.Low -> playerLowXPBudget.getOrDefault(level, 0)
        Type.Moderate -> playerModerateXPBudget.getOrDefault(level, 0)
        Type.High -> playerHighXPBudget.getOrDefault(level, 0)
    }

    private val playerLowXPBudget = mapOf(
        1 to 50,
        2 to 100,
        3 to 150,
        4 to 250,
        5 to 500,
        6 to 600,
        7 to 750,
        8 to 1000,
        9 to 1300,
        10 to 1600,
        11 to 1900,
        12 to 2200,
        13 to 2600,
        14 to 2900,
        15 to 3300,
        16 to 3800,
        17 to 4500,
        18 to 5000,
        19 to 5500,
        20 to 6400,
    )

    private val playerModerateXPBudget = mapOf(
        1 to 75,
        2 to 150,
        3 to 225,
        4 to 375,
        5 to 750,
        6 to 1000,
        7 to 1300,
        8 to 1700,
        9 to 2000,
        10 to 2300,
        11 to 2900,
        12 to 3700,
        13 to 4200,
        14 to 4900,
        15 to 5400,
        16 to 6100,
        17 to 7200,
        18 to 8700,
        19 to 10700,
        20 to 13200,
    )

    private val playerHighXPBudget = mapOf(
        1 to 100,
        2 to 200,
        3 to 400,
        4 to 500,
        5 to 1100,
        6 to 1400,
        7 to 1700,
        8 to 2100,
        9 to 2600,
        10 to 3100,
        11 to 4100,
        12 to 4700,
        13 to 5400,
        14 to 6200,
        15 to 7800,
        16 to 9800,
        17 to 11700,
        18 to 14200,
        19 to 17200,
        20 to 22000,
    )
}