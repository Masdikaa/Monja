package com.masdika.monja.util

import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun dateFormat(dateString: String): String {
    return try {
        val parsedDate = ZonedDateTime.parse(dateString)

        val jakartaZone = ZoneId.of("Asia/Jakarta")
        val jakartaDate = parsedDate.withZoneSameInstant(jakartaZone)
        val localeID = Locale.forLanguageTag("id-ID")

        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm", localeID)
        jakartaDate.format(formatter)
    } catch (e: Exception) {
        e.printStackTrace()
        dateString
    }
}