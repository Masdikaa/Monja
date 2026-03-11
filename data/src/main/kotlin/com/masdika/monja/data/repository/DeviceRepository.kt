package com.masdika.monja.data.repository

import com.masdika.monja.data.entity.DeviceConnectivityEntity
import com.masdika.monja.data.model.Device
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresChangeFlow
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

interface DeviceRepository {
    suspend fun getAvailableDevices(): List<Device>
    fun getDeviceStream(): Flow<List<Device>>
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

    override fun getDeviceStream(): Flow<List<Device>> = channelFlow {
        send(getAvailableDevices()) // Emit initial device

        launch(Dispatchers.IO) {
            val channel = supabase.channel("device_monitoring_channel")
            val changes = channel.postgresChangeFlow<PostgresAction>(schema = "public") {
                table = "devices"
            }
            channel.subscribe()
            changes.collect {
                send(getAvailableDevices())
            }
        }

        launch(Dispatchers.IO) {
            while (isActive) {
                delay(5000)
                send(getAvailableDevices())
            }
        }
    }
}