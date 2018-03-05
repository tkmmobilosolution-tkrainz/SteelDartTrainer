package sdt.tkm.at.steeldarttrainer.training

import android.annotation.SuppressLint
import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import sdt.tkm.at.steeldarttrainer.base.OverviewActivity
import sdt.tkm.at.steeldarttrainer.base.animateIntegerValue
import sdt.tkm.at.steeldarttrainer.dialog.PickerDialogFragment
import sdt.tkm.at.steeldarttrainer.dialog.PickerDialogFragment.PickerDialogListener
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
class XOITrainingsFragment : Fragment() {

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

    var currentThrow: Int = 0
    var dartMultiplier: Int = 1

    var dartResultArrayList: ArrayList<Int> = ArrayList()
    var threeDatrArrayList: ArrayList<Int> = ArrayList()

    var hasNoScore = false

    private val className = "xoi_training"

    private var gameCount: Int = 0
    private var startAmount = -1
    var score: Int = -1
    var previousScore = score

    private lateinit var oververviewActivity: OverviewActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.xoi_hs_trainings_activity, container, false)

        oververviewActivity = activity as OverviewActivity

        bannerAdView = view.findViewById(R.id.trainingBanner)

        dataholder = DataHolder(oververviewActivity)
        initIntersital()

        gridView = view.findViewById<GridView>(R.id.gridView)
        gridAdapter = GridAdapter(oververviewActivity)
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
                    Toast.makeText(oververviewActivity, "Triple Bull not valid", Toast.LENGTH_LONG).show()
                } else {
                    currentThrow = throwCount
                    dartThrown(dart, false)
                }
            }
        }

        dartView = view.findViewById(R.id.dartCount)
        roundView = view.findViewById(R.id.roundCount)
        scoreView = view.findViewById(R.id.score)

        doubleButton = view.findViewById<Button>(R.id.doubleButton)
        doubleButton.setOnClickListener {
            setMultiplierButton(doubleButton)
            dartMultiplier = if (doubleButton.isSelected) 2 else 1
        }

        trippleButton = view.findViewById<Button>(R.id.trippleButton)
        trippleButton.setOnClickListener {
            setMultiplierButton(trippleButton)
            dartMultiplier = if (trippleButton.isSelected) 3 else 1
        }

        missButton = view.findViewById(R.id.missButton)
        missButton.setOnClickListener {
            if (throwCount <= 2 && !hasNoScore) {
                dartThrown(Dart(0, 1), false)
            }
        }

        nextThrowButton = view.findViewById(R.id.nextThrowButton)
        nextThrowButton.setOnClickListener {

            if (hasNoScore) {

                animateIntegerValue(score, previousScore, scoreView)
                score = previousScore
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
            doubleButton.isSelected = false
            trippleButton.isSelected = false
            hasNoScore = false
        }

        resetButton = view.findViewById(R.id.resetButton)
        resetButton.setOnClickListener {

            animateIntegerValue(score, previousScore, scoreView)
            score = previousScore
            dartAmount = previousDartAmount

            dartView.text = "Darts: " + dartAmount
            throwCount = 0
            doubleButton.isSelected = false
            trippleButton.isSelected = false
            defaultDarts()
        }

        firstDart = view.findViewById(R.id.firstDart)
        secondDart = view.findViewById(R.id.secondDart)
        thirdDart = view.findViewById(R.id.thirdDart)

        showChooserDialog()

        /**
        val speekButton: Button = findViewById(R.id.speechButton)
        speekButton.setBackgroundColor(Color.LTGRAY)
        speekButton.setOnClickListener {
        speech()
        }
         */

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

    /**
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.getItemId()) {
    android.R.id.home -> {
    if (!oververviewActivity.isDialogShown) {
    oververviewActivity.isDialogShown = true
    val chooserDialog = ChooserDialogFragment.Companion.newChooserDialog(
    getString(R.string.general_hint),
    getString(R.string.dialog_finish_training_text),
    getString(R.string.dialog_finish_training_finish),
    getString(R.string.dialog_finish_training_close)
    )

    chooserDialog.listener = object : ChooserDialogFragment.ChooserDialogListener {
    override fun positivButtonClicked() {
    chooserDialog.dismiss()
    oververviewActivity.isDialogShown = false
    LogEventsHelper(oververviewActivity).logButtonTap("xoi_finish_dialog_close")
    activity.onBackPressed()
    }

    override fun negativeButtonClicked() {
    chooserDialog.dismiss()
    LogEventsHelper(oververviewActivity).logButtonTap("xoi_finish_dialog_continue")
    oververviewActivity.isDialogShown = false
    }

    }
    return true
    }

    return false
    }
    else -> return super.onOptionsItemSelected(item)
    }
    }
     */
    private fun defaultDarts() {
        firstDart.text = "-"
        secondDart.text = "-"
        thirdDart.text = "-"
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
            animateIntegerValue(score, 0, scoreView)
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

            Toast.makeText(oververviewActivity, getString(R.string.general_no_score), Toast.LENGTH_SHORT).show()
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

            animateIntegerValue(score, score - res, scoreView)
            score -= res
        }

        currentDart(result)
        resetThrow()
        return returnValue
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
        dartView.text = "Darts: " + dartAmount
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
        dartView.text = "Darts: " + dartAmount

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

        animateIntegerValue(0, startAmount, scoreView)
        previousScore = score

        defaultDarts()
        checkIntersital()
    }

    private fun checkedOut() {
        val training = XOITraining(startAmount, dartAmount, heighestThreeDart, checkoutTries, sixtyPlus,
            hundretPlus, hundretFourtyPlus, hundretEigthy)
        dataholder.addXOITraining(training)

        oververviewActivity.isDialogShown = true
        /**
        val chooserDialog = ChooserDialogFragment.Companion.newChooserDialog(
        getString(R.string.dialog_new_training_title),
        getString(R.string.dialog_new_training_text),
        getString(R.string.dialog_new_training_new),
        getString(R.string.dialog_new_training_close)
        )

        chooserDialog.listener = object : ChooserDialogFragment.ChooserDialogListener {
        override fun positivButtonClicked() {

        }

        override fun negativeButtonClicked() {

        }

        }
         */
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
            LogEventsHelper(oververviewActivity).logButtonTap("xoi_new_dialog_new")
            oververviewActivity.isDialogShown = false
        }

        closeButton.setOnClickListener {
            finishDialog.dismiss()
            fragmentManager.popBackStack()
            LogEventsHelper(oververviewActivity).logButtonTap("xoi_new_dialog_close")
            oververviewActivity.isDialogShown = false
        }

        finishDialog.setCancelable(false)
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

    private fun showChooserDialog() {
        val xoiArray = arrayOf("301", "501", "701", "801", "901", "1001")
        val pickerDialog = PickerDialogFragment.Companion.newPickerDialog(
            getString(R.string.trainings_overview_xoi_hint_title),
            xoiArray,
            getString(R.string.trainings_overview_xoi_hint_button_text)
        )

        pickerDialog.listener = object : PickerDialogListener {
            override fun buttonClicked(value: Int) {
                oververviewActivity.isDialogShown = false
                score = xoiArray[value].toInt()
                startAmount = score
                newLeg()
                LogEventsHelper(oververviewActivity).logButtonTap("chooser_dialog")
                pickerDialog.dismiss()
            }

        }

        pickerDialog.show(fragmentManager, null)
        oververviewActivity.isDialogShown = true
    }
}