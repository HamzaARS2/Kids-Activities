package com.example.kidsactivities.ui

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.example.kidsactivities.databinding.ActivityMainBinding
import com.example.kidsactivities.interfaces.LatestPostListener
import com.example.kidsactivities.model.Post
import com.example.kidsactivities.R
import com.example.kidsactivities.interfaces.FragmentInteraction
import com.example.kidsactivities.interfaces.RecentPostListener
import com.example.kidsactivities.ui.fragments.*
import com.example.kidsactivities.viewmodels.MainViewModel


import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity(), LatestPostListener,
    NavigationView.OnNavigationItemSelectedListener, TodayFragment.PostLoadingCompleteListener,
    View.OnClickListener, RecentPostListener,
    FragmentInteraction {
    lateinit var binding: ActivityMainBinding
    private val mainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }


    private lateinit var todayPost: Post

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private var todayFragmentVisible = true

    companion object {
        const val FIRST_LAUNCH_SP = "isFirstLaunch"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onBoardingIfFirstLaunch()
        initViews()



        mainViewModel.getLatestPost(this)
        binding.chipNavigationBar.background = null
        binding.chipNavigationBar[1].isEnabled = false
        binding.chipNavigationBar[2].isEnabled = false
        matchFragmentsWithNavBar()

        binding.fab.setOnClickListener(this)
        sendNotificationsIsChecked()

    }

    private fun matchFragmentsWithNavBar() {
        binding.chipNavigationBar.setOnItemSelectedListener { itemId ->
            val transaction = supportFragmentManager.beginTransaction()
            when (itemId) {
                R.id.chip_nav_learn_more_item -> {
                    Toast.makeText(this, "Opens Learn More screen", Toast.LENGTH_SHORT).show()
                }
                R.id.chip_nav_recent_item -> {
                    openRecentQuotes(transaction)
                }
            }
        }

    }


    override fun onTodayPostReceived(post: Post) {
        todayPost = post
        openTodayPost()
        checkNightMode()
    }


    private fun openTodayPost() {
        val todayFragment = TodayFragment().newInstance(todayPost)
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_fragments_container, todayFragment)
            .commitAllowingStateLoss()
        binding.chipNavigationBar.setItemSelected(R.id.chip_nav_recent_item, false)
        binding.chipNavigationBar.setItemSelected(R.id.chip_nav_learn_more_item, false)
        binding.fab.setImageResource(R.drawable.ic_share)
        todayFragmentVisible = true

    }

    private fun openRecentQuotes(transaction: FragmentTransaction) {
        transaction.replace(
            R.id.main_fragments_container,
            RecentFragment()
        )
            .addToBackStack(null).commitAllowingStateLoss()

        binding.fab.setImageResource(R.drawable.ic_quote)
        todayFragmentVisible = false
        drawerLayout.closeDrawer(GravityCompat.START)
        binding.chipNavigationBar.setItemSelected(R.id.chip_nav_recent_item,true)
    }

    private fun openSettings() {
        drawerLayout.closeDrawer(GravityCompat.START)
        startActivity(Intent(this, SettingsActivity::class.java))

    }

    private fun openSocialMediaConnect() {
        drawerLayout.closeDrawer(GravityCompat.START)
        startActivity(Intent(this, ConnectActivity::class.java))
    }

    private fun openAboutUsScreen(){
        drawerLayout.closeDrawer(GravityCompat.START)
        startActivity(Intent(this, AboutUsActivity::class.java))

    }

    private fun initViews() {
        drawerLayout = findViewById(R.id.drawer)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        binding.navigationView.setNavigationItemSelectedListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (toggle.onOptionsItemSelected(item))
            true
        else
            super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val transaction = supportFragmentManager.beginTransaction()

        when (item.itemId) {
            R.id.nav_quote_item -> {
                openTodayPost()
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            R.id.nav_recent_quotes_item -> {
                openRecentQuotes(transaction)
            }
            R.id.nav_meditation_videos_item -> {
//                val uri = Uri.parse(resources.getString(R.string.meditation_videos_url))
//                startActivity(Intent(Intent.ACTION_VIEW, uri))
            }
            R.id.nav_about_us_item -> {
                openAboutUsScreen()
            }
            R.id.nav_connect_item -> {
                openSocialMediaConnect()
            }
            R.id.nav_settings_item -> {
                openSettings()
            }
            R.id.nav_review_app_item -> {
                showPlayStorePage()
            }
            R.id.nav_close_app_item -> {
                showDialog()
            }
        }
        return true
    }


    override fun onFullPostLoadingCompleted() {
        binding.coordinator.visibility = View.VISIBLE
        binding.mainFragmentsContainer.visibility = View.VISIBLE
        binding.mainProgressbar.visibility = View.GONE
    }

    override fun onFullPostLoadingFailed(e: Exception) {
        binding.coordinator.visibility = View.VISIBLE
        Toast.makeText(this, "Error : ${e.message.toString()}", Toast.LENGTH_SHORT).show()
    }

    override fun onClick(view: View?) {
        if (todayFragmentVisible) {
            val intent = Intent(this, ShareActivity::class.java)
            intent.putExtra(ShareActivity.SHARE_POST_CODE, todayPost)
            startActivity(intent)
        } else {
            openTodayPost()
        }
    }

    private fun showDialog() {
        val mDialog: AlertDialog

        val dialogInterface = DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> finishAffinity()
                DialogInterface.BUTTON_NEGATIVE -> dialog.dismiss()
            }
        }
        val builder = AlertDialog.Builder(this)
            .setTitle("Exit")
            .setMessage("Are you sure you want to close the app?")
            .apply {
                setPositiveButton("YES", dialogInterface)
                setNegativeButton("NO", dialogInterface)
            }
        mDialog = builder.create()
        mDialog.show()
    }

    private fun showPlayStorePage() {
        val uri = Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        try {
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "Error : ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkNightMode() {
        val sp: SharedPreferences =
            getSharedPreferences(SettingsActivity.NIGHT_MODE_STATE, MODE_PRIVATE)
        SettingsActivity.NIGHT_MODE = sp.getBoolean(SettingsActivity.NIGHT_MODE_STATE, false)
        if (SettingsActivity.NIGHT_MODE) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    private fun sendNotificationsIsChecked() {
        val sp = getSharedPreferences(SettingsActivity.NOTIFICATIONS_STATE, MODE_PRIVATE)
        val topic = sp.getString(SettingsActivity.NOTIFICATIONS_STATE, "true")
        if (topic != null) {
            mainViewModel.subscribeUsers(topic)
        }
    }


    override fun onPostClicked(post: Post, image: ImageView, quoteTv: TextView) {
        val intent = Intent(this, SharedRecentPostActivity::class.java)
        intent.putExtra("recentPost", post)

        val pair: Pair<View, String> = Pair.create(image as View, "imageTN")
        val pair2: Pair<View, String> = Pair.create(quoteTv as View, "quoteTN")
        val pair3: Pair<View, String> = Pair.create(binding.fab as View, "fabTN")

        val option = ActivityOptionsCompat.makeSceneTransitionAnimation(this, pair, pair2, pair3)
        startActivity(intent, option.toBundle())
    }

    override fun onScrolling(scrollY: Int) {
        if (scrollY > 0) {
            binding.bottomAppBar.performHide()
            binding.fab.hide()
        } else {
            binding.bottomAppBar.performShow()
            binding.fab.show()
        }
    }

    override fun onNavIconClicked() {
        drawerLayout.openDrawer(GravityCompat.START)
    }

    private fun onBoardingIfFirstLaunch(){
        val sp = getSharedPreferences(FIRST_LAUNCH_SP, MODE_PRIVATE)
        val isFirstLaunch = sp.getBoolean(FIRST_LAUNCH_SP,true)
        if (isFirstLaunch) startActivity(Intent(this,OnBoardingActivity::class.java))
    }


}