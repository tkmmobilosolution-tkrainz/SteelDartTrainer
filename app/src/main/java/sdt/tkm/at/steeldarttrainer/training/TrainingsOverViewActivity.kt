package sdt.tkm.at.steeldarttrainer.training

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import sdt.tkm.at.steeldarttrainer.R
import sdt.tkm.at.steeldarttrainer.base.DataHolder
import sdt.tkm.at.steeldarttrainer.base.LogEventsHelper

/**
 * [Add class description here]
 *
 * Created 21.02.18
 *
 * @author Thomas Krainz-Mischitz (Level1 GmbH)
 * @version %I%, %G%
 */
class TrainingsOverViewActivity: AppCompatActivity() {

    private lateinit var bannerAdView: AdView
    private val className = "trainings_overview"
    private val dataHolder = DataHolder(this)

    private var dialogShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.select_training_activity)

        supportActionBar?.title = getString(R.string.actionbar_title_trainings_overview)

        val xoiButton = findViewById<Button>(R.id.xoiButton)
        xoiButton.setOnClickListener {
            val intent = Intent(this, TrainingActivity::class.java)
            if (dataHolder.shouldShowXOIOverviewHint()) {
                showInformationDialog(getString(R.string.trainings_overview_xoi_hint_message), intent)
                dataHolder.xOIOverviewHintShown()
            } else {
                startActivity(intent)
            }
            LogEventsHelper(this).logButtonTap("trainings_overview_xoi")
        }

        val hsButton = findViewById<Button>(R.id.highscoreButton)
        hsButton.setOnClickListener {
            val intent = Intent(this, HighscoreTrainingsActivity::class.java)
            startActivity(intent)
            LogEventsHelper(this).logButtonTap("trainings_overview_hs")
        }

        val xxButton = findViewById<Button>(R.id.dartsToXButton)
        xxButton.setOnClickListener {
            val intent = Intent(this, XXTrainingActivity::class.java)
            startActivity(intent)
            LogEventsHelper(this).logButtonTap("trainings_overview_xx")
        }
    }

    override fun onResume() {
        super.onResume()

        bannerAdView = findViewById(R.id.trainingsOverviewBanner)
        val adRequest = AdRequest.Builder().build()
        bannerAdView.loadAd(adRequest)

        bannerAdView.adListener = object: AdListener() {
            override fun onAdLoaded() {
                LogEventsHelper(this@TrainingsOverViewActivity).logBannerLoaded(className)
            }

            override fun onAdFailedToLoad(errorCode: Int) {
                bannerAdView.loadAd(adRequest)
                LogEventsHelper(this@TrainingsOverViewActivity).logBannerFailed(className, errorCode)
            }

            override fun onAdOpened() {
                bannerAdView.loadAd(adRequest)
                LogEventsHelper(this@TrainingsOverViewActivity).logBannerOpened(className)
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

    override fun onBackPressed() {
        if (!dialogShown) {
            super.onBackPressed()
        }
    }

    private fun showInformationDialog(message: String, intent: Intent) {
        val inflater = this.layoutInflater
        val dialogHintBuilder = AlertDialog.Builder(
            this)
        val hintDialogView = inflater.inflate(R.layout.dialog_hint, null)
        val hintTitleView = hintDialogView.findViewById<TextView>(R.id.hintDialogTitle)
        hintTitleView.text = getString(R.string.trainings_overview_xoi_hint_title)

        val hintMessageView = hintDialogView.findViewById<TextView>(R.id.hintDialogMessage)
        hintMessageView.text = message

        val okButton = hintDialogView.findViewById<Button>(R.id.hintDialogButton)
        okButton.text = getString(R.string.trainings_overview_xoi_hint_button_text)
        dialogHintBuilder.setView(hintDialogView)
        val hintDialog = dialogHintBuilder.create()

        okButton.setOnClickListener {
            dialogShown = false
            startActivity(intent)
            LogEventsHelper(this).logButtonTap("trainings_overview_dialog")
            hintDialog.dismiss()
        }

        hintDialog.setCancelable(false)
        hintDialog.setCanceledOnTouchOutside(false)
        hintDialog.show()
    }
}