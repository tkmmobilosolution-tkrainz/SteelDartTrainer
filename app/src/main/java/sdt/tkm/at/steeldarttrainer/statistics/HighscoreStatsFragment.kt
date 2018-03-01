package sdt.tkm.at.steeldarttrainer.statistics

import android.app.Fragment
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import sdt.tkm.at.steeldarttrainer.R
import sdt.tkm.at.steeldarttrainer.base.DataHolder
import sdt.tkm.at.steeldarttrainer.base.animateDoubleValue
import sdt.tkm.at.steeldarttrainer.base.animateIntegerValue
import sdt.tkm.at.steeldarttrainer.models.HighscoreTraining
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

    private lateinit var graph: GraphView
    private lateinit var ppd: TextView
    private lateinit var pptd: TextView
    private lateinit var cor: TextView
    private lateinit var corText: TextView
    private lateinit var darts: TextView
    private lateinit var sixtyPlus: TextView
    private lateinit var hundretPlus: TextView
    private lateinit var hundretFourtyPlus: TextView
    private lateinit var hundretEighty: TextView

    private lateinit var statsinfoTextView: TextView
    private lateinit var layout: LinearLayout

    private lateinit var hsTrainingsList: ArrayList<HighscoreTraining>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
         hsTrainingsList = DataHolder(this.activity.applicationContext).getHighscoreTrainingsList()

        if (hsTrainingsList.isEmpty()) {
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
        }
        super.onViewCreated(view, savedInstanceState)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View {

        val view = inflater.inflate(R.layout.xoi_stats_fragment, container, false)
        ppd = view.findViewById<TextView>(R.id.ppdAmount)
        pptd = view.findViewById<TextView>(R.id.pptdAmount)
        cor = view.findViewById<TextView>(R.id.checkoutAmount)
        corText = view.findViewById<TextView>(R.id.corText)
        corText.text = "Avg. Score"
        darts = view.findViewById<TextView>(R.id.darts)

        sixtyPlus = view.findViewById<TextView>(R.id.sixtyPlus)
        hundretPlus = view.findViewById<TextView>(R.id.hundretPlus)
        hundretFourtyPlus = view.findViewById<TextView>(R.id.hundretFourtyPlus)
        hundretEighty = view.findViewById<TextView>(R.id.hundretEighty)

        statsinfoTextView = view.findViewById<TextView>(R.id.xoiStatsInfoTextView)
        layout = view.findViewById<LinearLayout>(R.id.xoiStatsLayout)
        return view
    }

    override fun onResume() {
        super.onResume()

        if (!hsTrainingsList.isEmpty()) {
            animateDoubleValue(0.0, averagePPD(hsTrainingsList), ppd)
            animateDoubleValue(0.0, averagePPTD(hsTrainingsList), pptd)
            animateDoubleValue(0.0, averageHS(hsTrainingsList), cor)
            animateIntegerValue(0, countDarts(hsTrainingsList), darts)

            animateIntegerValue(0, countSixties(hsTrainingsList), sixtyPlus)
            animateIntegerValue(0, countHundretPlus(hsTrainingsList), hundretPlus)
            animateIntegerValue(0, countHundretFourtyPlus(hsTrainingsList), hundretFourtyPlus)
            animateIntegerValue(0, countHundretEighty(hsTrainingsList), hundretEighty)

            if (hsTrainingsList.size > 1) {
                var ppds = ArrayList<Double>()
                for (item in hsTrainingsList) {
                    ppds.add(item.ppdAvarage())
                }

                val minPPD = getMinValue(ppds)
                val maxPPD = getMaxValue(ppds)

                graph = view.findViewById(R.id.xoi_graph) as GraphView
                drawGraph(minPPD, maxPPD, dataPointsPPD(hsTrainingsList), getString(R.string.statistics_graph_ppd))
            }
        }
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
    private fun dataPointsPPD(list: ArrayList<HighscoreTraining>): Array<DataPoint> {

        var ppd = 0.0
        var datapoints = ArrayList<DataPoint>()
        for (index in 0 until list.size) {
            ppd = list.get(index).ppdAvarage()
            val value = BigDecimal(ppd).setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()

            val dataPoint = DataPoint(index.toDouble() + 1, value)
            datapoints.add(dataPoint)
        }

        return toArray(datapoints)
    }

    private fun getMaxValue(list: ArrayList<Double>): Double {
        var maxValue = 0.0
        for (value in list) {
            if (value > maxValue) {
                maxValue = value
            }
        }

        return maxValue
    }

    private fun getMinValue(list: ArrayList<Double>): Double {
        var minValue = 61.0
        for (value in list) {
            if (value < minValue) {
                minValue = value
            }
        }

        return minValue
    }

    inline fun <reified T> toArray(list: List<*>): Array<T> {
        return (list as List<T>).toTypedArray()
    }

    private fun drawGraph(minY: Double, maxY: Double, dataPoints: Array<DataPoint>, title: String) {
        graph.title = title
        graph.titleTextSize = 40F
        graph.titleColor = Color.WHITE
        graph.gridLabelRenderer.gridColor = Color.WHITE
        graph.gridLabelRenderer.horizontalLabelsColor = Color.WHITE
        graph.gridLabelRenderer.verticalLabelsColor = Color.WHITE

        graph.viewport.setMinY(minY - 5.0)
        graph.viewport.setMaxY(maxY + 5.0)
        graph.viewport.isYAxisBoundsManual = true
        graph.viewport.setMinX(1.0)
        graph.viewport.setMaxX(dataPoints.size.toDouble())
        graph.viewport.isXAxisBoundsManual = true
        graph.gridLabelRenderer.numHorizontalLabels = if (dataPoints.size <= 6) dataPoints.size else 6

        val series = LineGraphSeries<DataPoint>(dataPoints)
        series.setColor(Color.WHITE)
        series.setDrawDataPoints(true)
        series.setDataPointsRadius(8F)
        series.isDrawBackground = true
        series.setThickness(6)
        graph.addSeries(series)
    }
}