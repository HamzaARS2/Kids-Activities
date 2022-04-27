package com.example.kidsactivities.database

import android.util.Log
import com.example.kidsactivities.interfaces.FirestoreListener
import com.example.kidsactivities.interfaces.LatestPostListener
import com.example.kidsactivities.model.Post
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Repository(
    private val firebase:Firebase = Firebase,
    private val fireStore: FirebaseFirestore = Firebase.firestore,
    private val postsColl: CollectionReference = fireStore.collection("posts")
) {

    private object Constants {
        const val POST_ID = "id"
        const val POST_IMAGE = "image"
        const val POST_TITLE = "title"
        const val POST_SUBTITLE = "subtitle"
        const val POST_QUOTE = "quote"
        const val POST_URL_LINK = "link"

    }

    fun retrieveData(listener:FirestoreListener){
        val postList = arrayListOf<Post>()
        postsColl
            .orderBy("date",Query.Direction.DESCENDING)
            .limit(10)
            .get()
            .addOnSuccessListener { docs ->
                for (doc in docs){
                    val id = doc.data[Constants.POST_ID].toString()
                    val image = doc.data[Constants.POST_IMAGE].toString()
                    val title = doc.data[Constants.POST_TITLE].toString()
                    val subtitle = doc.data[Constants.POST_SUBTITLE].toString()
                    val quote = doc.data[Constants.POST_QUOTE].toString()
                    val link = doc.data[Constants.POST_URL_LINK].toString()
                    postList.add(Post(id,image,title,subtitle,quote,link))
                }
                listener.onDataReceived(postList)
            }
            .addOnFailureListener { e ->
                Log.e("RetrieveDataError",e.message.toString())
            }
    }

    fun getLatestPost(listener: LatestPostListener){
        postsColl
            .orderBy("date",Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    val docs = task.result.documents
                    if (docs.size != 0 && docs[0].exists()){
                        val todayDoc = docs[0]
                        val id = todayDoc.data?.get(Constants.POST_ID).toString()
                        val image = todayDoc.data?.get(Constants.POST_IMAGE).toString()
                        val title = todayDoc.data?.get(Constants.POST_TITLE).toString()
                        val subtitle = todayDoc.data?.get(Constants.POST_SUBTITLE).toString()
                        val quote = todayDoc.data?.get(Constants.POST_QUOTE).toString()
                        val link = todayDoc.data?.get(Constants.POST_URL_LINK).toString()
                        val todayPost = Post(id,image,title,subtitle,quote,link)
                        listener.onTodayPostReceived(todayPost)
                    }
                }

            }
    }

    fun insertPost(post:Post){
        postsColl
            .add(post)
            .addOnSuccessListener { doc ->
                Log.d("InsertPost", "DocumentSnapshot added with ID:")
            }
            .addOnFailureListener { e ->
                Log.d("InsertPost", "DocumentSnapshot added with ID: ${e.message.toString()}")
            }
    }



}