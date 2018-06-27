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
  private lateinit var graph: GraphView
  private lateinit var hits: TextView
  private lateinit var hitsPercentage: TextView
  private lateinit var score: TextView
  private lateinit var single: TextView
  private lateinit var double: TextView
  private lateinit var triple: TextView
  private lateinit var statsinfoTextView: TextView
  private lateinit var layout: LinearLayout
  private lateinit var xxList: ArrayList<XXTraining>

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    xxList = DataHolder(this.activity.applicationContext).getXXTrainingsList()

    if (xxList.isEmpty()) {
      layout.visibility = View.GONE
      /** if (activity is StatisticsActivity) {
      val currentActivity = activity as StatisticsActivity
      currentActivity.shouldShowInfoButton(false)
      }*/
    } else {
      statsinfoTextView.visibility = View.GONE
      /**if (activity is StatisticsActivity) {
      val currentActivity = activity as StatisticsActivity
      currentActivity.shouldShowInfoButton(true)
      }*/
    }

    super.onViewCreated(view, savedInstanceState)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View {
    val view = inflater.inflate(R.layout.xx_stats_fragment, container, false)
    hits = view.findViewById<TextView>(R.id.xxHitAvaregaeStats)
    hitsPercentage = view.findViewById<TextView>(R.id.xxHitPercentageStats)
    score = view.findViewById<TextView>(R.id.xxScoreStats)

    single = view.findViewById<TextView>(R.id.singleCountStats)
    double = view.findViewById<TextView>(R.id.doubleCountStats)
    triple = view.findViewById<TextView>(R.id.tripleCountStats)

    statsinfoTextView = view.findViewById<TextView>(R.id.statsInfoTextView)
    layout = view.findViewById<LinearLayout>(R.id.xxStatsLayout)
    return view
  }

  override fun onResume() {
    super.onResume()

    if (!xxList.isEmpty()) {
      animateDoubleValue(0.0, hitsText(xxList), hits)
      animateDoubleValue(0.0, hitsPercentageText(xxList), hitsPercentage)
      animateDoubleValue(0.0, scoreText(xxList), score)

      animateIntegerValue(0, singleAmount(xxList), single)
      animateIntegerValue(0, doubleAmount(xxList), double)
      animateIntegerValue(0, tripleAmount(xxList), triple)

      if (xxList.size > 1) {
        var hitsArray = ArrayList<Double>()
        for (item in xxList) {
          hitsArray.add(item.hits().toDouble())
        }
        val minPPD = getMinValue(hitsArray)
        val maxPPD = getMaxValue(hitsArray)

        graph = view.findViewById(R.id.xx_graph) as GraphView
        drawGraph(minPPD, maxPPD, dataPointsHits(xxList), getString(R.string.statistics_graph_hits))
      }
    }
  }

  private fun drawGraph(minY: Double, maxY: Double, dataPoints: Array<DataPoint>, title: String) {
    graph.title = title
    graph.titleTextSize = 40F
    graph.titleColor = Color.WHITE
    graph.gridLabelRenderer.gridColor = Color.WHITE
    graph.gridLabelRenderer.horizontalLabelsColor = Color.WHITE
    graph.gridLabelRenderer.verticalLabelsColor = Color.WHITE

    graph.viewport.setMinY(0.0)
    graph.viewport.setMaxY(maxY + 2.0)
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

  private fun dataPointsHits(list: ArrayList<XXTraining>): Array<DataPoint> {
    var hits = 0.0
    var datapoints = ArrayList<DataPoint>()
    for (index in 0 until list.size) {
      hits = list.get(index).hits().toDouble()
      val value = BigDecimal(hits).setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()
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