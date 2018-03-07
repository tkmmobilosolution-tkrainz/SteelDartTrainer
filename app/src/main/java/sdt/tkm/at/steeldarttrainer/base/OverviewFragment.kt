package sdt.tkm.at.steeldarttrainer.base

import android.app.Fragment
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextSwitcher
import android.widget.TextView
import android.widget.ViewSwitcher
import sdt.tkm.at.steeldarttrainer.R

/**
 * [Add class description here]
 *
 * Created 05.03.18
 *
 * @author Thomas Krainz-Mischitz (Level1 GmbH)
 * @version %I%, %G%
 */
class OverviewFragment : Fragment() {

    private lateinit var textSwitcher: TextSwitcher
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private var currentIndex = 0
    private var textShowCount = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.overview_fragment, container, false)
        textSwitcher = view.findViewById(R.id.textSwitcher)
        textSwitcher.setFactory(object : ViewSwitcher.ViewFactory {
            override fun makeView(): View {
                val myText = TextView(activity)
                myText.gravity = Gravity.CENTER
                myText.textSize = 19F
                myText.setTextColor(Color.WHITE)
                return myText;
            }
        })

        textSwitcher.setInAnimation(activity, android.R.anim.slide_in_left);
        textSwitcher.setOutAnimation(activity, android.R.anim.slide_out_right);




        return view
    }

    override fun onResume() {
        super.onResume()
        val strings = arrayOf(getString(R.string.overview_facts_quadro_board),
            getString(R.string.overview_facts_million_points),
            getString(R.string.overview_facts_london_tournament),
            getString(R.string.overview_facts_world_champoin),
            getString(R.string.overview_facts_fastest_hundret_eighty),
            getString(R.string.overview_facts_dart_reglement)
        )
        val interval = 7000L // 1 Second
        handler = Handler()
        runnable = object : Runnable {
            override fun run() {
                if (currentIndex == strings.size - 1) {
                    currentIndex = 0;
                }

                textSwitcher.setText(strings[currentIndex])

                textShowCount += 1
                currentIndex += 1
                handler.postDelayed(this, interval)
            }
        }

        handler.postDelayed(runnable, 50L)
    }

    override fun onStop() {
        super.onStop()
        LogEventsHelper(activity).logCount("overview_text", textShowCount)
        handler.removeCallbacks(runnable)
    }
}