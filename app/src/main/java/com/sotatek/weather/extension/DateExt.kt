package com.sotatek.weather.extension

import java.text.SimpleDateFormat
import java.util.*

fun Date.toIsoDateUTC(): String {
    return try {
        val simpleDateFormat = SimpleDateFormat(DateUtils.FORMAT_YYYY_MM_DD, Locale.ENGLISH)
        simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
        simpleDateFormat.format(this)
    } catch (e: Exception) {
        "0000-00-00T00:00:00"
    }
}

class DateUtils {
    companion object {
        const val FORMAT_YYYY_MM_DD = "yyyy-MM-dd"
        fun toIsoDateUTC(date: Date): String = date.toIsoDateUTC()
    }
}