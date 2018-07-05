package sdt.tkm.at.steeldarttrainer.base

import android.app.AlertDialog
import android.app.Fragment
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import sdt.tkm.at.steeldarttrainer.R
import sdt.tkm.at.steeldarttrainer.statistics.StatisticsActivity
import sdt.tkm.at.steeldarttrainer.training.TrainingsOverViewFragment

/**
 * [Add class description here]
 *
 * Created 05.03.18
 *
 * @author Thomas Krainz-Mischitz (Level1 GmbH)
 * @version %I%, %G%
 */
class OverviewActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
  private lateinit var toggle: ActionBarDrawerToggle
  private lateinit var mActionBar: ActionBar
  var isDialogShown = false
  var isDialogBackPressed = false
  private var mToolBarNavigationListenerIsRegistered = false
  private lateinit var dataholder: DataHolder
  private lateinit var logHelper: LogEventsHelper

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.nav_drawer)

    dataholder = DataHolder(this)
    logHelper = LogEventsHelper(this)

    dataholder.checkPlayedGames()

      if (dataholder.shouldShowRatingDialog() && !dataholder.hasRated()) {
          val inflater = this.layoutInflater
          val dialogHintBuilder = AlertDialog.Builder(
                  this)
          val finishDialogView = inflater.inflate(R.layout.multiple_button_dialog, null)
          val dialogTitle = finishDialogView.findViewById<TextView>(R.id.dialogTitle)
          dialogTitle.text = this.getString(R.string.like_dialog_title)
          val dialogText = finishDialogView.findViewById<TextView>(R.id.dialogText)
          dialogText.text = this.getString(R.string.like_dialog_text)
          val rateButton = finishDialogView.findViewById<Button>(R.id.newGameButton)
          rateButton.text = this.getString(R.string.like_dialog_rate)
          val closeButton = finishDialogView.findViewById<Button>(R.id.closeButton)
          closeButton.text = this.getString(R.string.like_dialog_cancel)

          dialogHintBuilder.setView(finishDialogView)
          val finishDialog = dialogHintBuilder.create()

          rateButton.setOnClickListener {
              finishDialog.dismiss()
              dataholder.hadRated()
              this.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=sdt.tkm.at.steeldarttrainer")))
              logHelper.logButtonTap("like_dialog_rate")
          }

          closeButton.setOnClickListener {
              finishDialog.dismiss()
              logHelper.logButtonTap("like_dialog_cancel")
              isDialogShown = false
          }

          finishDialog.setCancelable(false)
          finishDialog.setCanceledOnTouchOutside(false)

          logHelper.logButtonTap("like_dialog_show")
          isDialogShown = true
          finishDialog.show()
      }

    val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
    setSupportActionBar(toolbar)
    mActionBar = supportActionBar!!
    val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
    toggle = ActionBarDrawerToggle(
        this, drawer, toolbar, R.string.navigation_drawer_open,
        R.string.navigation_drawer_close)
    drawer.addDrawerListener(toggle)
    toggle.syncState()
    val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
    navigationView.setNavigationItemSelectedListener(this)

    replaceFragment(OverviewFragment())

    navigationView.menu.getItem(0).isChecked = true
  }

  override fun onBackPressed() {
    val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START)
    }
    var count = fragmentManager.backStackEntryCount

    if (count != 0 && !isDialogShown) {
      if (fragmentManager.findFragmentByTag("Detail_Training") != null) {
        if (!isDialogBackPressed) {
          isDialogShown = true
          val inflater = this.layoutInflater
          val dialogHintBuilder = AlertDialog.Builder(
              this)
          val finishDialogView = inflater.inflate(R.layout.multiple_button_dialog, null)
          val dialogTitle = finishDialogView.findViewById<TextView>(R.id.dialogTitle)
          dialogTitle.text = this.getString(R.string.general_hint)
          val dialogText = finishDialogView.findViewById<TextView>(R.id.dialogText)
          dialogText.text = this.getString(R.string.dialog_finish_training_text)
          val exitButton = finishDialogView.findViewById<Button>(R.id.newGameButton)
          exitButton.text = this.getString(R.string.dialog_finish_training_finish)
          val closeButton = finishDialogView.findViewById<Button>(R.id.closeButton)
          closeButton.text = this.getString(R.string.dialog_finish_training_close)

          dialogHintBuilder.setView(finishDialogView)
          val finishDialog = dialogHintBuilder.create()

          exitButton.setOnClickListener {
            finishDialog.dismiss()
            isDialogShown = false
            logHelper.logButtonTap("training_dialog_exit")
            showUpButton(false)
            fragmentManager.popBackStack("Training", 0)
          }

          closeButton.setOnClickListener {
            finishDialog.dismiss()
            logHelper.logButtonTap("training_dialog_close")
            isDialogShown = false
          }

          finishDialog.setCancelable(false)
          finishDialog.setCanceledOnTouchOutside(false)
          finishDialog.show()
          return
        } else {
          showUpButton(false)
          fragmentManager.popBackStack("Training", 0)
          isDialogBackPressed = false
        }
      } else if (fragmentManager.findFragmentByTag("Statistics") != null) {
        // Do nothing
      } else {
        fragmentManager.popBackStack()
      }
    }
  }

  override fun onNavigationItemSelected(item: MenuItem): Boolean {
    val id = item.itemId

    when (id) {
      R.id.nav_home -> {
        replaceFragment(OverviewFragment())
        logHelper.logMenuClick("home")
      }
      R.id.nav_exercises -> {
        replaceFragment(TrainingsOverViewFragment())
        logHelper.logMenuClick("exercises")
      }
      R.id.nav_statistics -> {
        replaceFragment(StatisticsActivity())
        logHelper.logMenuClick("statistics")
      }
      R.id.nav_rate -> {
        this.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=sdt.tkm.at.steeldarttrainer")))
        logHelper.logMenuClick("rate")
      }
      R.id.nav_tos -> {
        replaceFragment(PrivacyPolicyFragment())
        logHelper.logMenuClick("privacy_policy")
      }
    }
    val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
    drawer.closeDrawer(GravityCompat.START)
    return true
  }

  private fun replaceFragment(fragment: Fragment) {
    val transaction = fragmentManager.beginTransaction()

    if (fragment is TrainingsOverViewFragment) {
      transaction.addToBackStack("Training")
      transaction.replace(R.id.content_frame, fragment)
    } else if (fragment is StatisticsActivity) {
      transaction.replace(R.id.content_frame, fragment, "Statistics")
    } else {
      transaction.replace(R.id.content_frame, fragment)
    }

    transaction.commit()
    showUpButton(false)
  }

  fun showUpButton(show: Boolean) {
    if (show) {
      toggle.isDrawerIndicatorEnabled = false
      mActionBar.setDisplayHomeAsUpEnabled(true)
      if (!mToolBarNavigationListenerIsRegistered) {
        toggle.toolbarNavigationClickListener = View.OnClickListener { onBackPressed() }

        mToolBarNavigationListenerIsRegistered = true
      }
    } else {
      mActionBar.setDisplayHomeAsUpEnabled(false)
      toggle.isDrawerIndicatorEnabled = true
      toggle.toolbarNavigationClickListener = null
      mToolBarNavigationListenerIsRegistered = false
    }
  }
}