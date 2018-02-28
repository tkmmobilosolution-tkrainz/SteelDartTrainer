package sdt.tkm.at.steeldarttrainer.training

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.AdapterView
import android.widget.Button
import android.widget.GridView
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.InterstitialAd
import sdt.tkm.at.steeldarttrainer.R
import sdt.tkm.at.steeldarttrainer.base.DataHolder
import sdt.tkm.at.steeldarttrainer.base.GridAdapter
import sdt.tkm.at.steeldarttrainer.base.LogEventsHelper
import sdt.tkm.at.steeldarttrainer.models.Dart
import sdt.tkm.at.steeldarttrainer.models.HighscoreTraining

/**
 * [Add class description here]
 *
 * Created 21.02.18
 *
 * @author Thomas Krainz-Mischitz (Level1 GmbH)
 * @version %I%, %G%
 */
class HighscoreTrainingsActivity : AppCompatActivity() {
    
    private lateinit var doubleButton: Button
    private lateinit var trippleButton: Button
    private lateinit var missButton: Button
    private lateinit var nextThrowButton: Button
    private lateinit var resetButton: Button
    
    private lateinit var scoreView: TextView
    private lateinit var dartView: TextView
    private lateinit var roundView: TextView
    private lateinit var firstDart: TextView
    private lateinit var secondDart: TextView
    private lateinit var thirdDart: TextView
    
    private lateinit var gridAdapter: GridAdapter
    private lateinit var gridView: GridView

    private lateinit var dataholder: DataHolder

    private lateinit var intersitalAd: InterstitialAd
    private lateinit var bannerAdView: AdView

    var currentThrow: Int = 0
    var dartMultiplier: Int = 1
    
    var dartAmount: Int = 0
    var previousDartAmount: Int = 0

    var throwCount: Int = 0
    var dartsCount: Int = 0
    var round: Int = 1

    var heighestThreeDart: Int = 0
    var checkoutTries: Int = 0

    var sixtyPlus: Int = 0
    var hundretPlus: Int = 0
    var hundretFourtyPlus = 0
    var hundretEigthy = 0

    var score: Int = START_SCORE
    var previousScore = score
    
    var dartResultArrayList: ArrayList<Int> = ArrayList()
    var threeDatrArrayList: ArrayList<Int> = ArrayList()

    var hasNoScore = false
    var dialogShown = false

    private var gameCount: Int = 0
    private val className = "hs_training"

    companion object {
        private const val START_SCORE = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.xoi_hs_trainings_activity)

        dataholder = DataHolder(this.applicationContext)
        supportActionBar?.title = getString(R.string.actionbar_title_hs_training)

        initIntersital()

        gridView = findViewById<GridView>(R.id.gridView)
        gridAdapter = GridAdapter(this)
        gridView.adapter = gridAdapter
        gridView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->

