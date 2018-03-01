package sdt.tkm.at.steeldarttrainer.statistics

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import sdt.tkm.at.steeldarttrainer.base.DataHolder
import sdt.tkm.at.steeldarttrainer.R
import sdt.tkm.at.steeldarttrainer.base.animateDoubleValue
import sdt.tkm.at.steeldarttrainer.base.animateIntegerValue
import sdt.tkm.at.steeldarttrainer.models.XOITraining
import java.math.BigDecimal

/**
 * [Add class description here]
 *
 * Created 21.02.18
 *
 * @author Thomas Krainz-Mischitz (Level1 GmbH)
 * @version %I%, %G%
 */
class XOIStatsFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val ppd = view.findViewById<TextView>(R.id.ppdAmount)
        val pptd = view.findViewById<TextView>(R.id.pptdAmount)
        val cor = view.findViewById<TextView>(R.id.checkoutAmount)
        val darts = view.findViewById<TextView>(R.id.darts)

        val sixtyPlus = view.findViewById<TextView>(R.id.sixtyPlus)
        val hundretPlus = view.findViewById<TextView>(R.id.hundretPlus)
        val hundretFourtyPlus = view.findViewById<TextView>(R.id.hundretFourtyPlus)
        val hundretEighty = view.findViewById<TextView>(R.id.hundretEighty)

        val statsinfoTextView = view.findViewById<TextView>(R.id.xoiStatsInfoTextView)
        val layout = view.findViewById<LinearLayout>(R.id.xoiStatsLayout)

        val xoiTrainingsList = DataHolder(this.activity.applicationContext).getXOITrainingsList()

        if (xoiTrainingsList.isEmpty()) {
            layout.visibility = View.GONE

            if (activity is StatisticsActivity) {
                val currentActivity = activity as StatisticsActivity
                currentActivity.shouldShowInfoButton(false)
            }

        } else {

            if (activity is StatisticsActivity) {
                val currentActivity = activity as StatisticsActivity
                currentActivity.shouldShowInfoButton(true)
            }
            statsinfoTextView.visibility = View.GONE


            animateDoubleValue(0.0, averagePPD(xoiTrainingsList), ppd)
            animateDoubleValue(0.0, averagePPTD(xoiTrainingsList), pptd)
            animateDoubleValue(0.0, avareageCOR(xoiTrainingsList), cor)
            animateIntegerValue(0, countDarts(xoiTrainingsList), darts)

            animateIntegerValue(0, countSixties(xoiTrainingsList), sixtyPlus)
            animateIntegerValue(0, countHundretPlus(xoiTrainingsList), hundretPlus)
            animateIntegerValue(0, countHundretFourtyPlus(xoiTrainingsList), hundretFourtyPlus)
            animateIntegerValue(0, countHundretEighty(xoiTrainingsList), hundretEighty)
        }

        super.onViewCreated(view, savedInstanceState)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View {

        val view = inflater.inflate(R.layout.xoi_stats_fragment, container, false)
        return view
    }

    private fun averagePPD(list: ArrayList<XOITraining>): Double {
        var ppd = 0.0
        for (item in list) {
            ppd += item.ppdAvarage()
        }

        val value = ppd / list.size
        return BigDecimal(value).setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()
    }

    private fun averagePPTD(list: ArrayList<XOITraining>): Double {
        var pptd = 0.0
        for (item in list) {
            pptd += item.pptdAvarage()
        }

        val value = pptd / list.size
        return BigDecimal(value).setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()
    }

    private fun avareageCOR(list: ArrayList<XOITraining>): Double {
        var cor = 0.0
        for (item in list) {
            cor += item.checkoutRate()
        }

        val value = cor / list.size
        return BigDecimal(value).setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()
    }

    private fun countSixties(list: ArrayList<XOITraining>): Int {
        var value = 0
        for (item in list) {
            value += item.sixtyPlus
        }
        return value
    }

    private fun countHundretPlus(list: ArrayList<XOITraining>): Int {
        var value = 0
        for (item in list) {
            value += item.hundretPlus
        }
        return value
    }

    private fun countHundretFourtyPlus(list: ArrayList<XOITraining>): Int {
        var value = 0
        for (item in list) {
            value += item.hundretFourtyPlus
        }
        return value
    }

    private fun countHundretEighty(list: ArrayList<XOITraining>): Int {
        var value = 0
        for (item in list) {
            value += item.hundretEighty
        }
        return value
    }

    private fun countDarts(list: ArrayList<XOITraining>): Int {
        var value = 0
        for (item in list) {
            value += item.dartAmount
        }
        return value
    }
}
