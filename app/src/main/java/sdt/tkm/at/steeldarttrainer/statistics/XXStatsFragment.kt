package sdt.tkm.at.steeldarttrainer.statistics

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import sdt.tkm.at.steeldarttrainer.R
import sdt.tkm.at.steeldarttrainer.base.DataHolder
import sdt.tkm.at.steeldarttrainer.base.animateDoubleValue
import sdt.tkm.at.steeldarttrainer.base.animateIntegerValue
import sdt.tkm.at.steeldarttrainer.models.XXTraining
import java.math.BigDecimal

/**
 * [Add class description here]
 *
 * Created 22.02.18
 *
 * @author Thomas Krainz-Mischitz (Level1 GmbH)
 * @version %I%, %G%
 */
class XXStatsFragmentFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val hits = view.findViewById<TextView>(R.id.xxHitAvaregaeStats)
        val hitsPercentage = view.findViewById<TextView>(R.id.xxHitPercentageStats)
        val score = view.findViewById<TextView>(R.id.xxScoreStats)

        val single = view.findViewById<TextView>(R.id.singleCountStats)
        val double = view.findViewById<TextView>(R.id.doubleCountStats)
        val triple = view.findViewById<TextView>(R.id.tripleCountStats)

        val statsinfoTextView = view.findViewById<TextView>(R.id.statsInfoTextView)
        val layout = view.findViewById<LinearLayout>(R.id.xxStatsLayout)

        val xxList = DataHolder(this.activity.applicationContext).getXXTrainingsList()

        if (xxList.isEmpty()) {
            layout.visibility = View.GONE
            if (activity is StatisticsActivity) {
                val currentActivity = activity as StatisticsActivity
                currentActivity.shouldShowInfoButton(false)
            }
        } else {
            statsinfoTextView.visibility = View.GONE
            if (activity is StatisticsActivity) {
                val currentActivity = activity as StatisticsActivity
                currentActivity.shouldShowInfoButton(true)
            }


            animateDoubleValue(0.0, hitsText(xxList), hits)
            animateDoubleValue(0.0, hitsPercentageText(xxList), hitsPercentage)
            animateDoubleValue(0.0, scoreText(xxList), score)

            animateIntegerValue(0, singleAmount(xxList), single)
            animateIntegerValue(0, doubleAmount(xxList), double)
            animateIntegerValue(0, tripleAmount(xxList), triple)
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.xx_stats_fragment, container, false)
    }

    private fun hitsText(list: ArrayList<XXTraining>): Double {
        var value = 0.0
        for (item in list) {
            value += item.hits()
        }

        val hits = value / list.size
        return BigDecimal(hits).setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()
    }

    private fun hitsPercentageText(list: ArrayList<XXTraining>): Double {
        var value = 0.0
        for (item in list) {
            value += item.hitPercentage()
        }

        val hitsPercentage = value / list.size
        return BigDecimal(hitsPercentage).setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()
    }

    private fun scoreText(list: ArrayList<XXTraining>): Double {
        var value = 0.0
        for (item in list) {
            value += item.score
        }

        val score = value / list.size
        return BigDecimal(score).setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()
    }

    private fun singleAmount(list: ArrayList<XXTraining>): Int {
        var value = 0
        for (item in list) {
            value += item.single
        }

        return value
    }

    private fun doubleAmount(list: ArrayList<XXTraining>): Int {
        var value = 0
        for (item in list) {
            value += item.double
        }

        return value
    }

    private fun tripleAmount(list: ArrayList<XXTraining>): Int {
        var value = 0
        for (item in list) {
            value += item.triple
        }

        return value
    }
}