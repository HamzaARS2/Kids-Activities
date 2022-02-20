package com.example.bal_mukund.helpers

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class BalMukundDateManager(private val longDate:Long = Date().time) {


    @SuppressLint("SimpleDateFormat")
    fun convertDateToMilliseconds(date:String) : Long{
        try {
            val dateFormat = SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
            val date = dateFormat.parse(date)
            return date.time
        }catch (e: ParseException){
            e.printStackTrace()
        }
        return 0L
    }

    fun convertLongDateToString(longDate: Long): String {
        return SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US).format(Date(longDate))
    }

    fun getTimeAgo(sentTime: Long = longDate): String {
        try {
            val sentDate = Date(sentTime)
            val nowDate = Date()
            val timeAgo = nowDate.time - sentDate.time
            val seconds = TimeUnit.MILLISECONDS.toSeconds(timeAgo)
            val minutes = TimeUnit.MILLISECONDS.toMinutes(timeAgo)
            val hours = TimeUnit.MILLISECONDS.toHours(timeAgo)
            val days = TimeUnit.MILLISECONDS.toDays(timeAgo)
            return when {
                seconds < 60 -> "$seconds seconds ago"
                minutes < 60 -> "$minutes minutes ago"
                hours < 24 -> "$hours hours ago"
                else -> "$days days ago"
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return "unknown"
    }
}