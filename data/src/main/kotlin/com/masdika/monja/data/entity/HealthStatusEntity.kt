package com.masdika.monja.data.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HealthStatusEntity(
    @SerialName("mac_address") val macAddress: String,
    @SerialName("status") val status: String,
    @SerialName("updated_at") val updatedAt: String? = null
)