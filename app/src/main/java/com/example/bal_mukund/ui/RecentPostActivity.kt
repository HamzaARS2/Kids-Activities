package com.example.bal_mukund.ui

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.bal_mukund.adapters.ViewPagerAdapter
import com.example.bal_mukund.databinding.ActivityRecentPostBinding
import com.example.bal_mukund.interfaces.FirestoreListener
import com.example.bal_mukund.model.Post

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
            Glide.with(this@RecentPostActivity).load(post.imageUrl).into(recentPostActivityImage)
            recentPostActivityQuoteTv.apply {
                text = post.quote
                textSize = post.textSize
                setTextColor(Color.parseColor(post.textColor))
            }
        }
    }

    override fun onPostTypeClicked(postType: Int) {

    }

    override fun onDataReceived(posts: ArrayList<Post>) {
    }


}