package com.masdika.monja.util

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

fun openGoogleMaps(context: Context, latitude: String, longitude: String) {
    val gmmUri = "geo:$latitude,$longitude?q=$latitude,$longitude"
    val intent = Intent(Intent.ACTION_VIEW, gmmUri.toUri())

    intent.setPackage("com.google.android.apps.maps")
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    } else {
        val browserUri = "https://www.google.com/maps/search/?api=1&query=$latitude,$longitude"
        val browserIntent = Intent(Intent.ACTION_VIEW, browserUri.toUri())
        context.startActivity(browserIntent)
    }

}