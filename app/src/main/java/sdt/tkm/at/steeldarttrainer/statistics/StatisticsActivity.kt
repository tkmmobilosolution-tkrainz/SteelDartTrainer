package sdt.tkm.at.steeldarttrainer.statistics

import android.app.Fragment
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import sdt.tkm.at.steeldarttrainer.R
import sdt.tkm.at.steeldarttrainer.base.DataHolder
import sdt.tkm.at.steeldarttrainer.base.LogEventsHelper
import sdt.tkm.at.steeldarttrainer.base.OverviewActivity

/**
 * [Add class description here]
 *
 * Created 21.02.18
 *
 * @author Thomas Krainz-Mischitz (Level1 GmbH)
 * @version %I%, %G%
 */
class StatisticsActivity : Fragment() {

    private lateinit var bannerAdView: AdView
    private val className = "statistics"

    private lateinit var infoButton: ImageButton

    private lateinit var oververviewActivity: OverviewActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View {

        oververviewActivity = activity as OverviewActivity

        val view = inflater.inflate(R.layout.statistics_activity, container, false)

        var currentStatistics: ShownStatistics = ShownStatistics.XOI

        val xoiButton = view.findViewById<Button>(R.id.xoiStatsButton)
        xoiButton.isSelected = true

        bannerAdView = view.findViewById(R.id.statsBanner)

        val hsButton = view.findViewById<Button>(R.id.highscoreStasButton)
        val nnButton = view.findViewById<Button>(R.id.ninetynineStatsButton)
        infoButton = view.findViewById<ImageButton>(R.id.statistics_info_button)
        infoButton.setOnClickListener {

            var dialogText = ""
            when (currentStatistics) {
                ShownStatistics.XOI -> {
                    dialogText = "${getString(R.string.statistics_ppd)}\n\n${getString(R.string.statistics_pptd)}\n\n${getString(R.string.statistics_cor)}\n\n${getString(R.string.statistics_dart_xoi)}\n\n${getString(R.string.statistics_sixty_plus)}\n\n${getString(R.string.statistics_hundret_plus)}\n\n${getString(R.string.statistics_hundretfourty_plus)}\n\n${getString(R.string.statistics_hundret_eighty)}"
                    LogEventsHelper(oververviewActivity).logButtonTap("statistics_info_xoi")
                }
                ShownStatistics.HIGHSCORE -> {
                    dialogText = "${getString(R.string.statistics_ppd)}\n\n${getString(R.string.statistics_pptd)}\n\n${getString(R.string.statistics_avg_score)}\n\n${getString(R.string.statistics_dart_hs)}\n\n${getString(R.string.statistics_sixty_plus)}\n\n${getString(R.string.statistics_hundret_plus)}\n\n${getString(R.string.statistics_hundretfourty_plus)}\n\n${getString(R.string.statistics_hundret_eighty)}"
                    LogEventsHelper(oververviewActivity).logButtonTap("statistics_info_hs")
                }
                ShownStatistics.XX_DARTS -> {
                    dialogText = "${getString(R.string.statistics_hits)}\n\n${getString(R.string.statistics_hits_percentage)}\n\n${getString(R.string.statistics_score)}\n\n${getString(R.string.statistics_single)}\n\n${getString(R.string.statistics_double)}\n\n${getString(R.string.statistics_triple)}"
                    LogEventsHelper(oververviewActivity).logButtonTap("statistics_info_xx")
                }
            }

            if (!dialogText.isNullOrEmpty()) {
                showInfoDialog(dialogText)
            }
        }

        replaceFragment(XOIStatsFragment())

        xoiButton.setOnClickListener {
            if (!xoiButton.isSelected) {
                xoiButton.isSelected = true
                hsButton.isSelected = false
                nnButton.isSelected = false
                currentStatistics = ShownStatistics.XOI
                LogEventsHelper(oververviewActivity).logButtonTap("statistics_xoi")

                replaceFragment(XOIStatsFragment())
            }
        }

        hsButton.setOnClickListener {
            if (!hsButton.isSelected) {
                xoiButton.isSelected = false
                hsButton.isSelected = true
                nnButton.isSelected = false
                currentStatistics = ShownStatistics.HIGHSCORE
                LogEventsHelper(oververviewActivity).logButtonTap("statistics_hs")

                replaceFragment(HighscoreStatsFragment())
            }
        }

        nnButton.setOnClickListener {
            if (!nnButton.isSelected) {
                xoiButton.isSelected = false
                hsButton.isSelected = false
                nnButton.isSelected = true
                currentStatistics = ShownStatistics.XX_DARTS
                LogEventsHelper(oververviewActivity).logButtonTap("statistics_xx")

                replaceFragment(XXStatsFragmentFragment())
            }
        }

        return view
    }

    override fun onResume() {
        super.onResume()

        val adRequest = AdRequest.Builder().build()
        bannerAdView.loadAd(adRequest)

        bannerAdView.adListener = object: AdListener() {
            override fun onAdLoaded() {
                LogEventsHelper(oververviewActivity).logBannerLoaded(className)
            }

            override fun onAdFailedToLoad(errorCode: Int) {
                bannerAdView.loadAd(adRequest)
                LogEventsHelper(oververviewActivity).logBannerFailed(className, errorCode)
            }

            override fun onAdOpened() {
                bannerAdView.loadAd(adRequest)
                LogEventsHelper(oververviewActivity).logBannerOpened(className)
            }

            override fun onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            override fun onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.frame_container, fragment)
        transaction.commitAllowingStateLoss()
    }

    private fun showInfoDialog(message: String) {
        oververviewActivity.isDialogShown = true
        val inflater = oververviewActivity.layoutInflater
        val dialogHintBuilder = AlertDialog.Builder(
            oververviewActivity)
        val hintDialogView = inflater.inflate(R.layout.neutral_dialog, null)
        val hintTitleView = hintDialogView.findViewById<TextView>(R.id.hintDialogTitle)
        hintTitleView.text = getString(R.string.statistics_hint_dialog_title)

        val hintMessageView = hintDialogView.findViewById<TextView>(R.id.hintDialogMessage)
        hintMessageView.gravity = Gravity.LEFT
        hintMessageView.text = message

        val okButton = hintDialogView.findViewById<Button>(R.id.hintDialogButton)
        okButton.text = getString(R.string.statistics_hint_dialog_button_text)
        dialogHintBuilder.setView(hintDialogView)
        val hintDialog = dialogHintBuilder.create()

        okButton.setOnClickListener {
            oververviewActivity.isDialogShown = false
            hintDialog.dismiss()
            LogEventsHelper(oververviewActivity).logButtonTap("statistics_info_ok")
        }

        hintDialog.setCancelable(false)
        hintDialog.setCanceledOnTouchOutside(false)
        hintDialog.show()
    }

    fun shouldShowInfoButton(show: Boolean) {
        if (show) {
            infoButton.visibility = View.VISIBLE
        } else {
            infoButton.visibility = View.GONE
        }
    }
}