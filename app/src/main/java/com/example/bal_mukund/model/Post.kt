package com.example.bal_mukund.model

import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp
import java.io.Serializable
import java.util.*


@IgnoreExtraProperties
data class Post(
    val id:String,
    val imageUrl:String = "",
    val quote:String = "",
    val textSize:Float = 14f,
    val textColor:String = "",
    val link:String = "http://www.google.com",
    @ServerTimestamp
    val date:Date? = null
):Serializable
