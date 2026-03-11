package com.masdika.monja.data.model

data class Device(
    val macAddress: String,
    val isOnline: Boolean,
    val lastSeen: String
)