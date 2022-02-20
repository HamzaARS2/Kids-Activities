package com.example.bal_mukund.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.*


data class Post(
    val type:Int,
    val backgroundImg:String = "",
    val imageUrl:String = "",
    val topic:String = "",
    val title:String = "",
    val body:String = "",
    @ServerTimestamp
    val date:Date? = null
)
