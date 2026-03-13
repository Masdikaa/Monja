package com.masdika.monja.data.repository

import android.util.Log
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val supabase: SupabaseClient
) : LocationRepository {
    override suspend fun getAvailableLocation(macAddress: String): Location? {
        return try {
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

    override fun getLocationStream(macAddress: String): Flow<Location?> = channelFlow {
        send(getAvailableLocation(macAddress))

        launch(Dispatchers.IO) {
            val channel = supabase.channel("location_channel_$macAddress")
            val changeFlow = channel.postgresChangeFlow<PostgresAction>(schema = "public") {
                table = "location_log"
                filter("mac_address", FilterOperator.EQ, macAddress)
            }

            channel.subscribe()
            changeFlow.collect { action ->
                Log.i("REPOSITORY SUPABASE LOCATION", "New Vitals: $macAddress - $action")
                send(getAvailableLocation(macAddress))
            }
        }
    }
}