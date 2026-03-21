package com.masdika.monja.data.repository.interfaces

import com.masdika.monja.data.model.MedicalAlert
import kotlinx.coroutines.flow.Flow

interface MedicalAlertsRepository {
    suspend fun getMedicalAlerts(macAddress: String): List<MedicalAlert>
    fun getMedicalAlertsStream(macAddress: String): Flow<List<MedicalAlert>>
}