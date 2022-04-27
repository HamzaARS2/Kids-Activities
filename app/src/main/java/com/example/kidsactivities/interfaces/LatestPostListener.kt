package com.example.kidsactivities.interfaces

import com.example.kidsactivities.model.Post

interface LatestPostListener {
    fun onTodayPostReceived(post: Post)
}