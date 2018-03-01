package sdt.tkm.at.steeldarttrainer.base

import android.app.Fragment
import android.widget.TextView
import java.math.BigDecimal

/**
 * [Add class description here]
 *
 * Created 01.03.18
 *
 * @author Thomas Krainz-Mischitz (Level1 GmbH)
 * @version %I%, %G%
 */

fun Fragment.animateDoubleValue(from: Double, to: Double, textView: TextView) {
    val animator = android.animation.ValueAnimator.ofFloat(from.toFloat(), to.toFloat())
    animator.duration = 650
    animator.addUpdateListener { animation ->
        val text = String.format("%.2f", animation.animatedValue)
        textView.text = text
    }
    animator.start()
}

fun Fragment.animateIntegerValue(from: Int, to: Int, textView: TextView) {
    val animator = android.animation.ValueAnimator.ofInt(from, to)
    animator.duration = 650
    animator.addUpdateListener { animation ->
        textView.text = animation.animatedValue.toString() }
    animator.start()
}