package com.example.bal_mukund.database

import android.util.Log
import com.example.bal_mukund.interfaces.FirestoreListener
import com.example.bal_mukund.interfaces.LatestPostListener
import com.example.bal_mukund.model.Post
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
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
        const val BG_IMAGE = "bg_image"
        const val IMAGE_URL = "small_image"
        const val TYPE = "type"
        const val TITLE = "title"
        const val BODY = "body"
        const val DATE = "date"
        const val TOPIC = "topic"
    }

    fun retrieveData(listener:FirestoreListener){
        val postList = arrayListOf<Post>()
        postsColl
            .orderBy("date",Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { docs ->
                for (doc in docs){
                    val type = doc.data[Constants.TYPE].toString().toInt()
                    val imageBg = doc.data[Constants.BG_IMAGE].toString()
                    val imageUrl = doc.data[Constants.IMAGE_URL].toString()
                    val topic = doc.data[Constants.TOPIC].toString()
                    val title = doc.data[Constants.TITLE].toString()
                    val body = doc.data[Constants.BODY].toString()
                    postList.add(Post(type,imageBg,imageUrl,topic,title,body))
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
                        val type = todayDoc.data?.get(Constants.TYPE).toString().toInt()
                        val imageBg = todayDoc.data?.get(Constants.BG_IMAGE).toString()
                        val imageUrl = todayDoc.data?.get(Constants.IMAGE_URL).toString()
                        val topic = todayDoc.data?.get(Constants.TOPIC).toString()
                        val title = todayDoc.data?.get(Constants.TITLE).toString()
                        val body = todayDoc.data?.get(Constants.BODY).toString()
                        val todayPost = Post(type,imageBg,imageUrl,topic,title,body)
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