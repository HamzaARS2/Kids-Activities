package com.example.bal_mukund.interfaces

import com.example.bal_mukund.model.Post

interface FirestoreListener {
    fun onDataReceived(posts:ArrayList<Post>)

}