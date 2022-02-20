package com.example.bal_mukund.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import com.example.bal_mukund.SettingsFragment
import com.example.bal_mukund.adapters.DrawerAdapter
import com.example.bal_mukund.adapters.items.SpaceItem
import com.example.bal_mukund.databinding.ActivityMainBinding
import com.example.bal_mukund.interfaces.LatestPostListener
import com.example.bal_mukund.model.Post
import com.example.bal_mukund.R
import com.example.bal_mukund.ui.fragments.RecentFragment
import com.example.bal_mukund.ui.fragments.TodayFragment
import com.example.bal_mukund.viewmodels.MainViewModel
import com.yarolegovich.slidingrootnav.SlidingRootNav
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder


import com.example.bal_mukund.adapters.items.DrawerItem
import androidx.core.content.ContextCompat

import androidx.annotation.ColorRes

import androidx.annotation.ColorInt
import androidx.drawerlayout.widget.DrawerLayout


import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bal_mukund.adapters.items.SimpleItem
import com.yarolegovich.slidingrootnav.callback.DragStateListener


class MainActivity : AppCompatActivity(), LatestPostListener, DrawerAdapter.OnItemSelectedListener,
    DragStateListener {
    lateinit var binding: ActivityMainBinding
    private val mainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    private val POS_DASHBOARD = 0
    private val POS_ACCOUNT = 1
    private val POS_MESSAGES = 2
    private val POS_CART = 3
    private val POS_LOGOUT = 5

    private var screenTitles: Array<String> = arrayOf()
    private var screenIcons: Array<Drawable?> = arrayOf()

    private var slidingRootNav: SlidingRootNav? = null

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
    private lateinit var drawerRecycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_nav_menu)
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

        slidingRootNav = SlidingRootNavBuilder(this)
            .withToolbarMenuToggle(binding.toolbar)
            .withMenuOpened(false)
            .addDragStateListener(this)
            .withContentClickableWhenMenuOpened(false)
            .withSavedState(savedInstanceState)
            .withMenuLayout(R.layout.nav_drawer_menu)
            .inject()


        screenIcons = loadScreenIcons()
        screenTitles = loadScreenTitles()


        val drawerAdapter = DrawerAdapter(
            listOf(
                createItemFor(POS_DASHBOARD).setChecked(true),
                createItemFor(POS_ACCOUNT),
                createItemFor(POS_MESSAGES),
                createItemFor(POS_CART),
                SpaceItem(48),
                createItemFor(POS_LOGOUT)
            ) as List<DrawerItem<*>>
        )
        drawerAdapter.setListener(this)

        drawerRecycler = findViewById(R.id.drawer_recyclerView)
        drawerRecycler.apply {
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = drawerAdapter
            visibility = View.GONE
        }

        drawerAdapter.setSelected(POS_DASHBOARD)

    }

    private fun createItemFor(position: Int): DrawerItem<*> {
        return SimpleItem(screenIcons[position]!!, screenTitles[position])
            .withIconTint(color(R.color.textColorSecondary))
            .withTextTint(color(R.color.textColorPrimary))
            .withSelectedIconTint(color(R.color.colorAccent))
            .withSelectedTextTint(color(R.color.colorAccent))
    }

    @ColorInt
    private fun color(@ColorRes res: Int): Int {
        return ContextCompat.getColor(this, res)
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

    private fun loadScreenIcons(): Array<Drawable?> {
        val ta = resources.obtainTypedArray(R.array.ld_activityScreenIcons)
        val icons = arrayOfNulls<Drawable>(ta.length())
        for (i in 0 until ta.length()) {
            val id = ta.getResourceId(i, 0)
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id)
            }
        }
        ta.recycle()
        return icons
    }

    override fun onItemSelected(position: Int) {

    }

    private fun loadScreenTitles(): Array<String> {
        return resources.getStringArray(R.array.ld_activityScreenTitles)
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_fragments_container, fragment)
            .commit()
    }

    override fun onDragStart() {
        if (slidingRootNav?.isMenuClosed == true) {
            drawerRecycler.visibility = View.VISIBLE
        } else {
            drawerRecycler.visibility = View.GONE
            Toast.makeText(this, "dragStart", Toast.LENGTH_SHORT).show()
            slidingRootNav?.closeMenu()
        }

    }

    override fun onBackPressed() {
        if (slidingRootNav?.isMenuOpened == true) {
            slidingRootNav!!.closeMenu()
        } else
            super.onBackPressed()
    }

    override fun onDragEnd(isMenuOpened: Boolean) {


    }


}