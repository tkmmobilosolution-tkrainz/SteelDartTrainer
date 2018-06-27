package sdt.tkm.at.steeldarttrainer.base

import android.app.Application
import android.content.Context
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import sdt.tkm.at.steeldarttrainer.models.AroundTheClockTraining
import sdt.tkm.at.steeldarttrainer.models.HighscoreTraining
import sdt.tkm.at.steeldarttrainer.models.RandomTraining
import sdt.tkm.at.steeldarttrainer.models.XOITraining
import sdt.tkm.at.steeldarttrainer.models.XXTraining

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
  }

  private fun saveXOITrainingsList(list: ArrayList<XOITraining>) {
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    val jsonString = Gson().toJson(list)
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
  }

  private fun saveHighscoreTrainingsList(list: ArrayList<HighscoreTraining>) {
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    val jsonString = Gson().toJson(list)
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
  }

  private fun saveXXTrainingsList(list: ArrayList<XXTraining>) {
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    val jsonString = Gson().toJson(list)
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
  }

  private fun saveATCTrainingsList(list: ArrayList<AroundTheClockTraining>) {
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    val jsonString = Gson().toJson(list)
    preferences.edit().putString("xx-training", jsonString).apply()
    atcList = list
  }

  fun getATCTrainingsList(): ArrayList<AroundTheClockTraining> {
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    if (preferences != null) {
      val json = preferences.getString("xx-training", null)
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
  }

  private fun saveRandomTrainingsList(list: ArrayList<RandomTraining>) {
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    val jsonString = Gson().toJson(list)
    preferences.edit().putString("xx-training", jsonString).apply()
    randomList = list
  }

  fun getRandomTrainingsList(): ArrayList<RandomTraining> {
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    if (preferences != null) {
      val json = preferences.getString("xx-training", null)
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
}