package com.masdika.monja.data.repository

import com.masdika.monja.data.entity.DeviceConnectivityEntity
import com.masdika.monja.data.model.Device
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import jakarta.inject.Inject

interface DeviceRepository {
    suspend fun getAvailableDevices(): List<Device>
}

class DeviceRepositoryImpl @Inject constructor(
    private val supabase: SupabaseClient
) : DeviceRepository {
    override suspend fun getAvailableDevices(): List<Device> {
        return try {
            val entities = supabase.postgrest["device_connectivity"]
                .select()
                .decodeList<DeviceConnectivityEntity>()

            entities.map { entity ->
                Device(
                    macAddress = entity.macAddress,
                    isOnline = entity.connectionStatus.equals("Online", ignoreCase = true),
                    lastSeen = entity.lastSeen ?: "Unknown"
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}