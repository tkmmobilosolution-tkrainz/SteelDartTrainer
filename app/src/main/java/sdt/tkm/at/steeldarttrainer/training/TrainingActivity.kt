package sdt.tkm.at.steeldarttrainer.training

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
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
import com.shawnlin.numberpicker.NumberPicker
import sdt.tkm.at.steeldarttrainer.R
import sdt.tkm.at.steeldarttrainer.base.DataHolder
import sdt.tkm.at.steeldarttrainer.base.GridAdapter
import sdt.tkm.at.steeldarttrainer.base.LogEventsHelper
import sdt.tkm.at.steeldarttrainer.models.Dart
import sdt.tkm.at.steeldarttrainer.models.XOITraining
import java.util.Locale

/**
 * [Add class description here]
 *
 * Created 22.01.18
 *
 * @author Thomas Krainz-Mischitz (Level1 GmbH)
 * @version %I%, %G%
 */
class TrainingActivity : AppCompatActivity() {

    var currentThrow: Int = 0
    var dartMultiplier: Int = 1
    var doubleButton: Button? = null
    var trippleButton: Button? = null
    var missButton: Button? = null
    var nextThrowButton: Button? = null
    var resetButton: Button? = null
    var scoreView: TextView? = null
    var dartView: TextView? = null
    var roundView: TextView? = null
    var firstDart: TextView? = null
    var secondDart: TextView? = null
    var thirdDart: TextView? = null

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

    var gridAdapter: GridAdapter? = null
    var gridView: GridView? = null

    var dartResultArrayList: ArrayList<Int> = ArrayList()
    var threeDatrArrayList: ArrayList<Int> = ArrayList()

    var hasNoScore = false
    var dialogShown = false

    private lateinit var bannerAdView: AdView
    private val className = "xoi_training"

    private var gameCount: Int = 0
    private lateinit var dataholder: DataHolder
    private lateinit var intersitalAd: InterstitialAd

