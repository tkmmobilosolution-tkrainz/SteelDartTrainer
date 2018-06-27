package sdt.tkm.at.steeldarttrainer.models

/**
 * [Add class description here]
 *
 * Created 07.03.18
 *
 * @author Thomas Krainz-Mischitz (Level1 GmbH)
 * @version %I%, %G%
 */
class RandomTraining(val score: Int,
                     val single: Int,
                     val double: Int,
                     val triple: Int) {
  fun hits(): Int {
    return single + double + triple
  }

  fun hitPercentage(): Double {
    return hits().toDouble() / 99
  }
}