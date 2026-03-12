package com.masdika.monja.data.repository

import com.masdika.monja.data.entity.DeviceConnectivityEntity
import com.masdika.monja.data.model.Device
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.realtime.realtime
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface DeviceRepository {
    fun getDeviceStream(): Flow<List<Device>>
}

class DeviceRepositoryImpl @Inject constructor(
    private val supabase: SupabaseClient
) : DeviceRepository {

    private suspend fun getAvailableDevices(): List<Device> {
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

    override fun getDeviceStream(): Flow<List<Device>> = flow {
        emit(getAvailableDevices()) // Emit initial device

        val channel = supabase.channel("device_connectivity")
        val changeFlow = channel.postgresChangeFlow<PostgresAction>(schema = "public") {
            table = "device_connectivity"
        }

        supabase.realtime.connect()
        channel.subscribe()

        changeFlow.collect {
            emit(getAvailableDevices())
        }
    }
}