package com.masdika.monja.data.repository

import com.masdika.monja.data.di.IoDispatcher
import com.masdika.monja.data.entity.SevereMonitorEntity
import com.masdika.monja.data.model.SevereMonitor
import com.masdika.monja.data.repository.interfaces.SevereMonitorRepository
import com.masdika.monja.data.Result
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

class SevereMonitorRepositoryImpl @Inject constructor(
    private val supabase: SupabaseClient,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : SevereMonitorRepository {
    override fun getSevereMonitorStream(macAddress: String): Flow<Result<SevereMonitor?>> =
        flow<Result<SevereMonitor?>> {
            Timber.d("Fetching initial severe monitor status for $macAddress...")
            val initialEntity = supabase.postgrest["severe_monitor"]
                .select {
                    filter { eq("mac_address", macAddress) }
                    order("updated_at", order = Order.DESCENDING)
                    limit(1)
                }
                .decodeSingleOrNull<SevereMonitorEntity>()

            var currentSevereMonitor: SevereMonitor? = if (initialEntity != null) {
                Timber.d("Initial severe status found: ${initialEntity.isSevere}")
                SevereMonitor(
                    macAddress = initialEntity.macAddress ?: "",
                    isSevere = initialEntity.isSevere ?: false,
                    severeStartTime = initialEntity.severeStartTime ?: ""
                )
            } else {
                Timber.d("No initial severe monitor status found.")
                null
            }

            emit(Result.Success(currentSevereMonitor))

            Timber.d("Setting up Realtime subscription for severe_monitor_channel_$macAddress")
            val channel = supabase.channel("severe_monitor_channel_$macAddress")
            val changeFlow = channel.postgresChangeFlow<PostgresAction>(schema = "public") {
                table = "severe_monitor"
                filter("mac_address", FilterOperator.EQ, macAddress)
            }

            channel.subscribe()

            try {
                changeFlow.collect { action ->
                    Timber.d("Received Realtime action: ${action::class.simpleName}")
                    when (action) {
                        is PostgresAction.Insert -> {
                            val newEntity = action.decodeRecord<SevereMonitorEntity>()
                            currentSevereMonitor = SevereMonitor(
                                macAddress = newEntity.macAddress ?: "",
                                isSevere = newEntity.isSevere ?: false,
                                severeStartTime = newEntity.severeStartTime ?: ""
                            )
                            emit(Result.Success(currentSevereMonitor))
                        }

                        is PostgresAction.Update -> {
                            val updatedEntity = action.decodeRecord<SevereMonitorEntity>()
                            currentSevereMonitor = SevereMonitor(
                                macAddress = updatedEntity.macAddress ?: "",
                                isSevere = updatedEntity.isSevere ?: false,
                                severeStartTime = updatedEntity.severeStartTime ?: ""
                            )
                            emit(Result.Success(currentSevereMonitor))
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
                Timber.d("Starting severe monitor stream...")
                emit(Result.Loading)
            }
            .catch { e ->
                Timber.e(e, "Error observing severe monitor stream for $macAddress.")
                emit(Result.Error(e, "Connection Lost: ${e.localizedMessage}"))
            }
            .flowOn(ioDispatcher)
}