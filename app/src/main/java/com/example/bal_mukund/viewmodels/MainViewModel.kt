package com.example.bal_mukund.viewmodels

import androidx.lifecycle.ViewModel
import com.example.bal_mukund.database.Repository
import com.example.bal_mukund.interfaces.FirestoreListener
import com.example.bal_mukund.interfaces.LatestPostListener
import com.example.bal_mukund.model.Post
import com.google.firebase.messaging.FirebaseMessaging

class MainViewModel(
    private val repository: Repository = Repository()
) : ViewModel() {

    private val firebaseMsg = FirebaseMessaging.getInstance()
    private object Constants {
        const val SEND_NOTIFICATIONS = "SEND_BALMUKUND_NOTIFICATIONS"
    }


    fun subscribeUsers(){
        firebaseMsg.subscribeToTopic(Constants.SEND_NOTIFICATIONS)
    }

    fun addPost(post: Post){
        repository.insertPost(post)
    }

    fun getAllPosts(listener:FirestoreListener) {
        repository.retrieveData(listener)
    }

    fun getLatestPost(listener: LatestPostListener){
        repository.getLatestPost(listener)
    }
}