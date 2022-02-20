package com.example.bal_mukund.viewmodels

import androidx.lifecycle.ViewModel
import com.example.bal_mukund.database.Repository
import com.example.bal_mukund.interfaces.FirestoreListener

class PostPagerViewModel : ViewModel() {

    private val repository = Repository()

    fun getRecentPosts(mListener:FirestoreListener){
        repository.retrieveData(mListener)
    }
}