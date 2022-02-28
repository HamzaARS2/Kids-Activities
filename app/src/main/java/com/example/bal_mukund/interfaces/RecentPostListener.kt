package com.example.bal_mukund.interfaces

import android.widget.ImageView
import android.widget.TextView
import com.example.bal_mukund.model.Post

interface RecentPostListener {
    fun onPostClicked(post: Post,image:ImageView,quoteTv:TextView)
}