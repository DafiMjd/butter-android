package com.example.butterapp.common.helper

import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class DateHelper {
    companion object {
        fun toZonedDateTime(
            string: String,
            dateFormat: String = "yyyy-MM-dd HH:mm:ss",
            timeZone: ZoneId = ZoneId.of("UTC"),
        ): ZonedDateTime? {
            try {
                val date = string.substringBefore("T")
                val time = string.substringAfter("T").substringBefore(".")

                val timeServerFormatter = DateTimeFormatter.ofPattern(dateFormat, Locale.ENGLISH)

                val utcDateTime = LocalDateTime.parse(
                    "$date $time", timeServerFormatter
                ).atZone(timeZone)

                val localDateTime = utcDateTime.withZoneSameInstant(ZoneId.systemDefault())

//                val localFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)

                // Return the formatted local time.
                return localDateTime
            } catch (e: Exception) {
                return null
            }
        }

        fun toLocalDate(
            string: String?,
            dateFormat: String = "yyyy-MM-dd",
        ): LocalDate? {
            if (string == null) {
                return null
            }
            return try {
                val format = DateTimeFormatter.ofPattern(dateFormat, Locale.ENGLISH)
                LocalDate.parse(string, format)
            } catch (e: Exception) {
                null
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