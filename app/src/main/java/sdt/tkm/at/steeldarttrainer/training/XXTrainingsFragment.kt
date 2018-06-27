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
import sdt.tkm.at.steeldarttrainer.base.animateIntegerValue
import sdt.tkm.at.steeldarttrainer.dialog.PickerDialogFragment
import sdt.tkm.at.steeldarttrainer.models.XXTraining

/**
 * [Add class description here]
 *
 * Created 22.02.18
 *
 * @author Thomas Krainz-Mischitz (Level1 GmbH)
 * @version %I%, %G%
 */
class XXTrainingsFragment() : Fragment() {

    private lateinit var singleButton: Button
    private lateinit var doubleButton: Button
    private lateinit var tripleButton: Button

    private lateinit var resetButton: Button
    private lateinit var missButton: Button
    private lateinit var nextButton: Button

    private lateinit var scoreView: TextView
    private lateinit var roundView: TextView
    private lateinit var dartView: TextView

    private lateinit var firstDartView: TextView
    private lateinit var secondDartView: TextView
    private lateinit var thirdDartView: TextView

    private lateinit var singleView: TextView
    private lateinit var doubleView: TextView
    private lateinit var tripleView: TextView

    var score: Int = 0
    var previousScore: Int = score

    var dartAmount: Int = 0
    var previousDartAmount: Int = dartAmount

    var roundAmount: Int = 0

    var dartsThrowCount: Int = 0

    var singleCount: Int = 0
    var doubleCount: Int = 0
    var tripleCount: Int = 0
    var previousSingleCount: Int = singleCount
    var previousDoubleCount: Int = doubleCount
    var previousTripleCount: Int = tripleCount

    var dialogShown = false

    var xxTarget = -1

    private lateinit var bannerAdView: AdView
    private val className = "xx_training"

    private var gameCount: Int = 0
    private lateinit var dataholder: DataHolder
    private lateinit var intersitalAd: InterstitialAd

