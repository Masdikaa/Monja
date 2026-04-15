package com.masdika.monja.data.repository

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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import timber.log.Timber
import javax.inject.Inject

class HealthStatusRepositoryImpl @Inject constructor(
    private val supabase: SupabaseClient,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : HealthStatusRepository {
    override fun getHealthStatusesStream(macAddress: String): Flow<Result<HealthStatus?>> =
        flow<Result<HealthStatus?>> {
            Timber.d("Fetching initial health status for $macAddress...")
            val initialEntity = supabase.postgrest["device_health_status"]
                .select {
                    filter { eq("mac_address", macAddress) }
                    order("updated_at", order = Order.DESCENDING)
                    limit(1)
                }
                .decodeSingleOrNull<HealthStatusEntity>()

            var currentHealthStatus: HealthStatus? = if (initialEntity != null) {
                Timber.d("Initial health status found: ${initialEntity.status}")
                HealthStatus(
                    status = initialEntity.status
                )
            } else {
                Timber.d("No initial health status found.")
                null
            }

            emit(Result.Success(currentHealthStatus))

            Timber.d("Setting up channel subscription: health_status_channel_$macAddress")
            val channel = supabase.channel("health_status_channel_$macAddress")
            val changeFlow = channel.postgresChangeFlow<PostgresAction>(schema = "public") {
                table = "device_health_status"
                filter("mac_address", FilterOperator.EQ, macAddress)
            }

            channel.subscribe()

            try {
                changeFlow.collect { action ->
                    Timber.d("Received Realtime action: ${action::class.simpleName}")
                    when (action) {
                        is PostgresAction.Insert -> {
                            val newEntity = action.decodeRecord<HealthStatusEntity>()
                            currentHealthStatus = HealthStatus(
                                status = newEntity.status
                            )
                            emit(Result.Success(currentHealthStatus))
                        }

                        is PostgresAction.Update -> {
                            val updatedEntity = action.decodeRecord<HealthStatusEntity>()
                            currentHealthStatus = HealthStatus(
                                status = updatedEntity.status
                            )
                            emit(Result.Success(currentHealthStatus))
                        }

                        else -> {}
                    }
                }
            } finally {
                Timber.d("Closing stream. Removing Realtime channel for $macAddress...")
                supabase.realtime.removeChannel(channel)
            }
        }
            .onStart {
                Timber.d("Starting health status stream...")
                emit(Result.Loading)
            }
            .catch { e ->
                Timber.e(e, "Error observing health status stream for $macAddress.")
                emit(Result.Error(e, "Connection Lost: ${e.localizedMessage}"))
            }
            .flowOn(ioDispatcher)
}