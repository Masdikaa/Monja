package com.masdika.monja.data.repository.interfaces

import com.masdika.monja.data.model.SevereMonitor
import com.masdika.monja.data.Result
import kotlinx.coroutines.flow.Flow

interface SevereMonitorRepository {
    fun getSevereMonitorStream(macAddress: String): Flow<Result<SevereMonitor?>>
}