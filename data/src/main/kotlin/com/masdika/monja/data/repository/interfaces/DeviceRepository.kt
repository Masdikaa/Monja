package com.masdika.monja.data.repository.interfaces

import com.masdika.monja.data.model.Device
import com.masdika.monja.data.Result
import kotlinx.coroutines.flow.Flow

interface DeviceRepository {
    suspend fun getAvailableDevices(): List<Device>
    fun getDeviceStream(): Flow<Result<List<Device>>>
}