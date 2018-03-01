package sdt.tkm.at.steeldarttrainer.training

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.InterstitialAd
import com.shawnlin.numberpicker.NumberPicker
import sdt.tkm.at.steeldarttrainer.R
import sdt.tkm.at.steeldarttrainer.base.DataHolder
import sdt.tkm.at.steeldarttrainer.base.LogEventsHelper
import sdt.tkm.at.steeldarttrainer.base.animateValue
import sdt.tkm.at.steeldarttrainer.models.XXTraining

/**
 * [Add class description here]
 *
 * Created 22.02.18
 *
 * @author Thomas Krainz-Mischitz (Level1 GmbH)
 * @version %I%, %G%
 */
class XXTrainingsActivity() : AppCompatActivity() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.xx_trainings_activity)

        supportActionBar?.title = getString(R.string.actionbar_title_xx_training)

        showChooserDialog()

        dataholder = DataHolder(this.applicationContext)
        initIntersital()

        singleButton = findViewById(R.id.xxSingleButton)
        singleButton.setOnClickListener {
            xxDartCount(1)
        }

        doubleButton = findViewById(R.id.xxDoubleButton)
        doubleButton.setOnClickListener {
            xxDartCount(2)
        }

        tripleButton = findViewById(R.id.xxTripleButton)
        tripleButton.setOnClickListener {
            xxDartCount(3)
        }

        resetButton = findViewById(R.id.xxResetButton)
        resetButton.setOnClickListener {
            animateValue(score, previousScore, scoreView)
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

        missButton = findViewById(R.id.xxMissButton)
        missButton.setOnClickListener {
            xxDartCount(0)
        }

        nextButton = findViewById(R.id.xxNextButton)
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

                dialogShown = true
                val inflater = this.layoutInflater
                val dialogHintBuilder = AlertDialog.Builder(
                    this)
                val finishDialogView = inflater.inflate(R.layout.multiple_button_dialog, null)
                val replayButton = finishDialogView.findViewById<Button>(R.id.newGameButton)
                val closeButton = finishDialogView.findViewById<Button>(R.id.closeButton)

                dialogHintBuilder.setView(finishDialogView)
                val finishDialog = dialogHintBuilder.create()

                replayButton.setOnClickListener {
                    showChooserDialog()
                    finishDialog.dismiss()
                    LogEventsHelper(this).logButtonTap("xoi_new_dialog_new")
                    dialogShown = false
                }

                closeButton.setOnClickListener {
                    finishDialog.dismiss()
                    onBackPressed()
                    LogEventsHelper(this).logButtonTap("xx_new_dialog_close")
                    dialogShown = false
                }

                finishDialog.setCancelable(false)
                finishDialog.setCanceledOnTouchOutside(false)
                finishDialog.show()
            }
        }

        scoreView = findViewById(R.id.xxScore)
        roundView = findViewById(R.id.xxRoundCount)
        dartView = findViewById(R.id.xxDartCount)

        firstDartView = findViewById(R.id.xxFirstDart)
        secondDartView = findViewById(R.id.xxSecondDart)
        thirdDartView = findViewById(R.id.xxThirdDart)

        singleView = findViewById(R.id.xxSingleAmount)
        doubleView = findViewById(R.id.xxDoubleAmount)
        tripleView = findViewById(R.id.xxTripleAmount)

        setupView()
    }

    override fun onResume() {
        super.onResume()
        initBanner()
    }

    private fun initBanner() {
        bannerAdView = findViewById(R.id.xxBanner)
        val adRequest = AdRequest.Builder().build()
        bannerAdView.loadAd(adRequest)

        bannerAdView.adListener = object: AdListener() {
            override fun onAdLoaded() {
                LogEventsHelper(this@XXTrainingsActivity).logBannerLoaded(className)
            }

            override fun onAdFailedToLoad(errorCode: Int) {
                bannerAdView.loadAd(adRequest)
                LogEventsHelper(this@XXTrainingsActivity).logBannerFailed(className, errorCode)
            }

            override fun onAdOpened() {
                bannerAdView.loadAd(adRequest)
                LogEventsHelper(this@XXTrainingsActivity).logBannerOpened(className)
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
        intersitalAd = InterstitialAd(this)
        intersitalAd.adUnitId = getString(R.string.interistal_id)
        intersitalAd.loadAd(AdRequest.Builder().build())
        intersitalAd.adListener = object: AdListener() {
            override fun onAdLoaded() {
                LogEventsHelper(this@XXTrainingsActivity).logIntersitalLoaded(className)
            }

            override fun onAdFailedToLoad(errorCode: Int) {
                LogEventsHelper(this@XXTrainingsActivity).logIntersitalFailed(className, errorCode)
                intersitalAd.loadAd(AdRequest.Builder().build())
            }

            override fun onAdOpened() {
                LogEventsHelper(this@XXTrainingsActivity).logIntersitalOpened(className)
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

    override fun onBackPressed() {

        if (!dialogShown) {
            dialogShown = true
            val inflater = this.layoutInflater
            val dialogHintBuilder = AlertDialog.Builder(
                this)
            val finishDialogView = inflater.inflate(R.layout.multiple_button_dialog, null)

            val dialogText = finishDialogView.findViewById<TextView>(R.id.dialogText)
            dialogText.text = this.getString(R.string.dialog_finish_training_text)
            val exitButton = finishDialogView.findViewById<Button>(R.id.newGameButton)
            exitButton.text = this.getString(R.string.dialog_finish_training_finish)
            val closeButton = finishDialogView.findViewById<Button>(R.id.closeButton)
            closeButton.text = this.getString(R.string.dialog_finish_training_close)

            dialogHintBuilder.setView(finishDialogView)
            val finishDialog = dialogHintBuilder.create()

            exitButton.setOnClickListener {
                finishDialog.dismiss()
                dialogShown = false
                LogEventsHelper(this).logButtonTap("xx_finish_dialog_close")
                super.onBackPressed()
            }

            closeButton.setOnClickListener {
                finishDialog.dismiss()
                LogEventsHelper(this).logButtonTap("xx_finish_dialog_continue")
                dialogShown = false
            }

            finishDialog.setCancelable(false)
            finishDialog.setCanceledOnTouchOutside(false)
            finishDialog.show()
            return
        }

        super.onBackPressed()
    }

    private fun newLeg() {

        animateValue(score, 0, scoreView)
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
            animateValue(score, score + value, scoreView)
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
        var pickerValue = 1
        dialogShown = true
        val inflater = this.layoutInflater
        val dialogHintBuilder = AlertDialog.Builder(
            this)
        val chooserDialogView = inflater.inflate(R.layout.chooser_dialog, null)

        val targetTitle = chooserDialogView.findViewById<TextView>(R.id.xxChooserTitle)
        targetTitle.text = getString(R.string.xx_chooser_title)

        val targetPicker = chooserDialogView.findViewById<NumberPicker>(R.id.xxChooserPicker)
        targetPicker.minValue = 0
        targetPicker.maxValue = 6
        targetPicker.displayedValues = arrayOf("15", "16", "17", "18", "19", "20", "Bull")

        targetPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            pickerValue = newVal
        }

        val chooserButton = chooserDialogView.findViewById<Button>(R.id.xxChooserButton)
        chooserButton.text = this.getString(R.string.xx_chooser_button_title)

        dialogHintBuilder.setView(chooserDialogView)
        val chooserDialog = dialogHintBuilder.create()

        chooserButton.setOnClickListener {

            when (pickerValue) {
                0 -> xxTarget = 15
                1 -> xxTarget = 16
                2 -> xxTarget = 17
                3 -> xxTarget = 18
                4 -> xxTarget = 19
                5 -> xxTarget = 20
                6 -> xxTarget = 25
            }

            LogEventsHelper(this).logButtonTap("chooser_dialog")
            chooserDialog.dismiss()
            dialogShown = false
            newLeg()
        }

        chooserDialog.setCancelable(false)
        chooserDialog.setCanceledOnTouchOutside(false)
        chooserDialog.show()
    }
}