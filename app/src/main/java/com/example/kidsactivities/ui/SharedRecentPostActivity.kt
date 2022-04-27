package com.example.kidsactivities.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.kidsactivities.databinding.ActivitySharedElementsBinding
import com.example.kidsactivities.model.Post

class SharedRecentPostActivity : AppCompatActivity(), RequestListener<Drawable>,
    View.OnClickListener {

    private val binding by lazy{
        ActivitySharedElementsBinding.inflate(layoutInflater)
    }



    private lateinit var post:Post
    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        post = intent.getSerializableExtra("recentPost") as Post
        binding.run {
            Glide.with(this@SharedRecentPostActivity)
                .load(post.image)
                .listener(this@SharedRecentPostActivity)
                .into(binding.recentPostActivityImage)
            recentPostActivityQuoteTv.apply {
                text = post.quote
            }

            sharedFabBtn.setOnClickListener(this@SharedRecentPostActivity)
        }


    }

    override fun onLoadFailed(
        e: GlideException?,
        model: Any?,
        target: Target<Drawable>?,
        isFirstResource: Boolean
    ): Boolean {
        startPostponedEnterTransition()
        return false
    }

    override fun onResourceReady(
        resource: Drawable?,
        model: Any?,
        target: Target<Drawable>?,
        dataSource: DataSource?,
        isFirstResource: Boolean
    ): Boolean {
        startPostponedEnterTransition()
        return false
    }

    override fun onClick(view: View?) {
        val intent = Intent(this,ShareActivity::class.java)
        intent.putExtra(ShareActivity.SHARE_POST_CODE,post)
        val pair: Pair<View, String> = Pair.create(binding.recentPostActivityImage as View,"imageTN")
        val pair2: Pair<View, String> = Pair.create(binding.recentPostActivityQuoteTv as View,"quoteTN")


        val option = ActivityOptionsCompat.makeSceneTransitionAnimation(this,pair,pair2)
        startActivity(intent,option.toBundle())
    }
}