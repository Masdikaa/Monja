package com.masdika.monja.data.repository

import android.util.Log
import com.masdika.monja.data.di.IoDispatcher
import com.masdika.monja.data.entity.LocationEntity
import com.masdika.monja.data.model.Location
import com.masdika.monja.data.repository.interfaces.LocationRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.realtime.realtime
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val supabase: SupabaseClient,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : LocationRepository {
    override suspend fun getAvailableLocation(macAddress: String): Location? {
        return withContext(ioDispatcher) {
            try {
                val entity = supabase.postgrest["location_log"]
                    .select {
                        filter { eq("mac_address", macAddress) }
                        order("created_at", order = Order.DESCENDING)
                        limit(1)
                    }
                    .decodeSingleOrNull<LocationEntity>()

                entity?.let {
                    Location(
                        latitude = it.latitude,
                        longitude = it.longitude
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    override fun getLocationStream(macAddress: String): Flow<Location?> = channelFlow {
        send(getAvailableLocation(macAddress))

        val channel = supabase.channel("location_channel_$macAddress")
        val changeFlow = channel.postgresChangeFlow<PostgresAction>(schema = "public") {
            table = "location_log"
            filter("mac_address", FilterOperator.EQ, macAddress)
        }

        val realtimeJob = launch(ioDispatcher) {
            channel.subscribe()
            changeFlow.collect { action ->
                Log.i("REPOSITORY SUPABASE LOCATION", "New Location: $macAddress - $action")
                send(getAvailableLocation(macAddress))
            }
        }

        awaitClose {
            realtimeJob.cancel()
            CoroutineScope(ioDispatcher).launch {
                try {
                    supabase.realtime.removeChannel(channel)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            Log.i(
                "REPOSITORY SUPABASE LOCATION",
                "Close realtime connection for $macAddress location log"
            )
        }

    }
}