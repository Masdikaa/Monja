package com.masdika.monja.data.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SevereMonitorEntity(
    @SerialName("mac_address") val macAddress: String? = null,
    @SerialName("updated_at") val updatedAt: String? = null,
    @SerialName("is_severe") val isSevere: Boolean? = null,
    @SerialName("severe_start_time") val severeStartTime: String? = null
)