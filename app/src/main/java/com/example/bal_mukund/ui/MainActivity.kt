package com.example.bal_mukund.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.example.bal_mukund.SettingsFragment
import com.example.bal_mukund.databinding.ActivityMainBinding
import com.example.bal_mukund.interfaces.LatestPostListener
import com.example.bal_mukund.model.Post
import com.example.bal_mukund.R
import com.example.bal_mukund.ui.fragments.RecentFragment
import com.example.bal_mukund.ui.fragments.TodayFragment
import com.example.bal_mukund.viewmodels.MainViewModel


import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity(), LatestPostListener,
    NavigationView.OnNavigationItemSelectedListener {
    lateinit var binding: ActivityMainBinding
    private val mainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    companion object {
        const val MESSAGE_ARRIVED = "new_message_arrived"
        const val POST_IMAGE_BG = "imagebg"
        const val POST_IMAGE = "image"
        const val POST_TYPE = "type"
        const val POST_TITLE = "title"
        const val POST_BODY = "body"
        const val POST_BUNDLE = "post_bundle"
    }

    private lateinit var todayPost: Post

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout:DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        mainViewModel.getLatestPost(this)
        binding.chipNavigationBar.background = null
        binding.chipNavigationBar[1].isEnabled = false
        binding.chipNavigationBar[2].isEnabled = false
        matchFragmentsWithNavBar()
        mainViewModel.subscribeUsers()
        binding.fab.setOnClickListener {
            openTodayPost()
        }

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
        binding.mainFragmentsContainer.visibility = View.VISIBLE
        binding.mainProgressbar.visibility = View.GONE
        openTodayPost()
    }



    private fun openTodayPost() {
        val todayFragment = TodayFragment().newInstance(todayPost)
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_fragments_container, todayFragment)
            .commit()
        binding.chipNavigationBar.setItemSelected(R.id.chip_nav_recent_item, false)
        binding.chipNavigationBar.setItemSelected(R.id.chip_nav_learn_more_item, false)
    }

    private fun openRecentQuotes(transaction:FragmentTransaction){
        transaction.replace(
            R.id.main_fragments_container,
            RecentFragment()
        ).commit()
    }

    private fun openSettings(transaction: FragmentTransaction){
        transaction.replace(
            R.id.main_fragments_container,
            SettingsFragment()
        ).commit()
    }

    private fun initViews(){
        drawerLayout = findViewById(R.id.drawer)
        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        binding.navigationView.setNavigationItemSelectedListener(this)
        binding.navMenuBtn.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (toggle.onOptionsItemSelected(item))
            true
        else
            super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val transaction = supportFragmentManager.beginTransaction()

        when(item.itemId){
            R.id.nav_quote_item -> {
                openTodayPost()
                drawerLayout.close()
            }
            R.id.nav_recent_quotes_item -> {
                openRecentQuotes(transaction)
                drawerLayout.close()
            }
            R.id.nav_meditation_videos_item -> {
                  Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_about_us_item -> {
                startActivity(Intent(this,AboutUsActivity::class.java))
            }
            R.id.nav_connect_item -> {
                startActivity(Intent(this,ConnectActivity::class.java))
            }
            R.id.nav_settings_item -> {
                openSettings(transaction)
                drawerLayout.close()
            }

            R.id.nav_close_app_item -> {

            }
        }
        return true
    }


}