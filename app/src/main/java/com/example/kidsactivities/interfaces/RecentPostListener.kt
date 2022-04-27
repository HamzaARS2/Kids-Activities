package com.example.kidsactivities.interfaces

import android.widget.ImageView
import android.widget.TextView
import com.example.kidsactivities.model.Post

interface RecentPostListener {
    fun onPostClicked(post: Post,image:ImageView,quoteTv:TextView)
}