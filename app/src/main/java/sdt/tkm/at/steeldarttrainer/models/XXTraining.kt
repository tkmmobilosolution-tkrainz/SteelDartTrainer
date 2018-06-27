package sdt.tkm.at.steeldarttrainer.models

/**
 * [Add class description here]
 *
 * Created 22.02.18
 *
 * @author Thomas Krainz-Mischitz (Level1 GmbH)
 * @version %I%, %G%
 */
data class XXTraining(val value: Int,
                      val score: Int,
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