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
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeviceRepositoryImpl @Inject constructor(
    private val supabase: SupabaseClient,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : DeviceRepository {
    private val cleanupMutex = Mutex()

    override suspend fun getAvailableDevices(): List<Device> {
        return withContext(ioDispatcher) {
            try {
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
                throw e
            }
        }
    }

    override fun getDeviceStream(): Flow<Result<List<Device>>> = channelFlow {
        send(Result.Loading)

        try {
            val initialDeviceData = getAvailableDevices()
            send(Result.Success(initialDeviceData))
        } catch (e: Exception) {
            send(Result.Error(e, "Failed initiating device data: ${e.message}"))
        }

        val channel = supabase.channel("device_connectivity")
        val changeFlow = channel.postgresChangeFlow<PostgresAction>(schema = "public") {
            table = "devices"
        }

        val realtimeJob = launch(ioDispatcher) {
            channel.subscribe()
            changeFlow.collect { action ->
                Log.i("REPOSITORY SUPABASE DEVICE", "DEVICE: $action")
                try {
                    val newDeviceData = getAvailableDevices()
                    send(Result.Success(newDeviceData))
                } catch (e: Exception) {
                    send(
                        Result.Error(
                            e,
                            "Failed to update device data: ${e.message} - Realtime Job"
                        )
                    )
                }
            }
        }

        val pollingJob = launch(ioDispatcher) {
            while (isActive) {
                delay(3000)
                try {
                    val newDeviceData = getAvailableDevices()
                    send(Result.Success(newDeviceData))
                } catch (e: Exception) {
                    send(
                        Result.Error(
                            e,
                            "Failed to update device status: ${e.message} - Pooling Job"
                        )
                    )
                }
            }
        }

        cleanupMutex.lock()

        awaitClose {
            realtimeJob.cancel()
            pollingJob.cancel()
            cleanupMutex.unlock()
        }

        launch(ioDispatcher) {
            cleanupMutex.withLock {
                try {
                    supabase.realtime.removeChannel(channel)
                    Log.i("REPOSITORY SUPABASE DEVICE", "Close realtime device realtime connection")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}