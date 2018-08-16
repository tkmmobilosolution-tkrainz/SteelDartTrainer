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

class DetailAchievementFragment(): Fragment() {

    private lateinit var achievementList: ListView
    private lateinit var bannerAdView: AdView

    private lateinit var dataholder: DataHolder

    private val className = "detail_achievement"

    var achievements: ArrayList<Achievement> = arrayListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.achivement_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bannerAdView = view.findViewById(R.id.achivementBanner)
        achievementList = view.findViewById(R.id.achivementList)
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
                if (activity != null) {
                    LogEventsHelper(activity).logBannerFailed(className, errorCode)
                }
            }

            override fun onAdOpened() {
                if (activity != null) {
                    bannerAdView.loadAd(adRequest)
                    LogEventsHelper(activity).logBannerOpened(className)
                }
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
        achievementList.adapter = AchievementListAdapter(activity, checkAchievmentModiefied(achievements))
    }

    fun checkAchievmentModiefied(achievements: ArrayList<Achievement>): ArrayList<Achievement> {
        var model = AchievementModel(activity)

        for (achievement in achievements) {

            if (!achievement.successful) {
                when (achievement.type) {
                    AchievementType.DART_AMOUNT -> {
                        val amount = achievement.amount
                        if (amount <= model.getDartsFromTrainings()) {
                            achievement.successful = true
                        }
                        achievement.currentAmount = model.getDartsFromTrainings()
                    }
                    AchievementType.SIXTY_PLUS -> {
                        val amount = achievement.amount
                        if (amount <= model.countSixtyPlus()) {
                            achievement.successful = true
                        }
                        achievement.currentAmount = model.countSixtyPlus()
                    }
                    AchievementType.HUNDRET_PLUS -> {
                        val amount = achievement.amount
                        if (amount <= model.countHundretPlus()) {
                            achievement.successful = true
                        }
                        achievement.currentAmount = model.countHundretPlus()
                    }
                    AchievementType.HUNDRET_FOURTY_PLUS -> {
                        val amount = achievement.amount
                        if (amount <= model.countHundretFourtyPlus()) {
                            achievement.successful = true
                        }
                        achievement.currentAmount = model.countHundretFourtyPlus()
                    }
                    AchievementType.HUNDRET_EIGHTIES -> {
                        val amount = achievement.amount
                        if (amount <= model.countHundretEighties()) {
                            achievement.successful = true
                        }
                        achievement.currentAmount = model.countHundretEighties()
                    }
                    AchievementType.EXERCISES_DONE -> {
                        val amount = achievement.amount
                        if (amount <= model.countExcersices()) {
                            achievement.successful = true
                        }
                        achievement.currentAmount = model.countExcersices()
                    }
                    AchievementType.DAILY_OPEN -> {
                        val amount = achievement.amount
                        if (amount <= DataHolder(activity).getOpenCount()) {
                            achievement.successful = true
                        }
                        achievement.currentAmount = DataHolder(activity).getOpenCount()
                    }
                }
            }
        }

        return achievements
    }
}