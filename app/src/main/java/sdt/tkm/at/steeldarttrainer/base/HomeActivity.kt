package sdt.tkm.at.steeldarttrainer.base

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import sdt.tkm.at.steeldarttrainer.R
import sdt.tkm.at.steeldarttrainer.training.TrainingsOverViewActivity
import sdt.tkm.at.steeldarttrainer.statistics.StatisticsActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var bannerAdView: AdView
    private val className = "home"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        MobileAds.initialize(this, getString(R.string.admob_id))

        supportActionBar?.title = getString(R.string.actionbar_title_main)

        val trainingsOverview = findViewById<Button>(R.id.startTraining)
        trainingsOverview.setOnClickListener {
            val intent = Intent(this, TrainingsOverViewActivity::class.java)
            startActivity(intent)
            LogEventsHelper(this).logButtonTap("trainings_overview")
        }

        val showStats = findViewById<Button>(R.id.stats)
        showStats.setOnClickListener {
            val intent = Intent(this, StatisticsActivity::class.java)
            startActivity(intent)
            LogEventsHelper(this).logButtonTap("statistics")
        }
    }

    override fun onResume() {
        super.onResume()

        bannerAdView = findViewById(R.id.mainBanner)
        val adRequest = AdRequest.Builder().build()
        bannerAdView.loadAd(adRequest)

        bannerAdView.adListener = object: AdListener() {
            override fun onAdLoaded() {
                LogEventsHelper(this@HomeActivity).logBannerLoaded(className)
            }

            override fun onAdFailedToLoad(errorCode: Int) {
                bannerAdView.loadAd(adRequest)
                LogEventsHelper(this@HomeActivity).logBannerFailed(className, errorCode)
            }

            override fun onAdOpened() {
                bannerAdView.loadAd(adRequest)
                LogEventsHelper(this@HomeActivity).logBannerOpened(className)
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
}