    private lateinit var oververviewActivity: OverviewActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.xx_trainings_activity, container, false)

        oververviewActivity = activity as OverviewActivity
        showChooserDialog()

        dataholder = DataHolder(oververviewActivity)
        initIntersital()

        bannerAdView = view.findViewById(R.id.xxBanner)

        singleButton = view.findViewById(R.id.xxSingleButton)
        singleButton.setOnClickListener {
            xxDartCount(1)
        }

        doubleButton = view.findViewById(R.id.xxDoubleButton)
        doubleButton.setOnClickListener {
            xxDartCount(2)
        }

        tripleButton = view.findViewById(R.id.xxTripleButton)
        tripleButton.setOnClickListener {
            xxDartCount(3)
        }

        resetButton = view.findViewById(R.id.xxResetButton)
        resetButton.setOnClickListener {
            animateIntegerValue(score, previousScore, scoreView)
            score = previousScore

            singleCount = previousSingleCount
            doubleCount = previousDoubleCount
            tripleCount = previousTripleCount

            singleView.text = singleCount.toString()
            doubleView.text = doubleCount.toString()
            tripleView.text = tripleCount.toString()

            dartAmount = previousDartAmount
            setDartViewText(previousDartAmount)
            resetDartViews()
        }

        missButton = view.findViewById(R.id.xxMissButton)
        missButton.setOnClickListener {
            xxDartCount(0)
        }

        nextButton = view.findViewById(R.id.xxNextButton)
        nextButton.setOnClickListener {

            if (dartsThrowCount == 1) {
                xxDartCount(0)
                xxDartCount(0)
            } else if (dartsThrowCount == 2) {
                xxDartCount(0)
            } else if (dartsThrowCount == 0) {
                xxDartCount(0)
                xxDartCount(0)
                xxDartCount(0)
            }

            previousScore = score

            previousSingleCount = singleCount
            previousDoubleCount = doubleCount
            previousTripleCount = tripleCount

            previousDartAmount = dartAmount
            setDartViewText(previousDartAmount)
            increaseRound()
            resetDartViews()

            if (dartAmount == 99) {
                val training = XXTraining(xxTarget, score, singleCount, doubleCount, tripleCount)
                dataholder.addXXTraining(training)

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
                    showChooserDialog()
                    finishDialog.dismiss()
                    LogEventsHelper(oververviewActivity).logButtonTap("xx_new_dialog_new")
                    oververviewActivity.isDialogShown = false
                }

                closeButton.setOnClickListener {
                    oververviewActivity.isDialogShown = false
                    finishDialog.dismiss()
                    LogEventsHelper(oververviewActivity).logButtonTap("xx_new_dialog_close")
                    oververviewActivity.isDialogBackPressed = true
                    oververviewActivity.onBackPressed()
                }

                finishDialog.setCancelable(false)
                finishDialog.setCanceledOnTouchOutside(false)
                finishDialog.show()
            }
        }

        scoreView = view.findViewById(R.id.xxScore)
        roundView = view.findViewById(R.id.xxRoundCount)
        dartView = view.findViewById(R.id.xxDartCount)

        firstDartView = view.findViewById(R.id.xxFirstDart)
        secondDartView = view.findViewById(R.id.xxSecondDart)
        thirdDartView = view.findViewById(R.id.xxThirdDart)

        singleView = view.findViewById(R.id.xxSingleAmount)
        doubleView = view.findViewById(R.id.xxDoubleAmount)
        tripleView = view.findViewById(R.id.xxTripleAmount)

        setupView()

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
            }

            override fun onAdOpened() {
                intersitalAd.loadAd(AdRequest.Builder().build())
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

        animateIntegerValue(score, 0, scoreView)
        score = 0
        previousScore = score

        dartAmount = 0
        previousDartAmount = dartAmount
        setDartViewText(previousDartAmount)

        roundAmount = 0

        dartsThrowCount = 0

        singleCount = 0
        doubleCount = 0
        tripleCount = 0
        previousSingleCount = singleCount
        previousDoubleCount = doubleCount
        previousTripleCount = tripleCount

        setupView()
        checkIntersital()
    }

    private fun xxDartCount(value: Int) {
        if (dartsThrowCount < 3) {
            dartsThrowCount += 1
            dartAmount += 1
            setDartViewText(dartAmount)
            animateIntegerValue(score, score + value, scoreView)
            score += value
            setCurrentDart(value)
            setCountViews(value)
        }
    }

    private fun setCountViews(value: Int) {
        when (value) {
            1 -> {
                singleCount += 1
                singleView.text = singleCount.toString()
            }
            2 -> {
                doubleCount += 1
                doubleView.text = doubleCount.toString()
            }
            3 -> {
                tripleCount += 1
                tripleView.text = tripleCount.toString()
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
            1 -> return "Single"
            2 -> return "Double"
            3 -> return "Triple"
        }

        return "?!"
    }

    private fun setupView() {
        setDartViewText(0)
        roundView.text = "${getString(R.string.general_round)}: $roundAmount"
        singleView.text = "0"
        doubleView.text = "0"
        tripleView.text = "0"
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

    private fun showChooserDialog() {

        val xxArray = arrayOf("15", "16", "17", "18", "19", "20", "Bull")
        val pickerDialog = PickerDialogFragment.Companion.newPickerDialog(
            getString(R.string.xx_chooser_title),
            xxArray,
            getString(R.string.xx_chooser_title)
        )

        pickerDialog.listener = object : PickerDialogFragment.PickerDialogListener {
            override fun buttonClicked(value: Int) {
                when (value) {
                    0 -> xxTarget = 15
                    1 -> xxTarget = 16
                    2 -> xxTarget = 17
                    3 -> xxTarget = 18
                    4 -> xxTarget = 19
                    5 -> xxTarget = 20
                    6 -> xxTarget = 25
                }

                LogEventsHelper(oververviewActivity).logButtonTap("xx_chooser_dialog")
                pickerDialog.dismiss()
                oververviewActivity.isDialogShown = false
                newLeg()
            }

        }

        pickerDialog.show(fragmentManager, null)
        oververviewActivity.isDialogShown = true
    }
}