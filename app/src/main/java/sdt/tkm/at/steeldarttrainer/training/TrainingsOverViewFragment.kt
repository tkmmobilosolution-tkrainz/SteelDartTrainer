package sdt.tkm.at.steeldarttrainer.training

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import sdt.tkm.at.steeldarttrainer.R
import sdt.tkm.at.steeldarttrainer.base.DataHolder
import sdt.tkm.at.steeldarttrainer.base.LogEventsHelper
import sdt.tkm.at.steeldarttrainer.base.OverviewActivity
import sdt.tkm.at.steeldarttrainer.dialog.NeutralDialogFragment

/**
 * [Add class description here]
 *
 * Created 21.02.18
 *
 * @author Thomas Krainz-Mischitz (Level1 GmbH)
 * @version %I%, %G%
 */
class TrainingsOverViewFragment : Fragment() {
  private lateinit var bannerAdView: AdView
  private val className = "trainings_overview"
  private lateinit var dataHolder: DataHolder
  private lateinit var oververviewActivity: OverviewActivity

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View {
    oververviewActivity = activity as OverviewActivity

    dataHolder = DataHolder(oververviewActivity)
    val view = inflater.inflate(R.layout.trainings_overview_activity, container, false)

    bannerAdView = view.findViewById(R.id.trainingsOverviewBanner)
    val xoiButton = view.findViewById<Button>(R.id.xoiButton)
    xoiButton.setOnClickListener {
      if (dataHolder.shouldShowXOIOverviewHint()) {
        showInformationDialog(getString(R.string.trainings_overview_xoi_hint_message))
        dataHolder.xOIOverviewHintShown()
      } else {
        replaceFragment(XOITrainingsFragment())
      }
      LogEventsHelper(oververviewActivity).logButtonTap("trainings_overview_xoi")
    }
    val hsButton = view.findViewById<Button>(R.id.highscoreButton)
    hsButton.setOnClickListener {
      replaceFragment(HighscoreTrainingsFragment())
      LogEventsHelper(oververviewActivity).logButtonTap("trainings_overview_hs")
    }
    val xxButton = view.findViewById<Button>(R.id.dartsToXButton)
    xxButton.setOnClickListener {
      replaceFragment(XXTrainingsFragment())
      LogEventsHelper(oververviewActivity).logButtonTap("trainings_overview_xx")
    }
    val aroundClockButton = view.findViewById<Button>(R.id.aroundTheClockButton)
    aroundClockButton.setOnClickListener {
      replaceFragment(AroundTheClockTrainingsFragment())
      LogEventsHelper(oververviewActivity).logButtonTap("trainings_overview_random")
    }
    val randomButton = view.findViewById<Button>(R.id.randomTarget)
    randomButton.setOnClickListener {
      replaceFragment(RandomTrainingFragment())
      LogEventsHelper(oververviewActivity).logButtonTap("trainings_overview_random")
    }

    return view
  }

  override fun onResume() {
    super.onResume()
    val adRequest = AdRequest.Builder().build()
    bannerAdView.loadAd(adRequest)

    bannerAdView.adListener = object : AdListener() {
      override fun onAdLoaded() {
        if (oververviewActivity != null) {
          LogEventsHelper(oververviewActivity).logBannerLoaded(className)
        }
      }

      override fun onAdFailedToLoad(errorCode: Int) {
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

  private fun showInformationDialog(message: String) {
    val neutralDialog = NeutralDialogFragment.Companion.newNeutralDialog(
        getString(R.string.trainings_overview_xoi_hint_title),
        message,
        getString(R.string.trainings_overview_xoi_hint_button_text)
    )

    neutralDialog.listener = object : NeutralDialogFragment.NeutralListener {
      override fun buttonClicked() {
        oververviewActivity.isDialogShown = false
        replaceFragment(XOITrainingsFragment())
        LogEventsHelper(oververviewActivity).logButtonTap("trainings_overview_dialog")
        neutralDialog.dismiss()
      }
    }

    neutralDialog.show(fragmentManager, null)

    oververviewActivity.isDialogShown = true
  }

  private fun replaceFragment(fragment: Fragment) {
    val transaction = fragmentManager.beginTransaction()
    transaction.replace(R.id.content_frame, fragment, "Detail_Training")
    transaction.addToBackStack(null)
    transaction.commit()
    oververviewActivity.showUpButton(true)
  }
}