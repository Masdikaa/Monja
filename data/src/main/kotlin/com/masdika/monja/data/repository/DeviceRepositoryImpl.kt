package com.masdika.monja.data.repository

import android.util.Log
import com.masdika.monja.data.entity.DeviceConnectivityEntity
import com.masdika.monja.data.model.Device
import com.masdika.monja.data.repository.interfaces.DeviceRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.realtime.realtime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

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
                    lastSeen = entity.lastSeen ?: "Unknown",
                    createdAt = entity.createdAt ?: ""
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override fun getDeviceStream(): Flow<List<Device>> = channelFlow {
        send(getAvailableDevices())

        val channel = supabase.channel("device_connectivity")
        val changeFlow = channel.postgresChangeFlow<PostgresAction>(schema = "public") {
            table = "devices"
        }

        val realtimeJob = launch(Dispatchers.IO) {
            channel.subscribe()
            changeFlow.collect { action ->
                Log.i("REPOSITORY SUPABASE DEVICE", "DEVICE: $action")
                send(getAvailableDevices())
            }
        }

        val pollingJob = launch(Dispatchers.IO) {
            while (isActive) {
                delay(3000)
                send(getAvailableDevices())
            }
        }

        awaitClose {
            realtimeJob.cancel()
            pollingJob.cancel()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    supabase.realtime.removeChannel(channel)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            Log.i("REPOSITORY SUPABASE DEVICE", "Close realtime device realtime connection")
        }

    }
}