package com.masdika.monja.data.repository

import android.util.Log
import com.masdika.monja.data.entity.HealthStatusEntity
import com.masdika.monja.data.model.HealthStatus
import com.masdika.monja.data.repository.interfaces.HealthStatusRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.realtime.realtime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class HealthStatusRepositoryImpl @Inject constructor(
    private val supabase: SupabaseClient
) : HealthStatusRepository {
    override suspend fun getAvailableHealthStatuses(macAddress: String): HealthStatus? {
        return try {
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
            null
        }
    }

    override fun getHealthStatusesStream(macAddress: String): Flow<HealthStatus?> = channelFlow {
        send(getAvailableHealthStatuses(macAddress))

        val channel = supabase.channel("health_status_channel_$macAddress")
        val changeFlow = channel.postgresChangeFlow<PostgresAction>(schema = "public") {
            table = "device_health_status"
            filter("mac_address", FilterOperator.EQ, macAddress)
        }

        val realtimeJob = launch(Dispatchers.IO) {
            channel.subscribe()
            changeFlow.collect { action ->
                Log.i(
                    "REPOSITORY SUPABASE HEALTH STATUS",
                    "New Health Status: $macAddress - $action"
                )
                send(getAvailableHealthStatuses(macAddress))
            }
        }

        awaitClose {
            realtimeJob.cancel()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    supabase.realtime.removeChannel(channel)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            Log.i(
                "REPOSITORY SUPABASE HEALTH STATUS",
                "Close realtime connection for $macAddress device health status"
            )
        }
    }
}