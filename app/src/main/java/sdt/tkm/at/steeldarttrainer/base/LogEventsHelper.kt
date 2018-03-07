package sdt.tkm.at.steeldarttrainer.base

import android.app.Application
import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import sdt.tkm.at.steeldarttrainer.BuildConfig

/**
 * [Add class description here]
 *
 * Created 23.02.18
 *
 * @author Thomas Krainz-Mischitz (Level1 GmbH)
 * @version %I%, %G%
 */
class LogEventsHelper(val context: Context = Application().applicationContext) {

    private fun logEvent(name: String, bundle: Bundle) {
        if (!BuildConfig.DEBUG) {
            val analytics = FirebaseAnalytics.getInstance(context)
            analytics.logEvent(name, bundle)
        }
    }

    fun logBannerOpened(className: String) {
        val name = "banner_opened"
        val bundle = Bundle()
        bundle.putString("class", className)
        logEvent(name, bundle)
    }

    fun logBannerFailed(className: String, failure: Int) {
        val name = "banner_failed"
        val bundle = Bundle()
        bundle.putString("class", className)
        bundle.putInt("failure", failure)
        logEvent(name, bundle)
    }

    fun logBannerLoaded(className: String) {
        val name = "banner_loaded"
        val bundle = Bundle()
        bundle.putString("class", className)
        logEvent(name, bundle)
    }

    fun logIntersitalOpened(className: String) {
        val name = "intersital_opened"
        val bundle = Bundle()
        bundle.putString("class", className)
        logEvent(name, bundle)
    }

    fun logIntersitalFailed(className: String, failure: Int) {
        val name = "intersital_failed"
        val bundle = Bundle()
        bundle.putString("class", className)
        bundle.putInt("failure", failure)
        logEvent(name, bundle)
    }

    fun logIntersitalLoaded(className: String) {
        val name = "intersital_loaded"
        val bundle = Bundle()
        bundle.putString("class", className)
        logEvent(name, bundle)
    }

    fun logButtonTap(text: String) {
        val name = "button_tap"
        val bundle = Bundle()
        bundle.putString("button", text)
        logEvent(name, bundle)
    }

    fun logMenuClick(text: String) {
        val name = "menu_tap"
        val bundle = Bundle()
        bundle.putString("menu", text)
        logEvent(name, bundle)
    }

    fun logCount(name: String, count: Int) {
        val bundle = Bundle()
        bundle.putInt("count", count)
        logEvent(name, bundle)
    }
}