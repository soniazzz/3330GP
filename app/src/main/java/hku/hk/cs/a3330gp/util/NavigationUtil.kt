package hku.hk.cs.a3330gp.util

import android.app.ActivityOptions
import android.content.Intent
import android.content.res.Configuration
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.android.material.transition.platform.MaterialFadeThrough
import hku.hk.cs.a3330gp.CareTakingVideosActivity
import hku.hk.cs.a3330gp.ChatBotActivity
import hku.hk.cs.a3330gp.MainActivity
import hku.hk.cs.a3330gp.R
import hku.hk.cs.a3330gp.ar.AttendanceActivity
import hku.hk.cs.a3330gp.history.HistoryActivity
import hku.hk.cs.a3330gp.map.NavigationActivity

object NavigationUtil {

    fun setupNavigationView(
        navigationView: NavigationView,
        drawerLayout: DrawerLayout,
        activity: AppCompatActivity
    ) {
        navigationView.setNavigationItemSelectedListener { menuItem ->
            handleNavigationItemSelected(menuItem, activity, drawerLayout)
        }
    }

    private fun handleNavigationItemSelected(
        menuItem: MenuItem,
        activity: AppCompatActivity,
        drawerLayout: DrawerLayout
    ): Boolean {
        // Handle menu item selected
        val exit = MaterialFadeThrough().apply {
            addTarget(R.id.constraintlayout1)
        }
        activity.window.exitTransition = exit
        val options = ActivityOptions.makeSceneTransitionAnimation(activity)
        drawerLayout.close()
        if (menuItem.itemId != R.id.nav_home) {
            activity.findViewById<NavigationView>(R.id.navigationView).menu.findItem(R.id.nav_home).isChecked = false
        }

        when (menuItem.itemId) {
            R.id.nav_home -> {
                if (activity.javaClass != MainActivity::class.java) {
                    startHome(activity)
                }
            }
            R.id.nav_navigation -> {
                if (activity.javaClass != NavigationActivity::class.java) {
                    startMap(activity)
                }
            }
            R.id.nav_ar -> {
                if (activity.javaClass != AttendanceActivity::class.java) {
                    startAr(activity)
                }
            }
            R.id.nav_history -> {
                if (activity.javaClass != HistoryActivity::class.java) {
                    startHistory(activity)
                }
            }
            R.id.btnTheme -> switchTheme(activity)
            R.id.btnCareTakingVideo -> {
                if (activity.javaClass != HistoryActivity::class.java) {
                    startCareTakingVideo(activity)
                }
            }
            R.id.btnBot -> {

                if (activity.javaClass != ChatBotActivity::class.java) {
                    startChatBot(activity)
                }
            }
        }

        return true
    }

    private fun startChatBot(activity: AppCompatActivity) {
        val intent = Intent(activity, ChatBotActivity::class.java)
        activity.startActivity(intent)
    }

    fun startHome(activity: AppCompatActivity) {
        val intent = Intent(activity, MainActivity::class.java)
        activity.startActivity(intent)
    }
    fun startMap(activity: AppCompatActivity) {
        val intent = Intent(activity, NavigationActivity::class.java)
        activity.startActivity(intent)
    }

    private fun startAr(activity: AppCompatActivity) {
        val intent = Intent(activity, AttendanceActivity::class.java)
        activity.startActivity(intent)
    }

    private fun startHistory(activity: AppCompatActivity) {
        val intent = Intent(activity, HistoryActivity::class.java)
        activity.startActivity(intent)
    }

    private fun switchTheme(activity: AppCompatActivity) {
        val currentNightMode = activity.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when (currentNightMode) {
            Configuration.UI_MODE_NIGHT_NO -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)} // Night mode is not active, we're using the light theme
            Configuration.UI_MODE_NIGHT_YES -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)} // Night mode is active, we're using dark theme
        }
    }

    private fun startCareTakingVideo(activity: AppCompatActivity) {
        activity.startActivity(Intent(activity, CareTakingVideosActivity::class.java))
    }



}