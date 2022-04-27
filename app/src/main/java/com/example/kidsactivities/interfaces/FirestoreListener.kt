package com.example.kidsactivities.interfaces

import com.example.kidsactivities.model.Post

interface FirestoreListener {
    fun onDataReceived(posts:ArrayList<Post>)

}