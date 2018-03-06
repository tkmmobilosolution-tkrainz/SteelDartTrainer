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
        val strings = arrayOf("Wusstest du das es auch ein Dartboard mit Quadro Feldern gibt? Bei diesem Board gibt es einen weiteren Ring. Dieser zählt den vierfachen Wert.",
            "Martin Petrov, warf in 137,1 Stunden, aufgeteilt auf 6 Tage, insgesamt 1.000.001 Punkte.",
            "1937 wollten 300.000 Spieler, bei einem Stadt-Turnier in London teilnehmen.",
            "Phil Taylor hat bisher die meisten Weltmeistertitel gewinnen können. Er erlangt 16 Titel, davon 14 in der PDC und 2 in der BDO.",
            "Ricky Evans warf bis her die schnellste 180 im Live TV. Dafür brauchte er nur 2,13 Sekunden.",
            "Ein Pfeil darf nicht länger als 30,5 Zentimeter sein. Das maximale Gewicht eines Pfeils darf 50 Gramm betragen."
        )
        val interval = 7000L // 1 Second
        handler = Handler()
        runnable = object : Runnable {
            override fun run() {
                if (currentIndex == strings.size - 1) {
                    currentIndex = 0;
                }

                textSwitcher.setText(strings[currentIndex])

                currentIndex += 1
                handler.postDelayed(this, interval)
            }
        }

        handler.postDelayed(runnable, 50L)
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(runnable)
    }
}