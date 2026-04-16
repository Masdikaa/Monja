package com.masdika.monja.data.repository.interfaces

import com.masdika.monja.data.model.Location
import com.masdika.monja.data.Result
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun getLocationStream(macAddress: String): Flow<Result<Location?>>
}