package sdt.tkm.at.steeldarttrainer.base

import android.app.Fragment
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
import sdt.tkm.at.steeldarttrainer.R
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
    open var isDialogShown = false
    var mToolBarNavigationListenerIsRegistered = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nav_drawer)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        mActionBar = supportActionBar!!

        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        toggle = ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open,
            R.string.navigation_drawer_close)
        drawer.setDrawerListener(toggle)
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
            fragmentManager.popBackStack()

            count -= 1
            if (count > 2) {
                showUpButton(true)
            } else {
                showUpButton(false)
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.nav_home) {
            replaceFragment(OverviewFragment())
        } else if (id == R.id.nav_exercises) {
            replaceFragment(TrainingsOverViewFragment())
        }

        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.content_frame, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
        showUpButton(false)
    }

    open fun showUpButton(show: Boolean) {
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