    var score: Int = -1
    var previousScore = score

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.training_activity)

        supportActionBar?.title = getString(R.string.actionbar_title_xoi_training)

        dataholder = DataHolder(this.applicationContext)
        initIntersital()

        gridView = findViewById<GridView>(R.id.gridView)
        gridAdapter = GridAdapter(this)
        gridView!!.adapter = gridAdapter
        gridView!!.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->

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
                    dartThrown(dart, false)
                }
            }
        }

        dartView = findViewById(R.id.dartCount)
        roundView = findViewById(R.id.roundCount)
        scoreView = findViewById(R.id.score)

        doubleButton = findViewById<Button>(R.id.doubleButton)
        doubleButton!!.setOnClickListener {
            setMultiplierButton(doubleButton!!)
            dartMultiplier = if (doubleButton!!.isSelected) 2 else 1
        }

        trippleButton = findViewById<Button>(R.id.trippleButton)
        trippleButton!!.setOnClickListener {
            setMultiplierButton(trippleButton!!)
            dartMultiplier = if (trippleButton!!.isSelected) 3 else 1
        }

        missButton = findViewById(R.id.missButton)
        missButton!!.setOnClickListener {
            if (throwCount <= 2 && !hasNoScore) {
                dartThrown(Dart(0, 1), false)
            }
        }

        nextThrowButton = findViewById(R.id.nextThrowButton)
        nextThrowButton!!.setOnClickListener {

            if (hasNoScore) {
                score = previousScore
                scoreView!!.text = score.toString()
                dartAmount = previousDartAmount
            } else {
                previousScore = score

                val zero = Dart(0, 1)
                if (throwCount == 1) {
                    dartThrown(zero, false)
                    dartThrown(zero, false)
                } else if (throwCount == 2) {
                    dartThrown(zero, false)
                } else if (throwCount == 0) {
                    dartThrown(zero, false)
                    dartThrown(zero, false)
                    dartThrown(zero, false)
                }

                checkHeighestThreeDart(dartsCount)
                previousDartAmount = dartAmount
                checkAmount(dartsCount)
            }

            increaseRound()
            dartsCount = 0
            throwCount = 0
            defaultDarts()
            doubleButton!!.isSelected = false
            trippleButton!!.isSelected = false
            hasNoScore = false
        }

        resetButton = findViewById(R.id.resetButton)
        resetButton!!.setOnClickListener {
            score = previousScore
            dartAmount = previousDartAmount

            scoreView!!.text = score.toString()
            dartView!!.text = "Darts: " + dartAmount
            throwCount = 0
            doubleButton!!.isSelected = false
            trippleButton!!.isSelected = false
            defaultDarts()
        }

        firstDart = findViewById(R.id.firstDart)
        secondDart = findViewById(R.id.secondDart)
        thirdDart = findViewById(R.id.thirdDart)

        dataholder.increaseGameCount()

        showChooserDialog()

        /**
        val speekButton: Button = findViewById(R.id.speechButton)
        speekButton.setBackgroundColor(Color.LTGRAY)
        speekButton.setOnClickListener {
        speech()
        }
         */
    }

    override fun onResume() {
        super.onResume()
        initBanner()
    }

    private fun initBanner() {
        bannerAdView = findViewById(R.id.trainingBanner)
        val adRequest = AdRequest.Builder().build()
        bannerAdView.loadAd(adRequest)

        bannerAdView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                LogEventsHelper(this@TrainingActivity).logBannerLoaded(className)
            }

            override fun onAdFailedToLoad(errorCode: Int) {
                bannerAdView.loadAd(adRequest)
                LogEventsHelper(this@TrainingActivity).logBannerFailed(className, errorCode)
            }

            override fun onAdOpened() {
                bannerAdView.loadAd(adRequest)
                LogEventsHelper(this@TrainingActivity).logBannerOpened(className)
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
                LogEventsHelper(this@TrainingActivity).logIntersitalLoaded(className)
            }

            override fun onAdFailedToLoad(errorCode: Int) {
                LogEventsHelper(this@TrainingActivity).logIntersitalFailed(className, errorCode)
                intersitalAd.loadAd(AdRequest.Builder().build())
            }

            override fun onAdOpened() {
                LogEventsHelper(this@TrainingActivity).logIntersitalOpened(className)
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
            val finishDialogView = inflater.inflate(R.layout.dialog_replay, null)

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
                LogEventsHelper(this).logButtonTap("xoi_finish_dialog_close")
                super.onBackPressed()
            }

            closeButton.setOnClickListener {
                finishDialog.dismiss()
                LogEventsHelper(this).logButtonTap("xoi_finish_dialog_continue")
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
        firstDart!!.text = "-"
        secondDart!!.text = "-"
        thirdDart!!.text = "-"
    }

    private fun speech() {
        val i = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Please speak your current single dart result clearly as a number. For example, Tipple 20 is \"60\"")
        startActivityForResult(i, 1000)
    }

    private fun threeDartsThrown(list: List<Dart>) {

        /**
        var string: String = ""
        for (s in list) {
        string += s
        }

        Toast.makeText(this, string, Toast.LENGTH_LONG).show()
         */

        for (item in list) {
            if (dartThrown(item, true)) {
                break
            }
        }
    }

    private fun dartThrown(result: Dart, isSpeech: Boolean): Boolean {

        var returnValue = false
        val res = result.value * result.multiplier
        dartsCount += res
        val multiplier = result.multiplier
        throwCount += 1
        increaseDartAmount(1)

        if (multiplier == 2 && res == score) {
            dartResultArrayList.add(res)
            checkoutTries += 1
            checkAmount(dartsCount)
            checkHeighestThreeDart(dartsCount)
            score = 0
            dartsCount = 0
            throwCount = 0
            checkedOut()
            returnValue = true
        } else if (res >= score - 1) {

            dartResultArrayList.add(-1)
            if (score % 2 == 0 && score <= 40 || score == 50) {
                checkoutTries += 1
            }

            Toast.makeText(this, getString(R.string.general_no_score), Toast.LENGTH_SHORT).show()
            if (throwCount == 1) {
                increaseDartAmount(2)
            } else if (throwCount == 2) {
                increaseDartAmount(1)
            }
            returnValue = true
            hasNoScore = true
        } else {
            dartResultArrayList.add(res)
            if (score % 2 == 0 && score <= 40 || score == 50) {
                checkoutTries += 1
            }

            score -= res
        }

        currentDart(result)
        scoreView!!.text = score.toString()
        resetThrow()
        return returnValue
    }

    @SuppressLint("ResourceAsColor")
    private fun setMultiplierButton(button: Button) {

        button.isSelected = !button.isSelected
        if (button == doubleButton!!) {
            trippleButton!!.isSelected = false
        } else if (button == trippleButton!!) {
            doubleButton!!.isSelected = false
        }
    }

    private fun checkHeighestThreeDart(value: Int) {
        if (heighestThreeDart < value) {
            heighestThreeDart = value
        }
    }

    private fun increaseRound() {
        round += 1
        roundView!!.text = "${getString(R.string.general_round)}: $round"
        throwCount = 0
    }

    private fun increaseDartAmount(value: Int) {
        dartAmount += value
        dartView!!.text = "Darts: " + dartAmount
    }

    private fun resetThrow() {
        doubleButton!!.isSelected = false
        trippleButton!!.isSelected = false
        gridView!!.adapter = gridAdapter
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
        dartView!!.text = "Darts: " + dartAmount

        round = 1
        roundView!!.text = "${getString(R.string.general_round)}: $round"

        heighestThreeDart = 0
        checkoutTries = 0

        sixtyPlus = 0
        hundretPlus = 0
        hundretFourtyPlus = 0
        hundretEigthy = 0

        dartResultArrayList.clear()
        threeDatrArrayList.clear()

        previousScore = score
        scoreView!!.text = score.toString()

        defaultDarts()
        dataholder.increaseGameCount()
        checkIntersital()
    }

    private fun checkedOut() {
        val training = XOITraining(501, dartAmount, heighestThreeDart, checkoutTries, sixtyPlus,
            hundretPlus, hundretFourtyPlus, hundretEigthy)
        dataholder.addXOITraining(training)

        dialogShown = true
        val inflater = this.layoutInflater
        val dialogHintBuilder = AlertDialog.Builder(
            this)
        val finishDialogView = inflater.inflate(R.layout.dialog_replay, null)
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
            LogEventsHelper(this).logButtonTap("xoi_new_dialog_close")
            dialogShown = false
        }

        finishDialog.setCanceledOnTouchOutside(false)
        finishDialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1000 && data != null) {

            //handleVoiceInput(data)
            /**
            var string = ""
            for (res in result) {
            string += res + "\n"
            }
            Toast.makeText(this, string, Toast.LENGTH_LONG).show()


            var list: MutableList<Int> = mutableListOf()
            var singleNumber = -1
            for (string in result) {
            try {
            singleNumber = string.toInt()
            Toast.makeText(this, string, Toast.LENGTH_LONG).show()
            break
            } catch (e: Exception) {
            if (string.contains("-")) {
            var l = string.split("-")
            Toast.makeText(this, string, Toast.LENGTH_LONG).show()

            for (item in l) {
            try {
            var integer = item.toInt()
            if (checkValidDartResult(integer)) {
            list.add(item.toInt())
            }
            } catch (e: Exception) {
            }
            }
            }
            }
            }
            if (singleNumber != -1) {
            if (checkValidDartResult(singleNumber)) {
            val singleDartList: List<Int> = listOf(singleNumber, 0, 0)
            threeDartsThrown(singleDartList)
            }
            } else if (list.isNotEmpty()) {
            if (list.size == 2) {
            list.add(0)
            }

            threeDartsThrown(list)
            }
             */
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun checkValidDartResult(result: Int): Boolean {
        return result <= 20
    }

    private fun handleVoiceInput(data: Intent) {
        var darts: MutableList<Dart> = mutableListOf()
        val result: ArrayList<String> = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)

        var resultString = ""
        val regex: Regex = Regex(pattern = """\d+""")

        for (res in result) {
            val checkString = res.toLowerCase()
            if (checkString.contains("bullseye")) {
                if (regex.containsMatchIn(checkString)) {
                    resultString = res.toLowerCase()
                    break
                } else {
                    resultString = res.toLowerCase()
                }
            } else if (regex.containsMatchIn(checkString)) {
                resultString = res.toLowerCase()
                break
            }
        }

        val resultList = resultString.split(" ")

        if (resultList.size % 2 == 0) {
            for (index in 0..resultList.size - 1) {
                if (index % 2 == 1) {
                    val multiplierString = resultList[index - 1].toLowerCase()
                    if ((multiplierString.contains("single") ||
                            multiplierString.contains("double") ||
                            multiplierString.contains("triple"))) {

                        var multiplier = -1
                        if (multiplierString.equals("single")) {
                            multiplier = 1
                        } else if (multiplierString.equals("double")) {
                            multiplier = 2
                        } else if (multiplierString.equals("triple")) {
                            multiplier = 3
                        }

                        try {
                            val value = resultList[index].toInt()
                            if (checkValidDartResult(value)) {
                                darts.add(Dart(value, multiplier))
                            }
                        } catch (e: Exception) {
                            if (resultList[index].equals("bullseye") && (multiplier == 1 || multiplier == 2)) {
                                darts.add(Dart(25, multiplier))
                            } else {
                                darts = mutableListOf()
                            }

                        }
                    } else {
                        darts = mutableListOf()
                    }
                }
            }
        } else {
        }

        val zeroDart = Dart(0, 1)
        if (darts.size == 1) {
            darts.add(zeroDart)
            darts.add(zeroDart)
            threeDartsThrown(darts)
        } else if (darts.size == 2) {
            darts.add(zeroDart)
            threeDartsThrown(darts)
        } else if (darts.isEmpty()) {
            //TODO: Wrong text show alert
        } else {
            threeDartsThrown(darts)
        }
    }

    private fun currentDart(dart: Dart) {

        val dartString = dartToString(dart)
        if (throwCount == 1) {
            firstDart!!.text = dartString
        } else if (throwCount == 2) {
            secondDart!!.text = dartString
        } else if (throwCount == 3) {
            thirdDart!!.text = dartString
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

    private fun showChooserDialog() {
        var pickerValue = 1
        dialogShown = true
        val inflater = this.layoutInflater
        val dialogHintBuilder = AlertDialog.Builder(
            this)
        val chooserDialogView = inflater.inflate(R.layout.xx_chooser_dialog, null)

        val targetTitle = chooserDialogView.findViewById<TextView>(R.id.xxChooserTitle)
        targetTitle.text = getString(R.string.xoi_chooser_title)

        val targetPicker = chooserDialogView.findViewById<NumberPicker>(R.id.xxChooserPicker)
        targetPicker.minValue = 0
        targetPicker.maxValue = 5
        val xoiArray = arrayOf("301", "501", "701", "801", "901", "1001")
        targetPicker.displayedValues = xoiArray

        targetPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            pickerValue = newVal
        }

        val chooserButton = chooserDialogView.findViewById<Button>(R.id.xxChooserButton)
        chooserButton.text = this.getString(R.string.xoi_chooser_button_title)

        dialogHintBuilder.setView(chooserDialogView)
        val chooserDialog = dialogHintBuilder.create()

        chooserButton.setOnClickListener {

            score = xoiArray[pickerValue].toInt()

            chooserDialog.dismiss()
            dialogShown = false
            LogEventsHelper(this).logButtonTap("xx_chooser_dialog")
            newLeg()
        }

        chooserDialog.setCancelable(false)
        chooserDialog.setCanceledOnTouchOutside(false)
        chooserDialog.show()
    }
}