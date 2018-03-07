package sdt.tkm.at.steeldarttrainer.training

import android.app.Fragment
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.InterstitialAd
import sdt.tkm.at.steeldarttrainer.R
import sdt.tkm.at.steeldarttrainer.base.DataHolder
import sdt.tkm.at.steeldarttrainer.base.LogEventsHelper
import sdt.tkm.at.steeldarttrainer.base.OverviewActivity
import sdt.tkm.at.steeldarttrainer.models.AroundTheClockTraining
import java.util.Random

/**
 * [Add class description here]
 *
 * Created 07.03.18
 *
 * @author Thomas Krainz-Mischitz (Level1 GmbH)
 * @version %I%, %G%
 */
class AroundTheClockTrainingsFragment: Fragment() {


    private lateinit var hitButton: Button
    private lateinit var resetButton: Button
    private lateinit var missButton: Button
    private lateinit var nextButton: Button

    private lateinit var scoreView: TextView
    private lateinit var roundView: TextView
    private lateinit var dartView: TextView

    private lateinit var firstDartView: TextView
    private lateinit var secondDartView: TextView
    private lateinit var thirdDartView: TextView

    var score: Int = 0
    var previousScore: Int = score

    var dartAmount: Int = 0
    var previousDartAmount: Int = dartAmount

    var roundAmount: Int = 0

    var dartsThrowCount: Int = 0

    private var previousTarget = 1
    private var currentTarget = 1
    private lateinit var bannerAdView: AdView
    private val className = "atc_training"

    private var gameCount: Int = 0
    private lateinit var dataholder: DataHolder
    private lateinit var intersitalAd: InterstitialAd

