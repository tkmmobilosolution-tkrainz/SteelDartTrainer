package sdt.tkm.at.steeldarttrainer.base

import android.animation.ValueAnimator
import android.support.v7.app.AppCompatActivity
import android.widget.TextView

/**
 * [Add class description here]
 *
 * Created 01.03.18
 *
 * @author Thomas Krainz-Mischitz (Level1 GmbH)
 * @version %I%, %G%
 */

fun AppCompatActivity.animateValue(from: Int, to: Int, textView: TextView) {
    val animator = ValueAnimator.ofInt(from, to)
    animator.duration = 650
    animator.addUpdateListener { animation -> textView.text = animation.animatedValue.toString() }
    animator.start()
}