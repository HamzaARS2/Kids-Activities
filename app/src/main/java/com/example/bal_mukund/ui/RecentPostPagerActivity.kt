package com.example.bal_mukund.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.bal_mukund.adapters.ViewPagerAdapter
import com.example.bal_mukund.databinding.ActivityRecentPostPagerBinding
import com.example.bal_mukund.interfaces.FirestoreListener
import com.example.bal_mukund.model.Post
import com.example.bal_mukund.viewmodels.PostPagerViewModel

class RecentPostPagerActivity : AppCompatActivity(), ViewPagerAdapter.PostClickListener,
    FirestoreListener {

    private val binding by lazy {
        ActivityRecentPostPagerBinding.inflate(layoutInflater)
    }

    private val pagerViewModel by lazy {
        ViewModelProvider(this)[PostPagerViewModel::class.java]
    }

    private lateinit var postsPagerAdapter:ViewPagerAdapter
    private  var selectedPos:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val position = intent.getIntExtra("position",0)
        selectedPos = position
        pagerViewModel.getRecentPosts(this)



    }

    override fun onPostTypeClicked(postType: Int) {

    }

    override fun onDataReceived(posts: ArrayList<Post>) {
        postsPagerAdapter = ViewPagerAdapter(posts,listener = this)
        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(20))
        binding.postsViewPager.apply {
            adapter = postsPagerAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            setPageTransformer(compositePageTransformer)
            setCurrentItem(selectedPos,false)
        }
    }


}