    private lateinit var oververviewActivity: OverviewActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.atc_fragment, container, false)

        oververviewActivity = activity as OverviewActivity

        dataholder = DataHolder(oververviewActivity)
        initIntersital()

        bannerAdView = view.findViewById(R.id.atcBanner)

        hitButton = view.findViewById(R.id.atcHit)
        hitButton.setOnClickListener {
            atcDartCount(1)
        }

        missButton = view.findViewById(R.id.atcMissButton)
        missButton.setOnClickListener {
            atcDartCount(0)
        }

        resetButton = view.findViewById(R.id.atcResetButton)
        resetButton.setOnClickListener {
            currentTarget = previousTarget
            scoreView.text = currentTarget.toString()

            score = previousScore

            dartAmount = previousDartAmount
            setDartViewText(previousDartAmount)
            resetDartViews()
        }

        nextButton = view.findViewById(R.id.atcNextButton)
        nextButton.setOnClickListener {

            if (dartsThrowCount == 1) {
                atcDartCount(0)
                atcDartCount(0)
            } else if (dartsThrowCount == 2) {
                atcDartCount(0)
            } else if (dartsThrowCount == 0) {
                atcDartCount(0)
                atcDartCount(0)
                atcDartCount(0)
            }

            previousTarget = currentTarget
            previousScore = score

            previousDartAmount = dartAmount
            setDartViewText(previousDartAmount)
            increaseRound()
            resetDartViews()
        }

        scoreView = view.findViewById(R.id.atcScore)
        roundView = view.findViewById(R.id.atcRoundCount)
        dartView = view.findViewById(R.id.atcDartCount)

        firstDartView = view.findViewById(R.id.atcFirstDart)
        secondDartView = view.findViewById(R.id.atcSecondDart)
        thirdDartView = view.findViewById(R.id.atcThirdDart)

        newLeg()

        return view
    }

    override fun onResume() {
        super.onResume()
        initBanner()
    }

    private fun initBanner() {
        val adRequest = AdRequest.Builder().build()
        bannerAdView.loadAd(adRequest)

        bannerAdView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                if (oververviewActivity != null) {
                    LogEventsHelper(oververviewActivity).logBannerLoaded(className)
                }
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

    private fun initIntersital() {
        intersitalAd = InterstitialAd(oververviewActivity)
        intersitalAd.adUnitId = getString(R.string.interistal_id)
        intersitalAd.loadAd(AdRequest.Builder().build())
        intersitalAd.adListener = object : AdListener() {
            override fun onAdLoaded() {
                LogEventsHelper(oververviewActivity).logIntersitalLoaded(className)
            }

            override fun onAdFailedToLoad(errorCode: Int) {
                LogEventsHelper(oververviewActivity).logIntersitalFailed(className, errorCode)
                intersitalAd.loadAd(AdRequest.Builder().build())
            }

            override fun onAdOpened() {
                LogEventsHelper(oververviewActivity).logIntersitalOpened(className)
            }

            override fun onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            override fun onAdClosed() {
                intersitalAd.loadAd(AdRequest.Builder().build())
            }
        }
    }

    private fun checkIntersital() {

        dataholder.increaseGameCount()
        gameCount = dataholder.getGameCount()
        if (gameCount % 4 == 0 && intersitalAd.isLoaded) {
            intersitalAd.show()
        }
    }

    private fun newLeg() {

        currentTarget = 1
        previousTarget = 1
        scoreView.text = currentTarget.toString()
        score = 0
        previousScore = score

        dartAmount = 0
        previousDartAmount = dartAmount
        setDartViewText(previousDartAmount)

        roundAmount = 0

        dartsThrowCount = 0

        setupView()
        checkIntersital()
    }

    private fun atcDartCount(value: Int) {
        if (dartsThrowCount < 3) {

            if (value == 1) {
                currentTarget += 1
            }

            dartsThrowCount += 1
            dartAmount += 1
            setDartViewText(dartAmount)
            setCurrentDart(value)

            if (currentTarget == 22) {
                val training = AroundTheClockTraining(dartAmount)
                dataholder.addATCTraining(training)

                oververviewActivity.isDialogShown = true
                val inflater = oververviewActivity.layoutInflater
                val dialogHintBuilder = AlertDialog.Builder(
                    oververviewActivity)
                val finishDialogView = inflater.inflate(R.layout.multiple_button_dialog, null)
                val replayButton = finishDialogView.findViewById<Button>(R.id.newGameButton)
                val closeButton = finishDialogView.findViewById<Button>(R.id.closeButton)

                dialogHintBuilder.setView(finishDialogView)
                val finishDialog = dialogHintBuilder.create()

                replayButton.setOnClickListener {
                    finishDialog.dismiss()
                    newLeg()
                    LogEventsHelper(oververviewActivity).logButtonTap("atc_new_dialog_new")
                    oververviewActivity.isDialogShown = false
                }

                closeButton.setOnClickListener {
                    oververviewActivity.isDialogShown = false
                    finishDialog.dismiss()
                    LogEventsHelper(oververviewActivity).logButtonTap("atc_new_dialog_close")
                    oververviewActivity.onBackPressed()
                }

                finishDialog.setCancelable(false)
                finishDialog.setCanceledOnTouchOutside(false)
                finishDialog.show()
            } else {
                scoreView.text = currentTarget.toString()
            }
        }
    }

    private fun setCurrentDart(value: Int) {
        when (dartsThrowCount) {
            1 -> firstDartView.text = multiplierToString(value)
            2 -> secondDartView.text = multiplierToString(value)
            3 -> thirdDartView.text = multiplierToString(value)
        }
    }

    private fun multiplierToString(multiplier: Int): String {

        when (multiplier) {
            0 -> return "Miss"
            1 -> return "Hit"
        }

        return "?!"
    }

    private fun setupView() {
        setDartViewText(0)
        roundView.text = "${getString(R.string.general_round)}: $roundAmount"
        resetDartViews()
    }

    private fun resetDartViews() {
        firstDartView.text = "-"
        secondDartView.text = "-"
        thirdDartView.text = "-"
        dartsThrowCount = 0
    }

    private fun setDartViewText(value: Int) {
        dartView.text = "Darts: $value"

    }

    private fun increaseRound() {
        roundAmount += 1
        roundView.text = "${getString(R.string.general_round)}: $roundAmount"
    }
}