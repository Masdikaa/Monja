package com.masdika.monja.data.repository

import com.masdika.monja.data.di.IoDispatcher
import com.masdika.monja.data.entity.LocationEntity
import com.masdika.monja.data.model.Location
import com.masdika.monja.data.repository.interfaces.LocationRepository
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val supabase: SupabaseClient,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : LocationRepository {
    override fun getLocationStream(macAddress: String): Flow<Result<Location?>> =
        flow<Result<Location?>> {
            val initialEntity = supabase.postgrest["location_log"]
                .select {
                    filter { eq("mac_address", macAddress) }
                    order("created_at", order = Order.DESCENDING)
                    limit(1)
                }.decodeSingleOrNull<LocationEntity>()

            var currentLocation: Location? = if (initialEntity != null) {
                Location(
                    latitude = initialEntity.latitude ?: "",
                    longitude = initialEntity.longitude ?: ""
                )
            } else {
                null
            }

            emit(Result.Success(currentLocation))

            val channel = supabase.realtime.channel("location_channel_$macAddress")
            val changeFlow = channel.postgresChangeFlow<PostgresAction>(schema = "public") {
                table = "location_log"
                filter("mac_address", FilterOperator.EQ, macAddress)
            }

            channel.subscribe()

            try {
                changeFlow.collect { action ->
                    when (action) {
                        is PostgresAction.Insert -> {
                            val newEntity = action.decodeRecord<LocationEntity>()
                            currentLocation = Location(
                                latitude = newEntity.latitude ?: "",
                                longitude = newEntity.longitude ?: ""
                            )
                            emit(Result.Success(currentLocation))
                        }

                        else -> {}
                    }
                }
            } finally {
                supabase.realtime.removeChannel(channel)
            }
        }
            .onStart { emit(Result.Loading) }
            .catch { e -> emit(Result.Error(e, "Connection Lost: ${e.localizedMessage}")) }
            .flowOn(ioDispatcher)
}