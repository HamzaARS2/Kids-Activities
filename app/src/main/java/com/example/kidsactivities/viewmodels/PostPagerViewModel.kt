package com.example.kidsactivities.viewmodels

import androidx.lifecycle.ViewModel
import com.example.kidsactivities.database.Repository
import com.example.kidsactivities.interfaces.FirestoreListener

class PostPagerViewModel : ViewModel() {

    private val repository = Repository()

    fun getRecentPosts(mListener:FirestoreListener){
        repository.retrieveData(mListener)
    }
}