package com.example.kidsactivities.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.kidsactivities.adapters.ViewPagerAdapter
import com.example.kidsactivities.databinding.ActivityRecentPostBinding
import com.example.kidsactivities.interfaces.FirestoreListener
import com.example.kidsactivities.model.Post

class RecentPostActivity : AppCompatActivity(), ViewPagerAdapter.PostClickListener,
    FirestoreListener {

    private val binding by lazy {
        ActivityRecentPostBinding.inflate(layoutInflater)
    }

    private lateinit var post: Post




    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        post = intent.getSerializableExtra("recentPost") as Post

        binding.run {
            Glide.with(this@RecentPostActivity).load(post.image).into(recentPostActivityImage)
            recentPostActivityQuoteTv.apply {
                text = post.quote
            }
        }
    }

    override fun onPostTypeClicked(postType: Int) {

    }

    override fun onDataReceived(posts: ArrayList<Post>) {
    }


}