            if (throwCount <= 2 && !hasNoScore) {
                val dart: Dart = when (position) {
                    20 ->
                        Dart(25, dartMultiplier)
                    else ->
                        Dart(position + 1, dartMultiplier)
                }

                if (dart.value == 25 && dart.multiplier == 3) {
                    Toast.makeText(this, "Triple Bull not valid", Toast.LENGTH_LONG).show()
                } else {
                    currentThrow = throwCount
                    dartThrown(dart)
                }
            }
        }

        dartView = findViewById(R.id.dartCount)
        roundView = findViewById(R.id.roundCount)

        dartView.text = "Dart: 0"
        roundView.text = "${getString(R.string.general_round)}: 1"

        scoreView = findViewById(R.id.score)
        scoreView.text = score.toString()

        doubleButton = findViewById<Button>(R.id.doubleButton)
        doubleButton.setOnClickListener {
            setMultiplierButton(doubleButton)
            dartMultiplier = if (doubleButton.isSelected) 2 else 1
        }

        trippleButton = findViewById<Button>(R.id.trippleButton)
        trippleButton.setOnClickListener {
            setMultiplierButton(trippleButton)
            dartMultiplier = if (trippleButton.isSelected) 3 else 1
        }

        missButton = findViewById(R.id.missButton)
        missButton.setOnClickListener {
            if (throwCount <= 2 && !hasNoScore) {
                dartThrown(Dart(0, 1))
            }
        }

        nextThrowButton = findViewById(R.id.nextThrowButton)
        nextThrowButton.setOnClickListener {

            previousScore = score

            val zero = Dart(0, 1)
            if (throwCount == 1) {
                dartThrown(zero)
                dartThrown(zero)
            } else if (throwCount == 2) {
                dartThrown(zero)
            } else if (throwCount == 0) {
                dartThrown(zero)
                dartThrown(zero)
                dartThrown(zero)
            }

            checkHeighestThreeDart(dartsCount)
            previousDartAmount = dartAmount

            checkAmount(dartsCount)
            dartsCount = 0
            throwCount = 0
            doubleButton.isSelected = false
            trippleButton.isSelected = false
            hasNoScore = false
            increaseRound()
            defaultDarts()

            if (dartAmount == 30) {
                checkHeighestThreeDart(dartsCount)
                checkAmount(dartsCount)
                checkedOut()

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
                    dialogShown = false
                    finishDialog.dismiss()
                    LogEventsHelper(this).logButtonTap("hs_new_dialog_new")
                    newLeg()
                }

                closeButton.setOnClickListener {
                    finishDialog.dismiss()
                    onBackPressed()
                    LogEventsHelper(this).logButtonTap("hs_new_dialog_close")
                    dialogShown = false
                }

                finishDialog.setCancelable(false)
                finishDialog.setCanceledOnTouchOutside(false)
                finishDialog.show()
            }
        }

        resetButton = findViewById(R.id.resetButton)
        resetButton.setOnClickListener {
            score = previousScore
            dartAmount = previousDartAmount

            scoreView.text = score.toString()
            dartView.text = "Dart: " + dartAmount
            throwCount = 0
            doubleButton.isSelected = false
            trippleButton.isSelected = false
            defaultDarts()
        }

        firstDart = findViewById(R.id.firstDart)
        secondDart = findViewById(R.id.secondDart)
        thirdDart = findViewById(R.id.thirdDart)

        defaultDarts()
        checkIntersital()
        dataholder.increaseGameCount()
    }

    override fun onResume() {
        super.onResume()
        initBanner()
    }

    private fun checkIntersital() {

        gameCount = dataholder.getGameCount()
        if (gameCount % 4 == 0) {
            if (intersitalAd.isLoaded) {
                intersitalAd.show()
            }
        }
    }

    private fun initBanner() {
        bannerAdView = findViewById(R.id.trainingBanner)
        val adRequest = AdRequest.Builder().build()
        bannerAdView.loadAd(adRequest)

        bannerAdView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                LogEventsHelper(this@HighscoreTrainingsActivity).logBannerLoaded(className)
            }

            override fun onAdFailedToLoad(errorCode: Int) {
                bannerAdView.loadAd(adRequest)
                LogEventsHelper(this@HighscoreTrainingsActivity).logBannerFailed(className, errorCode)
            }

            override fun onAdOpened() {
                bannerAdView.loadAd(adRequest)
                LogEventsHelper(this@HighscoreTrainingsActivity).logBannerOpened(className)
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
        intersitalAd.adListener = object : AdListener() {
            override fun onAdLoaded() {
                LogEventsHelper(this@HighscoreTrainingsActivity).logIntersitalLoaded(className)
            }

            override fun onAdFailedToLoad(errorCode: Int) {
                LogEventsHelper(this@HighscoreTrainingsActivity).logIntersitalFailed(className, errorCode)
                intersitalAd.loadAd(AdRequest.Builder().build())
            }

            override fun onAdOpened() {
                LogEventsHelper(this@HighscoreTrainingsActivity).logIntersitalOpened(className)
            }

            override fun onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            override fun onAdClosed() {
                intersitalAd.loadAd(AdRequest.Builder().build())
            }
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
                LogEventsHelper(this).logButtonTap("hs_finish_dialog_close")
                super.onBackPressed()
            }

            closeButton.setOnClickListener {
                LogEventsHelper(this).logButtonTap("hs_finish_dialog_continue")
                finishDialog.dismiss()
                dialogShown = false
            }

            finishDialog.setCancelable(false)
            finishDialog.setCanceledOnTouchOutside(false)
            finishDialog.show()
            return
        }

        super.onBackPressed()
    }

    private fun defaultDarts() {
        firstDart.text = "-"
        secondDart.text = "-"
        thirdDart.text = "-"
    }

    private fun dartThrown(result: Dart) {
        val res = result.value * result.multiplier
        dartsCount += res
        throwCount += 1
        increaseDartAmount(1)
        score += res
        currentDart(result)
        scoreView.text = score.toString()
        resetThrow()
    }

    @SuppressLint("ResourceAsColor")
    private fun setMultiplierButton(button: Button) {

        button.isSelected = !button.isSelected
        if (button == doubleButton) {
            trippleButton.isSelected = false
        } else if (button == trippleButton) {
            doubleButton.isSelected = false
        }
    }

    private fun checkHeighestThreeDart(value: Int) {
        if (heighestThreeDart < value) {
            heighestThreeDart = value
        }
    }

    private fun increaseRound() {
        round += 1
        roundView.text = "${getString(R.string.general_round)}: $round"
        throwCount = 0
    }

    private fun increaseDartAmount(value: Int) {
        dartAmount += value
        dartView.text = "Dart: " + dartAmount
    }

    private fun resetThrow() {
        doubleButton.isSelected = false
        trippleButton.isSelected = false
        gridView.adapter = gridAdapter
        dartMultiplier = 1
        currentThrow = 0
    }

    private fun checkAmount(result: Int) {
        if (result == 180) {
            hundretEigthy += 1
        } else if (result >= 140 && result < 180) {
            hundretFourtyPlus += 1
        } else if (result >= 100 && result < 140) {
            hundretPlus += 1
        } else if (result >= 60 && result < 100) {
            sixtyPlus += 1
        }
    }

    private fun newLeg() {
        dartAmount = 0
        previousDartAmount = dartAmount
        dartView.text = "Dart: " + dartAmount

        round = 1
        roundView.text = "${getString(R.string.general_round)}: $round"

        heighestThreeDart = 0
        checkoutTries = 0

        sixtyPlus = 0
        hundretPlus = 0
        hundretFourtyPlus = 0
        hundretEigthy = 0

        dartResultArrayList.clear()
        threeDatrArrayList.clear()

        score = START_SCORE
        previousScore = score
        scoreView.text = score.toString()

        dartsCount = 0
        throwCount = 0
        doubleButton.isSelected = false
        trippleButton.isSelected = false
        hasNoScore = false

        defaultDarts()
        checkIntersital()
    }

    private fun checkedOut() {
        val training = HighscoreTraining(score, heighestThreeDart, sixtyPlus, hundretPlus, hundretFourtyPlus, hundretEigthy)
        dataholder.addHighscoreTraining(training)
    }

    private fun currentDart(dart: Dart) {

        val dartString = dartToString(dart)
        if (throwCount == 1) {
            firstDart.text = dartString
        } else if (throwCount == 2) {
            secondDart.text = dartString
        } else if (throwCount == 3) {
            thirdDart.text = dartString
        }
    }

    private fun dartToString(dart: Dart): String {

        if (dart.value == 0) {
            return "MISS"
        } else if (dart.value == 25) {
            if (dart.multiplier == 2) {
                return "D BULL"
            } else {
                return "BULL"
            }
        } else {
            if (dart.multiplier == 2) {
                return "D " + dart.value.toString()
            } else if (dart.multiplier == 3) {
                return "T " + dart.value.toString()
            }
        }

        return dart.value.toString()
    }
}