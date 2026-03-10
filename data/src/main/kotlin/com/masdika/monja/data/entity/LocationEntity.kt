package com.masdika.monja.data.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LocationEntity(
    @SerialName("id") val id: Int? = null,
    @SerialName("mac_address") val macAddress: String,
    @SerialName("latitude") val latitude: String,
    @SerialName("longitude") val longitude: String,
    @SerialName("created_at") val createdAt: String? = null
)