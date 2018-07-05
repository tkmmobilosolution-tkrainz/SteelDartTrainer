package sdt.tkm.at.steeldarttrainer.base

import android.app.Application
import android.content.Context
import android.preference.PreferenceManager
import android.util.Log
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import sdt.tkm.at.steeldarttrainer.models.AroundTheClockTraining
import sdt.tkm.at.steeldarttrainer.models.HighscoreTraining
import sdt.tkm.at.steeldarttrainer.models.RandomTraining
import sdt.tkm.at.steeldarttrainer.models.XOITraining
import sdt.tkm.at.steeldarttrainer.models.XXTraining
import java.util.*
import android.support.annotation.NonNull
import com.google.android.gms.tasks.OnFailureListener



/**
 * [Add class description here]
 *
 * Created 26.01.18
 *
 * @author Thomas Krainz-Mischitz (Level1 GmbH)
 * @version %I%, %G%
 */
class DataHolder(val context: Context = Application().baseContext) {
  var xoiList: ArrayList<XOITraining>? = null
  var hsList: ArrayList<HighscoreTraining>? = null
  var xxList: ArrayList<XXTraining>? = null
  var randomList: ArrayList<RandomTraining>? = null
  var atcList: ArrayList<AroundTheClockTraining>? = null

  fun addXOITraining(training: XOITraining) {
    val list = getXOITrainingsList()
    list.add(training)
    saveXOITrainingsList(list)
    calculateGamePoints(training.ppdAvarage().toFloat())
  }

  private fun saveXOITrainingsList(list: ArrayList<XOITraining>) {
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    val jsonString = Gson().toJson(list)
    preferences.edit().putBoolean("xoi_checked", true).apply()
    preferences.edit().putString("xoi-training", jsonString).apply()
    xoiList = list
  }

  fun getXOITrainingsList(): ArrayList<XOITraining> {
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    if (preferences != null) {
      val json = preferences.getString("xoi-training", null)
      if (json != null) {
        val turnsType = object : TypeToken<List<XOITraining>>() {}.type
        return Gson().fromJson<ArrayList<XOITraining>>(json, turnsType)
      }
    }
    return ArrayList()
  }

  fun addHighscoreTraining(training: HighscoreTraining) {
    val list = getHighscoreTrainingsList()
    list.add(training)
    saveHighscoreTrainingsList(list)
    calculateGamePoints(training.ppdAvarage().toFloat())
  }

  private fun saveHighscoreTrainingsList(list: ArrayList<HighscoreTraining>) {
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    val jsonString = Gson().toJson(list)
    preferences.edit().putBoolean("hs_checked", true).apply()
    preferences.edit().putString("hs-training", jsonString).apply()
    hsList = list
  }

  fun getHighscoreTrainingsList(): ArrayList<HighscoreTraining> {
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    if (preferences != null) {
      val json = preferences.getString("hs-training", null)
      if (json != null) {
        val turnsType = object : TypeToken<List<HighscoreTraining>>() {}.type
        return Gson().fromJson<ArrayList<HighscoreTraining>>(json, turnsType)
      }
    }
    return ArrayList()
  }

  fun addXXTraining(training: XXTraining) {
    val list = getXXTrainingsList()
    list.add(training)
    saveXXTrainingsList(list)
    val points = training.hitPercentage() * 55.67
    calculateGamePoints(points.toFloat())
  }

  private fun saveXXTrainingsList(list: ArrayList<XXTraining>) {
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    val jsonString = Gson().toJson(list)
    preferences.edit().putBoolean("xx_checked", true).apply()
    preferences.edit().putString("xx-training", jsonString).apply()
    xxList = list
  }

  fun getXXTrainingsList(): ArrayList<XXTraining> {
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    if (preferences != null) {
      val json = preferences.getString("xx-training", null)
      if (json != null) {
        val turnsType = object : TypeToken<List<XXTraining>>() {}.type
        return Gson().fromJson<ArrayList<XXTraining>>(json, turnsType)
      }
    }
    return ArrayList()
  }

  fun addATCTraining(training: AroundTheClockTraining) {
    val list = getATCTrainingsList()
    list.add(training)
    saveATCTrainingsList(list)
    calculateGamePoints((training.hitRate() * 55.67).toFloat())
  }

  private fun saveATCTrainingsList(list: ArrayList<AroundTheClockTraining>) {
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    val jsonString = Gson().toJson(list)
    preferences.edit().putBoolean("atc_checked", true).apply()
    preferences.edit().putString("atc-training", jsonString).apply()
    atcList = list
  }

  fun getATCTrainingsList(): ArrayList<AroundTheClockTraining> {
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    if (preferences != null) {
      val json = preferences.getString("atc-training", null)
      if (json != null) {
        val turnsType = object : TypeToken<List<XXTraining>>() {}.type
        return Gson().fromJson<ArrayList<AroundTheClockTraining>>(json, turnsType)
      }
    }
    return ArrayList()
  }

  fun addRandomTraining(training: RandomTraining) {
    val list = getRandomTrainingsList()
    list.add(training)
    saveRandomTrainingsList(list)
    val points = training.hitPercentage() * 55.67
    calculateGamePoints(points.toFloat())
  }

