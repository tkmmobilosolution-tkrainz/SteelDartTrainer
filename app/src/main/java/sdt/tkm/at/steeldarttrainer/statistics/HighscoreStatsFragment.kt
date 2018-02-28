package sdt.tkm.at.steeldarttrainer.statistics

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import sdt.tkm.at.steeldarttrainer.base.DataHolder
import sdt.tkm.at.steeldarttrainer.models.HighscoreTraining
import sdt.tkm.at.steeldarttrainer.R
import java.math.BigDecimal

/**
 * [Add class description here]
 *
 * Created 21.02.18
 *
 * @author Thomas Krainz-Mischitz (Level1 GmbH)
 * @version %I%, %G%
 */
class HighscoreStatsFragment: Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val ppd = view.findViewById<TextView>(R.id.ppdAmount)
        val pptd = view.findViewById<TextView>(R.id.pptdAmount)
        val cor = view.findViewById<TextView>(R.id.checkoutAmount)
        val corText = view.findViewById<TextView>(R.id.corText)
        corText.text = "Avg. Score"
        val darts = view.findViewById<TextView>(R.id.darts)

        val sixtyPlus = view.findViewById<TextView>(R.id.sixtyPlus)
        val hundretPlus = view.findViewById<TextView>(R.id.hundretPlus)
        val hundretFourtyPlus = view.findViewById<TextView>(R.id.hundretFourtyPlus)
        val hundretEighty = view.findViewById<TextView>(R.id.hundretEighty)

        val statsinfoTextView = view.findViewById<TextView>(R.id.xoiStatsInfoTextView)
        val layout = view.findViewById<LinearLayout>(R.id.xoiStatsLayout)

        val hsTrainingsList = DataHolder(this.activity.applicationContext).getHighscoreTrainingsList()

        if (hsTrainingsList.isEmpty()) {
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

            ppd.text = averagePPD(hsTrainingsList).toString()
            pptd.text = averagePPTD(hsTrainingsList).toString()
            cor.text = averageHS(hsTrainingsList).toString()
            darts.text = countDarts(hsTrainingsList).toString()
            sixtyPlus.text = countSixties(hsTrainingsList).toString()
            hundretPlus.text = countHundretPlus(hsTrainingsList).toString()
            hundretFourtyPlus.text = countHundretFourtyPlus(hsTrainingsList).toString()
            hundretEighty.text = countHundretEighty(hsTrainingsList).toString()
        }

        super.onViewCreated(view, savedInstanceState)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View {

        val view = inflater.inflate(R.layout.xoi_stats_fragment, container, false)
        return view
    }

    private fun averagePPD(list: ArrayList<HighscoreTraining>): Double {
        var ppd = 0.0
        for (item in list) {
            ppd += item.ppdAvarage()
        }

        val value = ppd / list.size
        return BigDecimal(value).setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()
    }

    private fun averagePPTD(list: ArrayList<HighscoreTraining>): Double {
        var pptd = 0.0
        for (item in list) {
            pptd += item.pptdAvarage()
        }

        val value = pptd / list.size
        return BigDecimal(value).setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()
    }

    private fun averageHS(list: ArrayList<HighscoreTraining>): Double {
        var score = 0.0
        for (item in list) {
            score += item.score
        }

        val value = score / list.size
        return BigDecimal(value).setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()
    }

    private fun countSixties(list: ArrayList<HighscoreTraining>): Int {
        var value = 0
        for (item in list) {
            value += item.sixtyPlus
        }
        return value
    }

    private fun countHundretPlus(list: ArrayList<HighscoreTraining>): Int {
        var value = 0
        for (item in list) {
            value += item.hundretPlus
        }
        return value
    }

    private fun countHundretFourtyPlus(list: ArrayList<HighscoreTraining>): Int {
        var value = 0
        for (item in list) {
            value += item.hundretFourtyPlus
        }
        return value
    }

    private fun countHundretEighty(list: ArrayList<HighscoreTraining>): Int {
        var value = 0
        for (item in list) {
            value += item.hundretEighty
        }
        return value
    }

    private fun countDarts(list: ArrayList<HighscoreTraining>): Int {
        return 30 * list.size
    }
}