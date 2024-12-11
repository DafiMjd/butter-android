package com.example.butterapp.common.helper

import android.icu.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class DateHelper {
    companion object {
        fun parse(
            string: String,
            dateFormat: String = "yyyy-MM-dd HH:mm:ss",
            timeZone: TimeZone = TimeZone.getTimeZone("UTC"),
        ): ZonedDateTime? {
            try {
                val date = string.substringBefore("T")
                val time = string.substringAfter("T").substringBefore(".")

                val timeServerFormatter = DateTimeFormatter.ofPattern(dateFormat, Locale.ENGLISH)

                val utcDateTime = LocalDateTime.parse(
                    "$date $time",
                    timeServerFormatter
                ).atZone(ZoneId.of("UTC"))

                val localDateTime = utcDateTime.withZoneSameInstant(ZoneId.systemDefault())

//                val localFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)

                // Return the formatted local time.
                return localDateTime
            } catch (e: Exception) {
                return null
            }
        }

        fun toPostTimeStamp(date: ZonedDateTime?): String {
            if (date == null) {
                return ""
            }
            val now = ZonedDateTime.now()
            val duration = Duration.between(date, now)

            return when {
                duration.toMinutes() < 1 -> "just now"
                duration.toHours() < 1 -> "${duration.toMinutes()}m"
                duration.toDays() < 1 -> "${duration.toHours()}h"
                duration.toDays() < 7 -> "${duration.toDays()}d"
                else -> date.format(DateTimeFormatter.ofPattern("dd/MM/yy"))
            }

//            return when {
//                duration.toMinutes() < 1 -> "just now"
//                duration.toHours() < 1 -> "${duration.toMinutes()} minute${if (duration.toMinutes() > 1) "s" else ""} ago"
//                duration.toDays() < 1 -> "${duration.toHours()} hour${if (duration.toHours() > 1) "s" else ""} ago"
//                duration.toDays() < 7 -> "${duration.toDays()} day${if (duration.toDays() > 1) "s" else ""} ago"
//                else -> date.format(DateTimeFormatter.ofPattern("dd/MM/yy"))
//            }
        }
    }
}