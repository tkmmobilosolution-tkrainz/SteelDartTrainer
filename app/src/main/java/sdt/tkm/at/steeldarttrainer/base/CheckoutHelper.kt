package sdt.tkm.at.steeldarttrainer.base

class CheckoutHelper {

    val threeDartFinishes = hashMapOf<Int, String>(
            170 to "T20 T20 D-Bull",
            167 to "T20 T19 D-Bull",
            164 to "T20 T18 D-Bull",
            161 to "T20 T17 D-Bull",
            160 to "T20 T20 D20",
            158 to "T20 T20 D19",
            157 to "T20 T19 D20",
            156 to "T20 T20 D18",
            155 to "T20 T19 D19",
            154 to "T20 T18 D20",
            153 to "T20 T19 D18",
            152 to "T20 T20 D16",
            151 to "T20 T17 D20",
            150 to "T20 T18 D18",
            149 to "T20 T19 D16",
            148 to "T20 T16 D20",
            147 to "T20 T17 D18",
            146 to "T20 T18 D16",
            145 to "TT20 T15 D20",
            144 to "T20 T20 D12",
            143 to "T20 T17 D16",
            142 to "T20 T14 D20",
            141 to "T20 T15 D18",
            140 to "T20 T20 D10",
            139 to "T20 T19 D11",
            138 to "T20 T18 D12",
            137 to "T20 T19 D10",
            136 to "T20 T20 D8",
            135 to "D-Bull T15 D20",
            134 to "T20 T14 D16",
            133 to "T20 T19 D8",
            132 to "D-Bull T14 D20",
            131 to "T20 T13 D16",
            130 to "T20 20 D-Bull",
            129 to "T19 T16 D12",
            128 to "T18 T18 D10",
            127 to "T20 T17 D8",
            126 to "T19 S19 D-Bull",
            125 to "D-Bull S-Bull D-Bull",
            124 to "T20 T14 D11",
            123 to "T19 T16 D9",
            122 to "T18 T18 D7",
            121 to "T20 T11 D14",
            120 to "T20 S20 D20",
            119 to "T19 T12 D13",
            118 to "T20 S18 D20",
            117 to "T20 S17 D20",
            116 to "T20 S16 D20",
            115 to "T20 S15 D20",
            114 to "T20 S14 D20",
            113 to "T20 S13 D20",
            112 to "T20 S12 D20",
            111 to "T20 S11 D20",
            110 to "T20 S10 D20",
            109 to "T20 S9 D20",
            108 to "T20 S8 D20",
            107 to "T20 S7 D20",
            106 to "T20 S6 D20",
            105 to "T20 S5 D20",
            104 to "T20 S4 D20",
            103 to "T20 S3 D20",
            102 to "T20 S2 D20",
            101 to "T20 S1 D20",
            100 to "T20 D20",
            99 to "T19 S1 D16")

    val twoDartFinishes = hashMapOf<Int, String>(
            110 to "T20 D-Bull",
            107 to "T19 D-Bull",
            104 to "T18 D-Bull",
            101 to "T17 D-Bull",
            100 to "T20 D20",
            98 to "T20 D19",
            97 to "T19 D20",
            96 to "T20 D18",
            95 to "T19 D19",
            94 to "T18 D20",
            93 to "T19 D18",
            92 to "T20 D16",
            91 to "T17 D20",
            90 to "T18 D18",
            89 to "T19 D16",
            88 to "T20 D14",
            87 to "T17 D18",
            86 to "T18 D16",
            85 to "T15 D20",
            84 to "T20 D12",
            83 to "T17 D16",
            82 to "T14 D20",
            81 to "T15 D18",
            80 to "T20 D10",
            79 to "T19 D11",
            78 to "T18 D12",
            77 to "T19 D10",
            76 to "T20 D8",
            75 to "T17 D12",
            74 to "T14 D16",
            73 to "T19 D8",
            72 to "T16 D12",
            71 to "T13 D16",
            70 to "T20 D5",
            69 to "T19 D6",
            68 to "T18 D7",
            67 to "T17 D8",
            66 to "T16 D9",
            65 to "T15 D10",
            64 to "T14 D11",
            63 to "T13 D12",
            62 to "T12 D13",
            61 to "T11 D14",
            60 to "S20 D20",
            59 to "S19 D20",
            58 to "S18 D20",
            57 to "S17 D20",
            56 to "S16 D20",
            55 to "S15 D20",
            54 to "S14 D20",
            53 to "S13 D20",
            52 to "S12 D20",
            51 to "S11 D20",
            50 to "S10 D20",
            49 to "S9 D20",
            48 to "S8 D20",
            47 to "S7 D20",
            46 to "S6 D20",
            45 to "S5 D20",
            44 to "S4 D20",
            43 to "S3 D20",
            42 to "S2 D20",
            41 to "S1 D20",
            39 to "S7 D16",
            37 to "S5 D16",
            35 to "S3 D16",
            33 to "S1 D16",
            31 to "S15 D8",
            29 to "S13 D8",
            27 to "S11 D8",
            25 to "S9 D8",
            23 to "S7 D8",
            21 to "S5 D8",
            19 to "S3 D8",
            17 to "S1 D8",
            15 to "S7 D4",
            13 to "S5 D4",
            11 to "S3 D4",
            9 to "S1 D4",
            7 to "S3 D2",
            5 to "S1 D2",
            3 to "S1 D1"
            )





    fun recommendedCheckoutThreeDarts(pointsToFinish: Int): String? {

        if (!checkThreeBogey(pointsToFinish)) {
            return null
        }

        if (pointsToFinish > 170) {
            return null
        }

        when (pointsToFinish) {
            in 99..170 -> {
                return threeDartFinishes[pointsToFinish]
            }
            in 41..98 -> {
                return twoDartFinishes[pointsToFinish]
            }
            in 2..40 -> {
                return if (pointsToFinish % 2 == 0) {
                    "D"+ (pointsToFinish / 2)
                } else {
                    twoDartFinishes[pointsToFinish]
                }
            }
        }

        return null
    }

    fun recommendedCheckoutTwoDarts(pointsToFinish: Int): String? {
        if (!checkTwoBogey(pointsToFinish)) {
            return null
        }
        if (pointsToFinish > 110) {
            return null
        }

        when (pointsToFinish) {
            in 41..110 -> {
                return twoDartFinishes[pointsToFinish]
            }
            in 2..40 -> {
                return if (pointsToFinish % 2 == 0) {
                    "D"+ (pointsToFinish / 2)
                } else {
                    twoDartFinishes[pointsToFinish]
                }
            }
        }

        return null
    }

    private fun checkThreeBogey(points: Int): Boolean {
        when (points) {
            169, 168, 166, 165, 163, 162, 159 -> {
                return true
            }
        }
        return true
    }

    private fun checkTwoBogey(points: Int): Boolean {
        when (points) {
            109, 108, 106, 105, 103, 102, 99 -> {
                return true
            }
        }
        return true
    }
}