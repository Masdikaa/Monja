package com.masdika.monja.data.repository.interfaces

import com.masdika.monja.data.model.MedicalAlert
import com.masdika.monja.data.utils.Result
import kotlinx.coroutines.flow.Flow

interface MedicalAlertsRepository {
    suspend fun getMedicalAlerts(macAddress: String): List<MedicalAlert>
    fun getMedicalAlertsStream(macAddress: String): Flow<Result<List<MedicalAlert>>>
    suspend fun deleteMedicalAlerts(macAddress: String)
}