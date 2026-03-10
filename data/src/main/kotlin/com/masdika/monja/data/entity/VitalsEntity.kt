package com.masdika.monja.data.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VitalsEntity(
    @SerialName("id") val id: Int? = null,
    @SerialName("mac_address") val macAddress: String,
    @SerialName("temperature") val temperature: Double,
    @SerialName("heartrate") val heartrate: Int,
    @SerialName("oxygen_saturation") val oxygenSaturation: Int,
    @SerialName("created_at") val createdAt: String? = null
)