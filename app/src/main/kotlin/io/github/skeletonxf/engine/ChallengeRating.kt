package io.github.skeletonxf.engine

enum class ChallengeRating(val number: String) {
    Zero("0"),
    Eighth("⅛"),
    Quarter("¼"),
    Half("½"),
    One("1"),
    Two("2"),
    Three("3"),
    Four("4"),
    Five("5"),
    Six("6"),
    Seven("7"),
    Eight("8"),
    Nine("9"),
    Ten("10"),
    Eleven("11"),
    Twelve("12"),
    Thirteen("13"),
    Fourteen("14"),
    Fifteen("15"),
    Sixteen("16"),
    Seventeen("17"),
    Eighteen("18"),
    Nineteen("19"),
    Twenty("20"),
    TwentyOne("21"),
    TwentyTwo("22"),
    TwentyThree("23"),
    TwentyFour("24"),
    TwentyFive("25"),
    TwentySix("26"),
    TwentySeven("27"),
    TwentyEight("28"),
    TwentyNine("29"),
    Thirty("30"),
    ;

    /**
     * This is the regular XP for a given CR, excluding lair bonuses.
     */
    fun xp(): Int = when (this) {
        Zero -> 10
        Eighth -> 25
        Quarter -> 50
        Half -> 100
        One -> 200
        Two -> 450
        Three -> 700
        Four -> 1100
        Five -> 1800
        Six -> 2300
        Seven -> 2900
        Eight -> 3900
        Nine -> 5000
        Ten -> 5900
        Eleven -> 7200
        Twelve -> 8400
        Thirteen -> 10000
        Fourteen -> 11500
        Fifteen -> 13000
        Sixteen -> 15000
        Seventeen -> 18000
        Eighteen -> 20000
        Nineteen -> 22000
        Twenty -> 25000
        TwentyOne -> 33000
        TwentyTwo -> 41000
        TwentyThree -> 50000
        TwentyFour -> 62000
        TwentyFive -> 75000
        TwentySix -> 90000
        TwentySeven -> 105000
        TwentyEight -> 120000
        TwentyNine -> 135000
        Thirty -> 155000
    }
}