  private fun saveRandomTrainingsList(list: ArrayList<RandomTraining>) {
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    val jsonString = Gson().toJson(list)
    preferences.edit().putBoolean("random_checked", true).apply()
    preferences.edit().putString("random-training", jsonString).apply()
    randomList = list
  }

  fun getRandomTrainingsList(): ArrayList<RandomTraining> {
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    if (preferences != null) {
      val json = preferences.getString("random-training", null)
      if (json != null) {
        val turnsType = object : TypeToken<List<XXTraining>>() {}.type
        return Gson().fromJson<ArrayList<RandomTraining>>(json, turnsType)
      }
    }
    return ArrayList()
  }

  fun getGameCount(): Int {
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    if (preferences != null) {
      return preferences.getInt("game_count", 0)
    }
    return -1
  }

  fun increaseGameCount() {
    val newGameCount = getGameCount() + 1
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    preferences.edit().putInt("game_count", newGameCount).apply()
  }

  fun shouldShowXOIOverviewHint(): Boolean {
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    if (preferences != null) {
      return preferences.getBoolean("show_xoi_overview", true)
    }

    return true
  }

  fun xOIOverviewHintShown() {
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    preferences.edit().putBoolean("show_xoi_overview", false).apply()
  }

  fun shouldShowXXOverviewHint(): Boolean {
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    if (preferences != null) {
      return preferences.getBoolean("show_xx_overview", true)
    }

    return true
  }

  fun xXOverviewHintShown() {
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    preferences.edit().putBoolean("show_xx_overview", false).apply()
  }

  fun calculateGamePoints(additionalPoints: Float) {
    Log.e("Game points", "$additionalPoints")

    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    if (preferences != null) {
      var points = preferences.getFloat("game-points", 0.0f);
      points += additionalPoints
      Log.e("Game points", "$points")
      preferences.edit().putFloat("game-points", points).apply()

      val country = Locale.getDefault().country
      if (getFirebaseToken() != null) {
        val pointMap = mapOf<String, Double>(getFirebaseToken()!! to points.toDouble())

        FirebaseDatabase.getInstance().getReference("rankings").setValue("test")
        FirebaseDatabase.getInstance().getReference("rankings").child("global").setValue(pointMap)
        FirebaseDatabase.getInstance().getReference("rankings").child(country).setValue(pointMap)
      }
    }
  }

  fun checkPlayedGames() {
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    Log.e("Dataholder", "check")

    val xoiTrainings = getXOITrainingsList()
    if (xoiTrainings.size != 0 && !isXOIChecked()) {
      for (training in xoiTrainings) {
        Log.e("Dataholder", "${training.ppdAvarage()}")
        calculateGamePoints(training.ppdAvarage().toFloat())
        preferences.edit().putBoolean("xoi_checked", true).apply()
      }
    }

    val xxTrainings = getXXTrainingsList()
    if (xxTrainings.size != 0 && !isXXChecked()) {
      for (training in xxTrainings) {
        calculateGamePoints((training.hitPercentage() * 55.67 ).toFloat())
        preferences.edit().putBoolean("xx_checked", true).apply()
      }
    }

    val atcTrainings = getATCTrainingsList()
    if (atcTrainings.size != 0 && !isATCChecked()) {
      for (training in atcTrainings) {
        calculateGamePoints((training.hitRate() * 55.67 ).toFloat())
        preferences.edit().putBoolean("atc_checked", true).apply()
      }
    }

    val hsTrainings = getHighscoreTrainingsList()
    if (hsTrainings.size != 0 && !isHSChecked()) {
      for (training in hsTrainings) {
        calculateGamePoints(training.ppdAvarage().toFloat())
        preferences.edit().putBoolean("hs_checked", true).apply()
      }
    }

    val randomTrainings = getRandomTrainingsList()
    if (randomTrainings.size != 0 && !isRandomChecked()) {
      for (training in randomTrainings) {
        calculateGamePoints((training.hitPercentage() * 55.67 ).toFloat())
        preferences.edit().putBoolean("random_checked", true).apply()
      }
    }
  }

  fun isXOIChecked(): Boolean {
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    if (preferences != null) {
      return preferences.getBoolean("xoi_checked", false)
    }

    return false
  }

  fun isXXChecked(): Boolean {
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    if (preferences != null) {
      return preferences.getBoolean("xx_checked", false)
    }

    return false
  }

  fun isATCChecked(): Boolean {
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    if (preferences != null) {
      return preferences.getBoolean("atc_checked", false)
    }

    return false
  }

  fun isHSChecked(): Boolean {
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    if (preferences != null) {
      return preferences.getBoolean("hs_checked", false)
    }

    return false
  }

  fun isRandomChecked(): Boolean {
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    if (preferences != null) {
      return preferences.getBoolean("random_checked", false)
    }

    return false
  }

  fun setFirebaseToken(token: String?) {

    if (token == null) {
      return
    }

    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    preferences.edit().putString("firebase_token", token).apply()
  }

  fun getFirebaseToken(): String? {
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    if (preferences != null) {
      val token = preferences.getString("firebase_token", "empty_token")
      if (token.equals("empty_token")) {
        val newToken = FirebaseInstanceId.getInstance().token

        setFirebaseToken(newToken)
        return newToken
      }
      return token
    }
    return null
  }
}