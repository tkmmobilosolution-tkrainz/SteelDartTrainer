package sdt.tkm.at.steeldarttrainer.models

/**
 * [Add class description here]
 *
 * Created 21.02.18
 *
 * @author Thomas Krainz-Mischitz (Level1 GmbH)
 * @version %I%, %G%
 */
data class HighscoreTraining(val score: Int,
                             val heighestThreeDart: Int,
                             val sixtyPlus: Int,
                             val hundretPlus: Int,
                             val hundretFourtyPlus: Int,
                             val hundretEighty: Int) {

    fun ppdAvarage(): Double {
        return score.toDouble() / 30
    }

    fun pptdAvarage(): Double {
        return ppdAvarage() * 3
    }
}