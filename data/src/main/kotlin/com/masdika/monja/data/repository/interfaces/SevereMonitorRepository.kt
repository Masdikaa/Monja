package com.masdika.monja.data.repository.interfaces

import com.masdika.monja.data.model.SevereMonitor
import com.masdika.monja.data.utils.Result
import kotlinx.coroutines.flow.Flow

interface SevereMonitorRepository {
    suspend fun getAvailableSevereMonitor(macAddress: String): SevereMonitor?
    fun getSevereMonitorStream(macAddress: String): Flow<Result<SevereMonitor>>
}