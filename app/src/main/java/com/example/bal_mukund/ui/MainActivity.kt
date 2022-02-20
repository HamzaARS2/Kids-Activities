package com.example.bal_mukund.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import com.example.bal_mukund.SettingsFragment
import com.example.bal_mukund.databinding.ActivityMainBinding
import com.example.bal_mukund.interfaces.LatestPostListener
import com.example.bal_mukund.model.Post
import com.example.bal_mukund.R
import com.example.bal_mukund.ui.fragments.RecentFragment
import com.example.bal_mukund.ui.fragments.TodayFragment
import com.example.bal_mukund.viewmodels.MainViewModel
import com.yarolegovich.slidingrootnav.SlidingRootNav


import androidx.recyclerview.widget.RecyclerView
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
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
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
                R.id.settings_item -> {
                    transaction.replace(
                        R.id.main_fragments_container,
                        SettingsFragment()
                    )
                }
                R.id.recent_item -> transaction.replace(
                    R.id.main_fragments_container,
                    RecentFragment()
                )
            }
            transaction.commit()
        }

    }

    override fun onTodayPostReceived(post: Post) {
        todayPost = post
        Toast.makeText(this, "activity = ${post.topic}", Toast.LENGTH_SHORT).show()
        binding.mainFragmentsContainer.visibility = View.VISIBLE
        binding.mainProgressbar.visibility = View.GONE
        openTodayPost()
    }

    private fun openTodayPost() {
        val todayFragment = TodayFragment().newInstance(todayPost)
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_fragments_container, todayFragment)
            .commit()
        binding.chipNavigationBar.setItemSelected(R.id.recent_item, false)
        binding.chipNavigationBar.setItemSelected(R.id.settings_item, false)
    }

    private fun initViews(){
//        setSupportActionBar(binding.toolbar)
//        binding.toolbar.title = ""


        drawerLayout = findViewById(R.id.drawer)
        toggle = ActionBarDrawerToggle(this,drawerLayout,binding.toolbar,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        binding.navigationView.setNavigationItemSelectedListener(this)
        binding.toolbar.setNavigationIcon(R.drawable.ic_nav_menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (toggle.onOptionsItemSelected(item))
            true
        else
            super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        //val transaction = supportFragmentManager.beginTransaction()

        when(item.itemId){
            R.id.nav_item_one -> {
                //openTodayPost()
                Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show()

            }
            R.id.nav_item_two -> {
//                transaction.replace(
//                    R.id.main_fragments_container,
//                    SettingsFragment()
//                )
                Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show()

            }
            R.id.nav_item_three -> {
//                transaction.replace(
//                    R.id.main_fragments_container,
//                    RecentFragment()
//                )
                Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show()
            }
        }
       // transaction.commit()
        return true
    }


}