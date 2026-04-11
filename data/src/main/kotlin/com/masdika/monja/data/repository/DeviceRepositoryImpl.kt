package com.masdika.monja.data.repository

import android.util.Log
import com.masdika.monja.data.di.IoDispatcher
import com.masdika.monja.data.entity.DeviceConnectivityEntity
import com.masdika.monja.data.model.Device
import com.masdika.monja.data.repository.interfaces.DeviceRepository
import com.masdika.monja.data.utils.Result
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.realtime.realtime
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeviceRepositoryImpl @Inject constructor(
    private val supabase: SupabaseClient,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : DeviceRepository {
    override suspend fun getAvailableDevices(): List<Device> {
        return withContext(ioDispatcher) {
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
        }
    }

    override fun getDeviceStream(): Flow<Result<List<Device>>> = channelFlow {
        send(Result.Loading)

        try {
            send(Result.Success(getAvailableDevices()))
        } catch (e: Exception) {
            send(Result.Error(e, "Failed initiating device data: ${e.message}"))
            return@channelFlow
        }

        val channel = supabase.realtime.channel("device_connectivity_channel")
        val changeFlow = channel.postgresChangeFlow<PostgresAction>(schema = "public") {
            table = "devices"
        }

        channel.subscribe()

        val realtimeJob = launch {
            try {
                changeFlow.collect { action ->
                    if (action is PostgresAction.Insert || action is PostgresAction.Update || action is PostgresAction.Delete) {
                        send(Result.Success(getAvailableDevices()))
                    }
                }
            } catch (e: Exception) {
                send(Result.Error(e, "Realtime error: ${e.message}"))
            }
        }

        val pollingJob = launch {
            while (isActive) {
                delay(3000)
                try {
                    send(Result.Success(getAvailableDevices()))
                } catch (e: Exception) {
                    send(Result.Error(e, "Polling error: ${e.message}"))
                }
            }
        }

        awaitClose {
            realtimeJob.cancel()
            pollingJob.cancel()

            CoroutineScope(ioDispatcher + NonCancellable).launch {
                try {
                    supabase.realtime.removeChannel(channel)
                } catch (e: Exception) {
                    Log.e("Device Repository", "Error remove channel: ${e.message}")
                }
            }
        }
    }.flowOn(ioDispatcher)
}