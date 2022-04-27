package com.example.kidsactivities.viewmodels

import androidx.lifecycle.ViewModel
import com.example.kidsactivities.database.Repository
import com.example.kidsactivities.interfaces.FirestoreListener
import com.example.kidsactivities.interfaces.LatestPostListener
import com.example.kidsactivities.model.Post
import com.google.firebase.messaging.FirebaseMessaging

open class MainViewModel(
    private val repository: Repository = Repository()
) : ViewModel() {

    private val firebaseMsg = FirebaseMessaging.getInstance()


    fun subscribeUsers(topic: String) {
        if (topic == "true")
            firebaseMsg.subscribeToTopic(topic)
    }

    fun unSubscribeUsers(){
        firebaseMsg.unsubscribeFromTopic("true")
    }

    fun addPost(post: Post) {
        repository.insertPost(post)
    }

    fun getAllPosts(listener: FirestoreListener) {
        repository.retrieveData(listener)
    }

    fun getLatestPost(listener: LatestPostListener) {
        repository.getLatestPost(listener)
    }

    fun notificationsStateChanged(isChecked: String) {
        if (isChecked == "true"){
            subscribeUsers(isChecked)
        }else
            unSubscribeUsers()
    }


}