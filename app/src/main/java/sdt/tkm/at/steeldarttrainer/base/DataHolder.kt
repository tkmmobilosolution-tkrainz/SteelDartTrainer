package sdt.tkm.at.steeldarttrainer.base

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
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
  val preferences = PreferenceManager.getDefaultSharedPreferences(context) 

  fun addXOITraining(training: XOITraining) {
    val list = getXOITrainingsList()
    list.add(training)
    saveXOITrainingsList(list)
    calculateGamePoints(training.ppdAvarage().toFloat())

    val bundle = Bundle()
    bundle.putString("finished_training", "xoi")
    LogEventsHelper(context).logEvent("training_finished_xoi", bundle)
  }

  private fun saveXOITrainingsList(list: ArrayList<XOITraining>) {
    val jsonString = Gson().toJson(list)
    preferences.edit().putBoolean("xoi_checked", true).apply()
    preferences.edit().putString("xoi-training", jsonString).apply()
    xoiList = list
  }

  fun getXOITrainingsList(): ArrayList<XOITraining> {
    
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
    val bundle = Bundle()
    bundle.putString("finished_training", "hs")
    LogEventsHelper(context).logEvent("training_finished_hs", bundle)
  }

  private fun saveHighscoreTrainingsList(list: ArrayList<HighscoreTraining>) {
    val jsonString = Gson().toJson(list)
    preferences.edit().putBoolean("hs_checked", true).apply()
    preferences.edit().putString("hs-training", jsonString).apply()
    hsList = list
  }

  fun getHighscoreTrainingsList(): ArrayList<HighscoreTraining> {
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
    val bundle = Bundle()
    bundle.putString("finished_training", "xx")
    LogEventsHelper(context).logEvent("training_finished_xx", bundle)
  }

  private fun saveXXTrainingsList(list: ArrayList<XXTraining>) {
    
    val jsonString = Gson().toJson(list)
    preferences.edit().putBoolean("xx_checked", true).apply()
    preferences.edit().putString("xx-training", jsonString).apply()
    xxList = list
  }

  fun getXXTrainingsList(): ArrayList<XXTraining> {
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
    val bundle = Bundle()
    bundle.putString("finished_training", "atc")
    LogEventsHelper(context).logEvent("training_finished_atc", bundle)
  }

  private fun saveATCTrainingsList(list: ArrayList<AroundTheClockTraining>) {
    val jsonString = Gson().toJson(list)
    preferences.edit().putBoolean("atc_checked", true).apply()
    preferences.edit().putString("atc-training", jsonString).apply()
    atcList = list
  }

  fun getATCTrainingsList(): ArrayList<AroundTheClockTraining> {
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
    val bundle = Bundle()
    bundle.putString("finished_training", "random")
    LogEventsHelper(context).logEvent("training_finished_random", bundle)
  }

  private fun saveRandomTrainingsList(list: ArrayList<RandomTraining>) {
    val jsonString = Gson().toJson(list)
    preferences.edit().putBoolean("random_checked", true).apply()
    preferences.edit().putString("random-training", jsonString).apply()
    randomList = list
  }

  fun getRandomTrainingsList(): ArrayList<RandomTraining> {
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
    if (preferences != null) {
      return preferences.getInt("game_count", 0)
    }
    return -1
  }

  fun increaseGameCount() {
    val newGameCount = getGameCount() + 1
    preferences.edit().putInt("game_count", newGameCount).apply()
  }

  fun shouldShowXOIOverviewHint(): Boolean {
    if (preferences != null) {
      return preferences.getBoolean("show_xoi_overview", true)
    }

    return true
  }

  fun xOIOverviewHintShown() {
    preferences.edit().putBoolean("show_xoi_overview", false).apply()
  }

  fun shouldShowXXOverviewHint(): Boolean {
    if (preferences != null) {
      return preferences.getBoolean("show_xx_overview", true)
    }

    return true
  }

  fun xXOverviewHintShown() {
    preferences.edit().putBoolean("show_xx_overview", false).apply()
  }

  fun calculateGamePoints(additionalPoints: Float) {
    Log.e("Game points", "$additionalPoints")

    if (preferences != null) {
      var points = preferences.getFloat("game-points", 0.0f);
      points += additionalPoints
      Log.e("Game points", "$points")
      preferences.edit().putFloat("game-points", points).apply()

      val country = Locale.getDefault().country
      if (getFirebaseToken() != null) {

        var userId = getUUID()
        if (userId == null) {
          genereateUUID()
          userId = getUUID()!!
        }

        val user = RankingsUser(userId, points.toDouble(), country)

        FirebaseDatabase.getInstance().getReference("user_rankings").child("global").child(userId).setValue(user)
        FirebaseDatabase.getInstance().getReference("user_rankings").child(country).child(userId).setValue(user)
      }
    }
  }

  fun genereateUUID() {
    if (getUUID() == null) {
      preferences.edit().putString("generated_uuid", UUID.randomUUID().toString()).apply()
    }
  }

  fun getUUID(): String? {
    return preferences.getString("generated_uuid", null)
  }

  fun checkPlayedGames() {

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
    if (preferences != null) {
      return preferences.getBoolean("xoi_checked", false)
    }

    return false
  }

  fun isXXChecked(): Boolean {
    if (preferences != null) {
      return preferences.getBoolean("xx_checked", false)
    }

    return false
  }

  fun isATCChecked(): Boolean {
    if (preferences != null) {
      return preferences.getBoolean("atc_checked", false)
    }

    return false
  }

  fun isHSChecked(): Boolean {
    if (preferences != null) {
      return preferences.getBoolean("hs_checked", false)
    }

    return false
  }

  fun isRandomChecked(): Boolean {
    if (preferences != null) {
      return preferences.getBoolean("random_checked", false)
    }

    return false
  }

  fun setFirebaseToken(token: String?) {

    if (token == null) {
      return
    }

    preferences.edit().putString("firebase_token", token).apply()
  }

  fun getFirebaseToken(): String? {
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

  fun shouldShowRatingDialog(): Boolean {
    if (preferences != null) {
      var starts = preferences.getInt("appStarts", 0)
      starts += 1
      preferences.edit().putInt("appStarts", starts).apply()

      if (starts % 5 == 0) {
        return true
      }
    }
    return false
  }

  fun hasRated(): Boolean {
    if (preferences != null) {
      return preferences.getBoolean("has_rated", false)
    }
    return false
  }

  fun hadRated() {
    preferences.edit().putBoolean("has_rated", true).apply()
  }
}