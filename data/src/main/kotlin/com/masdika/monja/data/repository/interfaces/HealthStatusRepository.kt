package com.masdika.monja.data.repository.interfaces

import com.masdika.monja.data.model.HealthStatus
import kotlinx.coroutines.flow.Flow

interface HealthStatusRepository {
    suspend fun getAvailableHealthStatuses(macAddress: String): HealthStatus?
    fun getHealthStatusesStream(macAddress: String): Flow<HealthStatus?>
}