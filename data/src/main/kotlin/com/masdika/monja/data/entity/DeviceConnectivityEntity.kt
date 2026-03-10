package com.masdika.monja.data.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeviceConnectivityEntity(
    @SerialName("mac_address") val macAddress: String,
    @SerialName("connection_status") val connectionStatus: String,
    @SerialName("last_seen") val lastSeen: String? = null
)