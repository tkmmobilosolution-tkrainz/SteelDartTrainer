package sdt.tkm.at.steeldarttrainer.models

/**
 * [Add class description here]
 *
 * Created 07.03.18
 *
 * @author Thomas Krainz-Mischitz (Level1 GmbH)
 * @version %I%, %G%
 */
class AroundTheClockTraining(val darts: Int) {

    fun hitRate(): Double {
        val rate: Double = (21 / darts).toDouble()
        return rate * 100.0
    }
}