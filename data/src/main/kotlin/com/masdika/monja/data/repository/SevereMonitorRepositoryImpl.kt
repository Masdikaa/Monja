package com.masdika.monja.data.repository

import android.util.Log
import com.masdika.monja.data.di.IoDispatcher
import com.masdika.monja.data.entity.SevereMonitorEntity
import com.masdika.monja.data.model.SevereMonitor
import com.masdika.monja.data.repository.interfaces.SevereMonitorRepository
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

class SevereMonitorRepositoryImpl @Inject constructor(
    private val supabase: SupabaseClient,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : SevereMonitorRepository {
    private val cleanupMutex = Mutex()

    override suspend fun getAvailableSevereMonitor(macAddress: String): SevereMonitor? {
        return withContext(ioDispatcher) {
            try {
                val entity = supabase.postgrest["severe_monitor"]
                    .select {
                        filter { eq("mac_address", macAddress) }
                        order("updated_at", order = Order.DESCENDING)
                        limit(1)
                    }
                    .decodeSingleOrNull<SevereMonitorEntity>()

                entity?.let {
                    SevereMonitor(
                        macAddress = it.macAddress ?: "",
                        isSevere = it.isSevere ?: false,
                        severeStartTime = it.severeStartTime ?: ""
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                throw e
            }
        }
    }

    override fun getSevereMonitorStream(
        macAddress: String
    ): Flow<Result<SevereMonitor>> = channelFlow {
        send(Result.Loading)

        try {
            val initialSevereMonitor = getAvailableSevereMonitor(macAddress)
            if (initialSevereMonitor != null) {
                send(Result.Success(initialSevereMonitor))
            } else {
                send(Result.Success(SevereMonitor(macAddress, false, "")))
            }
        } catch (e: Exception) {
            send(Result.Error(e, "Failed initiating severe monitor data: ${e.message}"))
        }

        val channel = supabase.channel("severe_monitor_channel_$macAddress")
        val changeFlow = channel.postgresChangeFlow<PostgresAction>(schema = "public") {
            table = "severe_monitor"
            filter("mac_address", FilterOperator.EQ, macAddress)
        }

        val realtimeJob = launch(ioDispatcher) {
            channel.subscribe()
            changeFlow.collect { action ->
                Log.i(
                    "REPOSITORY SUPABASE SEVERE MONITOR",
                    "New Severe Monitor: $macAddress - $action"
                )

                try {
                    when (action) {
                        is PostgresAction.Insert -> {
                            val entity = action.decodeRecord<SevereMonitorEntity>()
                            val newSevereMonitor = SevereMonitor(
                                macAddress = entity.macAddress ?: "",
                                isSevere = entity.isSevere ?: false,
                                severeStartTime = entity.severeStartTime ?: ""
                            )
                            send(Result.Success(newSevereMonitor))
                        }

                        is PostgresAction.Update -> {
                            val entity = action.decodeRecord<SevereMonitorEntity>()
                            val updatedSevereMonitor = SevereMonitor(
                                macAddress = entity.macAddress ?: "",
                                isSevere = entity.isSevere ?: false,
                                severeStartTime = entity.severeStartTime ?: ""
                            )
                            send(Result.Success(updatedSevereMonitor))
                        }

                        else -> {}
                    }
                } catch (e: Exception) {
                    send(Result.Error(e, "Failed to update severe monitor data: ${e.message}"))
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
                        "REPOSITORY SUPABASE SEVERE MONITOR",
                        "Close realtime connection for $macAddress severe monitor"
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}