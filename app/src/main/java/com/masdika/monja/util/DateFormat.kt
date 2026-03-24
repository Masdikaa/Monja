package com.masdika.monja.util

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun dateFormat(dateString: String): String {
    return try {
        val parsedDate = ZonedDateTime.parse(dateString)
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm", Locale.getDefault())
        parsedDate.format(formatter)
    } catch (e: Exception) {
        e.printStackTrace()
        dateString
    }
}