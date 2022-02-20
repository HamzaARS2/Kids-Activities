package com.example.bal_mukund.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.bal_mukund.R
import com.example.bal_mukund.model.Post
import com.example.bal_mukund.model.PostNotification
import com.example.bal_mukund.ui.MainActivity
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class PostNotificationService : FirebaseMessagingService() {

//    private val fireStore: FirebaseFirestore = Firebase.firestore
//    private val postsColl: CollectionReference = fireStore.collection("posts")

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
//        val bundle = Bundle()
//        val imageBg = message.data["imagebg"] as String
//        val imgUrl = message.data["image"] as String
//        val type = message.data["type"] as String
//        val title = message.data["title"] as String
//        val body = message.data["body"] as String
//        val postNotification = PostNotification(type,title)
//        insertPost(Post(imageBg,imgUrl,type,title,body))
//        bundle.putString(MainActivity.POST_IMAGE_BG,imageBg)
//        bundle.putString(MainActivity.POST_IMAGE,imgUrl)
//        bundle.putString(MainActivity.POST_TYPE,type)
//        bundle.putString(MainActivity.POST_TITLE,title)
//        bundle.putString(MainActivity.POST_BODY,body)
//        val intent = Intent(MainActivity.MESSAGE_ARRIVED)
//        intent.putExtra(MainActivity.POST_BUNDLE,bundle)
//        sendBroadcast(intent)
//        buildNotification(postNotification)
//        Log.d("onMessageReceived","Message Received : $title")

    }
//    fun insertPost(post:Post){
//        postsColl
//            .add(post)
//            .addOnSuccessListener { doc ->
//                Log.d("InsertPost", "DocumentSnapshot added with ID:")
//            }
//            .addOnFailureListener { e ->
//                Log.d("InsertPost", "DocumentSnapshot added with ID: ${e.message.toString()}")
//            }
//    }

    private object Constants {
        const val CHANNEL_ID = "BalMukundChannelId"
        const val PENDING_INTENT_REQ_CODE = 231
        const val Notification_ID = 1000
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(Constants.CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun buildNotification(notificationData: PostNotification) {
        createNotificationChannel()
        val builder = NotificationCompat.Builder(this, Constants.CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(notificationData.notificationName)
            .setContentText(notificationData.notificationBody)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(getPendingIntent())
        with(NotificationManagerCompat.from(this)) {
            notify(Constants.Notification_ID, builder.build())
        }
    }

    private fun getPendingIntent(): PendingIntent {
        val resultIntent = Intent(this, MainActivity::class.java)
        return PendingIntent.getActivity(
            this,
            Constants.PENDING_INTENT_REQ_CODE,
            resultIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }
}