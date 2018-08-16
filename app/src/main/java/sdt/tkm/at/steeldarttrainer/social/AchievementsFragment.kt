package sdt.tkm.at.steeldarttrainer.social

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import sdt.tkm.at.steeldarttrainer.R
import sdt.tkm.at.steeldarttrainer.base.DataHolder
import sdt.tkm.at.steeldarttrainer.base.LogEventsHelper
import sdt.tkm.at.steeldarttrainer.base.OverviewActivity

class AchievementsFragment: Fragment() {

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

        setupListView()
    }

    private fun setupListView() {
        val model = AchievementModel(activity)
        val achievements: ArrayList<Achievement> = arrayListOf()
        model.checkAchievements()
        achievements.add(model.getFirstLockedPlayedGamesAchievement())
        achievements.add(model.getFirstLockedDartAchievement())
        achievements.add(model.getFirstLockedSixtyAchievement())
        achievements.add(model.getFirstLockedHundretAchievement())
        achievements.add(model.getFirstLockedHundretFourtyAchievement())
        achievements.add(model.getFirstLockedHundretEightyAchievement())
        achievements.add(model.getFirstLockedDailyOpenAchievement())
        achivementList.adapter = AchievementListAdapter(activity, achievements)
        achivementList.setOnItemClickListener { parent, view, position, id ->

            var listToShow: ArrayList<Achievement> = arrayListOf()
            when (position) {
                0 -> {
                    listToShow = model.playedGamesAchievements()
                }
                1 -> {
                    listToShow = model.dartsAchievements()
                }
                2 -> {
                    listToShow = model.sixtyAchievements()
                }
                3 -> {
                    listToShow = model.hundretAchievements()
                }
                4 -> {
                    listToShow = model.hundretFourtyAchievements()
                }
                5 -> {
                    listToShow = model.hundretEightyAchievements()
                }
                6 -> {
                    listToShow = model.dailyOpenAchievements()
                }
            }

            LogEventsHelper(activity).logAchievementClicked(position)
            val fragment = DetailAchievementFragment()
            fragment.achievements = listToShow
            replaceFragment(fragment)
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.content_frame, fragment, "Detail_Achievement")
        transaction.addToBackStack(null)
        transaction.commit()
        val overviewActivity = activity as OverviewActivity
        overviewActivity.showUpButton(true)
    }
}