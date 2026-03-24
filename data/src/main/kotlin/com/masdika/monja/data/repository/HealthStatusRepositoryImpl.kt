package com.masdika.monja.data.repository

import android.util.Log
import com.masdika.monja.data.di.IoDispatcher
import com.masdika.monja.data.entity.HealthStatusEntity
import com.masdika.monja.data.model.HealthStatus
import com.masdika.monja.data.repository.interfaces.HealthStatusRepository
import com.masdika.monja.data.utils.Result
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.decodeRecord
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.realtime.realtime
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HealthStatusRepositoryImpl @Inject constructor(
    private val supabase: SupabaseClient,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : HealthStatusRepository {
    private val cleanupMutex = Mutex()

    override suspend fun getAvailableHealthStatuses(macAddress: String): HealthStatus? {
        return withContext(ioDispatcher) {
            try {
                val entity = supabase.postgrest["device_health_status"]
                    .select {
                        filter { eq("mac_address", macAddress) }
                        order("updated_at", order = Order.DESCENDING)
                        limit(1)
                    }
                    .decodeSingleOrNull<HealthStatusEntity>()

                entity?.let {
                    HealthStatus(
                        status = entity.status
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                throw e
            }
        }
    }

    override fun getHealthStatusesStream(
        macAddress: String
    ): Flow<Result<HealthStatus?>> = channelFlow {
        send(Result.Loading)

        try {
            val initialHealthStatusData = getAvailableHealthStatuses(macAddress)
            send(Result.Success(initialHealthStatusData))
        } catch (e: Exception) {
            send(Result.Error(e, "Failed initiating health status data: ${e.message}"))
        }

        val channel = supabase.channel("health_status_channel_$macAddress")
        val changeFlow = channel.postgresChangeFlow<PostgresAction>(schema = "public") {
            table = "device_health_status"
            filter("mac_address", FilterOperator.EQ, macAddress)
        }

        val realtimeJob = launch(ioDispatcher) {
            channel.subscribe()
            changeFlow.collect { action ->
                Log.i(
                    "REPOSITORY SUPABASE HEALTH STATUS",
                    "New Health Status: $macAddress - $action"
                )
                try {
                    when (action) {
                        is PostgresAction.Insert -> {
                            val entity = action.decodeRecord<HealthStatusEntity>()
                            send(Result.Success(HealthStatus(entity.status)))
                        }

                        is PostgresAction.Update -> {
                            val entity = action.decodeRecord<HealthStatusEntity>()
                            send(Result.Success(HealthStatus(entity.status)))
                        }

                        else -> {}
                    }
                } catch (e: Exception) {
                    send(Result.Error(e, "Failed to update health status data: ${e.message}"))
                }
            }
        }

        cleanupMutex.lock()

        awaitClose {
            realtimeJob.cancel()
            cleanupMutex.unlock()
        }

        launch(ioDispatcher) {
            cleanupMutex.withLock {
                try {
                    supabase.realtime.removeChannel(channel)
                    Log.i(
                        "REPOSITORY SUPABASE HEALTH STATUS",
                        "Close realtime connection for $macAddress device health status"
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}