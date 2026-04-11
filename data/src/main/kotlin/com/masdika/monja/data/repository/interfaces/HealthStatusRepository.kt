package com.masdika.monja.data.repository.interfaces

import com.masdika.monja.data.model.HealthStatus
import com.masdika.monja.data.utils.Result
import kotlinx.coroutines.flow.Flow

interface HealthStatusRepository {
    fun getHealthStatusesStream(macAddress: String): Flow<Result<HealthStatus?>>
}