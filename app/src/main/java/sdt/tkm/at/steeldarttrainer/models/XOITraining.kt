package sdt.tkm.at.steeldarttrainer.models

import java.util.Date

/**
 * [Add class description here]
 *
 * Created 22.01.18
 *
 * @author Thomas Krainz-Mischitz (Level1 GmbH)
 * @version %I%, %G%
 */

data class XOITraining(
    val xoi: Int,
    val dartAmount: Int,
    val heighestThreeDart: Int,
    val checkoutAmount: Int,
    val sixtyPlus: Int,
    val hundretPlus: Int,
    val hundretFourtyPlus: Int,
    val hundretEighty: Int) {

    fun ppdAvarage(): Double {
        return xoi.toDouble() / dartAmount.toDouble()
    }

    fun pptdAvarage(): Double {
        return ppdAvarage() * 3
    }

    fun checkoutRate(): Double {
        return 100 / checkoutAmount.toDouble()
    }
}