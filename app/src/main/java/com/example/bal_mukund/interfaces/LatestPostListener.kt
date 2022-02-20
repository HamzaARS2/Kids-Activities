package com.example.bal_mukund.interfaces

import com.example.bal_mukund.model.Post

interface LatestPostListener {
    fun onTodayPostReceived(post: Post)
}