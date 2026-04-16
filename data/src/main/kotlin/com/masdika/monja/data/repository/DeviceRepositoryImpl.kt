package com.masdika.monja.data.repository

import com.masdika.monja.data.di.IoDispatcher
import com.masdika.monja.data.entity.DeviceConnectivityEntity
import com.masdika.monja.data.model.Device
import com.masdika.monja.data.repository.interfaces.DeviceRepository
import com.masdika.monja.data.Result
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
import timber.log.Timber
import javax.inject.Inject

class DeviceRepositoryImpl @Inject constructor(
    private val supabase: SupabaseClient,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : DeviceRepository {
    override suspend fun getAvailableDevices(): List<Device> {
        return withContext(ioDispatcher) {
            Timber.d("Fetching available devices from Supabase...")
            try {
                val entities = supabase.postgrest["device_connectivity"]
                    .select()
                    .decodeList<DeviceConnectivityEntity>()

                Timber.d("Successfully fetched ${entities.size} devices.")

                entities.map { entity ->
                    Device(
                        macAddress = entity.macAddress,
                        isOnline = entity.connectionStatus.equals("Online", ignoreCase = true),
                        lastSeen = entity.lastSeen ?: "Unknown",
                        createdAt = entity.createdAt ?: ""
                    )
                }
            } catch (e: Exception) {
                Timber.e(e, "Failed to fetch available devices from Supabase.")
                throw e
            }
        }
    }

    override fun getDeviceStream(): Flow<Result<List<Device>>> = channelFlow {
        Timber.d("Starting device stream...")
        send(Result.Loading)

        try {
            Timber.d("Performing initial device data fetch...")
            send(Result.Success(getAvailableDevices()))
        } catch (e: Exception) {
            Timber.e(e, "Failed during initial device data fetch.")
            send(Result.Error(e, "Failed initiating device data: ${e.message}"))
            return@channelFlow
        }

        Timber.d("Setting up Supabase Realtime subscription for 'device_connectivity_channel'...")
        val channel = supabase.realtime.channel("device_connectivity_channel")
        val changeFlow = channel.postgresChangeFlow<PostgresAction>(schema = "public") {
            table = "devices"
        }

        channel.subscribe()

        val realtimeJob = launch {
            try {
                changeFlow.collect { action ->
                    Timber.d("Received Realtime action: ${action::class.simpleName}")

                    if (action is PostgresAction.Insert || action is PostgresAction.Update || action is PostgresAction.Delete) {
                        Timber.d("Triggering data refresh due to Realtime event.")
                        send(Result.Success(getAvailableDevices()))
                    }
                }
            } catch (e: Exception) {
                Timber.e(e, "Error collecting Realtime changes.")
                send(Result.Error(e, "Realtime error: ${e.message}"))
            }
        }

        Timber.d("Starting 3-second polling fallback job...")
        val pollingJob = launch {
            while (isActive) {
                delay(3000)
                try {
                    Timber.v("Polling: Fetching latest device data...")
                    send(Result.Success(getAvailableDevices()))
                } catch (e: Exception) {
                    Timber.e(e, "Error occurred during routine polling.")
                    send(Result.Error(e, "Polling error: ${e.message}"))
                }
            }
        }

        awaitClose {
            Timber.d("Closing device stream. Cancelling jobs and cleaning up resources...")
            realtimeJob.cancel()
            pollingJob.cancel()

            CoroutineScope(ioDispatcher + NonCancellable).launch {
                try {
                    Timber.d("Removing Realtime channel subscription...")
                    supabase.realtime.removeChannel(channel)
                    Timber.d("Realtime channel removed successfully.")
                } catch (e: Exception) {
                    Timber.e(e, "Failed to remove Realtime channel gracefully.")
                }
            }
        }
    }.flowOn(ioDispatcher)
}