package com.example.kidsactivities.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.kidsactivities.R
import com.example.kidsactivities.databinding.ActivityConnectBinding

class ConnectActivity : AppCompatActivity(), View.OnClickListener {


    private val binding by lazy {
        ActivityConnectBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.connectToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.fbBtn.setOnClickListener(this)
        binding.instaBtn.setOnClickListener(this)
        binding.ytBtn.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.fb_btn -> {
                val uri = Uri.parse(resources.getString(R.string.facebook_url))
                startActivity(Intent(Intent.ACTION_VIEW,uri))
            }
            R.id.insta_btn -> {
                val uri = Uri.parse(resources.getString(R.string.instagram_url))
                startActivity(Intent(Intent.ACTION_VIEW,uri))
            }
            R.id.yt_btn -> {
                val uri = Uri.parse(resources.getString(R.string.youtube_url))
                startActivity(Intent(Intent.ACTION_VIEW,uri))
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}