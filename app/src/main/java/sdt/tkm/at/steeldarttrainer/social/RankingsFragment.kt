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

class RankingsFragment: Fragment() {

    private lateinit var rankingsTextView: TextView
    private lateinit var rankingsList: ListView
    private lateinit var bannerAdView: AdView

    private lateinit var dataholder: DataHolder

    private val className = "rankings"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_rankings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rankingsTextView = view.findViewById(R.id.rankingsTextView)
        rankingsList = view.findViewById(R.id.rankingsList)
        bannerAdView = view.findViewById(R.id.rankingsBanner)

        dataholder = DataHolder(activity)

        val sortListener: DataHolder.SortingListener = object : DataHolder.SortingListener {
            override fun success(users: MutableList<RankingsUser>) {
                Log.e("Databse Users: ", "" + users.size)

                var count = 0

                setupListView(users)

                for (user in users) {
                    count++
                    if (user.uid == dataholder.getUUID()) {
                        setUserRankings(count, user.rankingPoints)
                        break
                    }

                    if (count == users.size) {
                        rankingsTextView.visibility = View.VISIBLE
                        rankingsList.visibility = View.VISIBLE
                        rankingsTextView.text = "You must finish a excercise to be ranked."
                    }
                }

                Log.e("position: ", "" + count)
            }

            override fun canceled() {
                Log.e("Databse Users: ", "Error")
            }
        }

        dataholder.orderedUsers(sortListener)
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

    private fun setupListView(users: MutableList<RankingsUser>) {
        rankingsList.adapter = RankingsListAdapter(activity, users)
    }

    private fun setUserRankings(position: Int, score: Double) {
        rankingsTextView.visibility = View.VISIBLE
        rankingsList.visibility = View.VISIBLE
        rankingsTextView.text = String.format(getString(R.string.rankings_worldwide), position, score)

        if (rankingsList.firstVisiblePosition > position || rankingsList.lastVisiblePosition < position) {
            rankingsList.setSelection(position - 3)
        }
    }
}