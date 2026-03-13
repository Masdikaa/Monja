package com.masdika.monja.data.repository.interfaces

import com.masdika.monja.data.model.Location
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    suspend fun getAvailableLocation(macAddress: String): Location?
    fun getLocationStream(macAddress: String): Flow<Location?>
}