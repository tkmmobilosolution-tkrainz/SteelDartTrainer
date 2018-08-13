package sdt.tkm.at.steeldarttrainer.social

import android.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import sdt.tkm.at.steeldarttrainer.R
import sdt.tkm.at.steeldarttrainer.base.DataHolder
import sdt.tkm.at.steeldarttrainer.base.LogEventsHelper
import sdt.tkm.at.steeldarttrainer.base.RankingsUser

class AchivementsFragment: Fragment() {

    private lateinit var achivementList: ListView
    private lateinit var bannerAdView: AdView

    private lateinit var dataholder: DataHolder

    private val className = "achivement"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.achivement_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bannerAdView = view.findViewById(R.id.achivementBanner)
        achivementList = view.findViewById(R.id.achivementList)
        setupListView()
    }

    override fun onResume() {
        super.onResume()

        val adRequest = AdRequest.Builder().build()
        bannerAdView.loadAd(adRequest)

        bannerAdView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                if (activity != null) {
                    LogEventsHelper(activity).logBannerLoaded(className)
                }
            }

            override fun onAdFailedToLoad(errorCode: Int) {
                LogEventsHelper(activity).logBannerFailed(className, errorCode)
            }

            override fun onAdOpened() {
                bannerAdView.loadAd(adRequest)
                LogEventsHelper(activity).logBannerOpened(className)
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

    private fun setupListView() {
        val model = AchivementModel(activity)
        val checkedAchivements = model.checkAchevements()
        achivementList.adapter = AchivementListAdapter(activity, checkedAchivements)
    }
}