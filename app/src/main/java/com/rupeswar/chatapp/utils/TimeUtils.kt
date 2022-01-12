package com.rupeswar.chatapp.utils

import java.text.SimpleDateFormat
import java.util.*

class TimeUtils {

    companion object{
        private const val SECOND_MILLIS = 1000L
        private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
        private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
        private const val DAY_MILLIS = 24 * HOUR_MILLIS

        fun getDateAndTime(time: Long): String {
            return SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault()).format(Date(time))
        }

        fun getDateOrTime(time: Long): String {
            val specifiedTime = getDateAndTime(time)
            val currentTime = getDateAndTime(System.currentTimeMillis())

            return if(specifiedTime.substring(0, 10) == currentTime.substring(0, 10)) specifiedTime.substring(10)
                else specifiedTime.substring(0, 10)
        }

        fun getDuration(duration: Long): String {
            return when {
                duration% DAY_MILLIS == 0L -> (duration/ DAY_MILLIS).toString() + " Days"
                duration% HOUR_MILLIS == 0L -> (duration/ HOUR_MILLIS).toString() + " Hours"
                duration% MINUTE_MILLIS == 0L -> (duration/ MINUTE_MILLIS).toString() + " Minutes"
                duration% SECOND_MILLIS == 0L -> (duration/ SECOND_MILLIS).toString() + " Seconds"
                else -> "$duration Milliseconds"
            }
        }
    }
}