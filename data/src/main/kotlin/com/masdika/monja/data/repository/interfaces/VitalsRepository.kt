package com.masdika.monja.data.repository.interfaces

import com.masdika.monja.data.model.Vitals
import com.masdika.monja.data.utils.Result
import kotlinx.coroutines.flow.Flow

interface VitalsRepository {
    suspend fun getAvailableVitals(macAddress: String, limit: Int = 100): List<Vitals>
    fun getVitalStream(macAddress: String): Flow<Result<List<Vitals>>>
}