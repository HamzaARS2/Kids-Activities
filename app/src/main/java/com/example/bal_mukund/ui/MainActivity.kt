package com.example.bal_mukund.ui

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
import com.example.bal_mukund.databinding.ActivityMainBinding
import com.example.bal_mukund.interfaces.LatestPostListener
import com.example.bal_mukund.model.Post
import com.example.bal_mukund.R
import com.example.bal_mukund.interfaces.RecentPostListener
import com.example.bal_mukund.ui.fragments.*
import com.example.bal_mukund.viewmodels.MainViewModel


import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity(), LatestPostListener,
    NavigationView.OnNavigationItemSelectedListener,TodayFragment.PostLoadingCompleteListener,
    View.OnClickListener,SettingsFragment.NotificationsListener,RecentPostListener {
    lateinit var binding: ActivityMainBinding
    private val mainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }



    private lateinit var todayPost: Post

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private var todayFragmentVisible = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
            .commit()
        binding.chipNavigationBar.setItemSelected(R.id.chip_nav_recent_item, false)
        binding.chipNavigationBar.setItemSelected(R.id.chip_nav_learn_more_item, false)
        binding.fab.setImageResource(R.drawable.ic_share)
        todayFragmentVisible = true
    }

    private fun openRecentQuotes(transaction: FragmentTransaction) {
        transaction.replace(
            R.id.main_fragments_container,
            RecentFragment()
        ).commit()

        binding.fab.setImageResource(R.drawable.ic_quote)
        todayFragmentVisible = false
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    private fun openSettings(transaction: FragmentTransaction) {
        transaction.replace(
            R.id.main_fragments_container,
            SettingsFragment()
        ).commit()
        binding.fab.setImageResource(R.drawable.ic_quote)
        todayFragmentVisible = false
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    private fun openSocialMediaConnect(transaction: FragmentTransaction){
        transaction.replace(
            R.id.main_fragments_container,
            ConnectFragment()
        ).commit()
        binding.fab.setImageResource(R.drawable.ic_quote)
        todayFragmentVisible = false
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    private fun initViews() {
        drawerLayout = findViewById(R.id.drawer)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
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

        when (item.itemId) {
            R.id.nav_quote_item -> {
                openTodayPost()
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            R.id.nav_recent_quotes_item -> {
                openRecentQuotes(transaction)

            }
            R.id.nav_meditation_videos_item -> {
                Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_about_us_item -> {
                startActivity(Intent(this, AboutUsActivity::class.java))
            }
            R.id.nav_connect_item -> {
                openSocialMediaConnect(transaction)
            }
            R.id.nav_settings_item -> {
                openSettings(transaction)
            }
            R.id.nav_review_app_item ->{
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
           val intent = Intent(this,ShareActivity::class.java)
            intent.putExtra(ShareActivity.SHARE_POST_CODE,todayPost)
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

    private fun showPlayStorePage(){
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
            getSharedPreferences(SettingsFragment.NIGHT_MODE_STATE, MODE_PRIVATE)
        SettingsFragment.NIGHT_MODE = sp.getBoolean(SettingsFragment.NIGHT_MODE_STATE, false)
        if (SettingsFragment.NIGHT_MODE){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)}
    }

    private fun sendNotificationsIsChecked(){
        val sp = getSharedPreferences(SettingsFragment.NOTIFICATIONS_STATE, MODE_PRIVATE)
        val topic = sp.getString(SettingsFragment.NOTIFICATIONS_STATE,"true")
        if (topic != null) {
            mainViewModel.subscribeUsers(topic)
        }
    }

    override fun onNotificationsStateChanged(isChecked: String) {
        if (isChecked == "true"){
            mainViewModel.subscribeUsers(isChecked)
        }else
            mainViewModel.unSubscribeUsers()

    }

    override fun onPostClicked(post: Post, image: ImageView, quoteTv:TextView) {
//        val image:ImageView = findViewById(R.id.post_recent_item_imageView)
//        val quote:TextView = findViewById(R.id.post_recent_item_tv)
//
//        val fragment = SharedElementsFragment.newInstance(post)
//        val transaction = supportFragmentManager.beginTransaction()
//        transaction.apply {
//            setReorderingAllowed(true)
//            addToBackStack(null)
//            replace(R.id.main_fragments_container,fragment)
//            addSharedElement(image,post.id)
//
//        }
//        transaction.commit()
        val intent = Intent(this,SharedRecentPostActivity::class.java)
        intent.putExtra("recentPost",post)

        val pair:Pair<View,String> = Pair.create(image as View,"imageTN")
        val pair2:Pair<View,String> = Pair.create(quoteTv as View,"quoteTN")
        val pair3:Pair<View,String> = Pair.create(binding.fab as View,"fabTN")

        val option = ActivityOptionsCompat.makeSceneTransitionAnimation(this,pair,pair2,pair3)
        startActivity(intent,option.toBundle())
    }


}