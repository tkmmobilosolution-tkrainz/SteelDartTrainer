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
  private lateinit var graph: GraphView
  private lateinit var ppd: TextView
  private lateinit var pptd: TextView
  private lateinit var cor: TextView
  private lateinit var darts: TextView
  private lateinit var sixtyPlus: TextView
  private lateinit var hundretPlus: TextView
  private lateinit var hundretFourtyPlus: TextView
  private lateinit var hundretEighty: TextView
  private lateinit var statsinfoTextView: TextView
  private lateinit var layout: LinearLayout
  private lateinit var xoiTrainingsList: ArrayList<XOITraining>

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    xoiTrainingsList = DataHolder(this.activity.applicationContext).getXOITrainingsList()


    if (xoiTrainingsList.isEmpty()) {
      layout.visibility = View.GONE
      /** if (activity is StatisticsFragment) {
      val currentActivity = activity as StatisticsFragment
      currentActivity.shouldShowInfoButton(false)
      }*/
    } else {
      statsinfoTextView.visibility = View.GONE
      /**if (activity is StatisticsFragment) {
      val currentActivity = activity as StatisticsFragment
      currentActivity.shouldShowInfoButton(true)
      }*/
    }

    super.onViewCreated(view, savedInstanceState)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View {
    val view = inflater.inflate(R.layout.xoi_stats_fragment, container, false)
    ppd = view.findViewById<TextView>(R.id.ppdAmount)
    pptd = view.findViewById<TextView>(R.id.pptdAmount)
    cor = view.findViewById<TextView>(R.id.checkoutAmount)
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

    if (!xoiTrainingsList.isEmpty()) {
      animateDoubleValue(0.0, averagePPD(xoiTrainingsList), ppd)
      animateDoubleValue(0.0, averagePPTD(xoiTrainingsList), pptd)
      animateDoubleValue(0.0, avareageCOR(xoiTrainingsList), cor)
      animateIntegerValue(0, countDarts(xoiTrainingsList), darts)

      animateIntegerValue(0, countSixties(xoiTrainingsList), sixtyPlus)
      animateIntegerValue(0, countHundretPlus(xoiTrainingsList), hundretPlus)
      animateIntegerValue(0, countHundretFourtyPlus(xoiTrainingsList), hundretFourtyPlus)
      animateIntegerValue(0, countHundretEighty(xoiTrainingsList), hundretEighty)

      if (xoiTrainingsList.size > 1) {
        var ppds = ArrayList<Double>()
        for (item in xoiTrainingsList) {
          ppds.add(item.ppdAvarage())
        }
        val minPPD = getMinValue(ppds)
        val maxPPD = getMaxValue(ppds)

        graph = view.findViewById(R.id.xoi_graph) as GraphView
        drawGraph(minPPD, maxPPD, dataPointsPPD(xoiTrainingsList), getString(R.string.statistics_graph_ppd))
      }
    }
  }

  private fun drawGraph(minY: Double, maxY: Double, dataPoints: Array<DataPoint>, title: String) {

    val portrait = resources.getBoolean(R.bool.orientation_portrait)
    graph.title = title

    val textSize = if (portrait) 40F else 30F
    graph.titleTextSize = textSize
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

    val dataPointRadius = if (resources.getBoolean(R.bool.orientation_portrait)) 8F else 4F
    series.dataPointsRadius = dataPointRadius
    series.isDrawBackground = true
    series.thickness = if (portrait) 6 else 3
    graph.addSeries(series)
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

  private fun dataPointsPPD(list: ArrayList<XOITraining>): Array<DataPoint> {
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
}
