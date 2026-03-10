package com.masdika.monja.data.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MedicalAlertEntity(
    @SerialName("id") val id: Int? = null,
    @SerialName("mac_address") val macAddress: String,
    @SerialName("old_status") val oldStatus: String,
    @SerialName("new_status") val newStatus: String,
    @SerialName("temperature_at_time") val temperatureAtTime: Double,
    @SerialName("spo2_at_time") val spo2AtTime: Int,
    @SerialName("latitude") val latitude: String? = null,
    @SerialName("longitude") val longitude: String? = null,
    @SerialName("created_at") val createdAt: String? = null
)