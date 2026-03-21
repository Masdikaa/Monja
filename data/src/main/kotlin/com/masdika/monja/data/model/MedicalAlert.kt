package com.masdika.monja.data.model

data class MedicalAlert(
    val id: Int,
    val macAddress: String,
    val oldStatus: String,
    val newStatus: String,
    val temperatureAtTime: Double?,
    val spo2AtTime: Int?,
    val latitude: String?,
    val longitude: String?,
    val createdAt: String
)