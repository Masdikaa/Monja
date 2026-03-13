package com.masdika.monja.data.repository.interfaces

import com.masdika.monja.data.model.Vitals
import kotlinx.coroutines.flow.Flow

interface VitalsRepository {
    suspend fun getAvailableVitals(macAddress: String): Vitals?
    fun getVitalStream(macAddress: String): Flow<Vitals?>
}