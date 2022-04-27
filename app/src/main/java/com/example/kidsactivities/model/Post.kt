package com.example.kidsactivities.model

import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp
import java.io.Serializable
import java.util.*


@IgnoreExtraProperties
data class Post(
    val id:String,
    val image:String = "",
    val title:String = "",
    val subtitle:String = "",
    val quote:String = "",
    val link:String = "http://www.google.com",
    @ServerTimestamp
    val date:Date? = null
):Serializable
