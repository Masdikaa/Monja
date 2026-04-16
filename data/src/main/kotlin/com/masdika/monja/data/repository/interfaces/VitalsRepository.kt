package com.masdika.monja.data.repository.interfaces

import com.masdika.monja.data.model.Vitals
import com.masdika.monja.data.Result
import kotlinx.coroutines.flow.Flow

interface VitalsRepository {
    fun getVitalStream(macAddress: String): Flow<Result<List<Vitals>>>
}