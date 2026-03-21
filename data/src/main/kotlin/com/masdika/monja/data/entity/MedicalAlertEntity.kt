package com.masdika.monja.data.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MedicalAlertEntity(
    @SerialName("id") val id: Int? = null,
    @SerialName("mac_address") val macAddress: String? = null,
    @SerialName("old_status") val oldStatus: String? = null,
    @SerialName("new_status") val newStatus: String? = null,
    @SerialName("temp_at_time") val temperatureAtTime: Double? = null,
    @SerialName("spo2_at_time") val spo2AtTime: Int? = null,
    @SerialName("lat") val latitude: String? = null,
    @SerialName("long") val longitude: String? = null,
    @SerialName("created_at") val createdAt: String? = null
)