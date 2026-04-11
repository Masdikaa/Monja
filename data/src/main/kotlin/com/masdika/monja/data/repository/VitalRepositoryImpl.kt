package com.masdika.monja.data.repository

import com.masdika.monja.data.di.IoDispatcher
import com.masdika.monja.data.entity.VitalsEntity
import com.masdika.monja.data.model.Vitals
import com.masdika.monja.data.repository.interfaces.VitalsRepository
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

class VitalRepositoryImpl @Inject constructor(
    private val supabase: SupabaseClient,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : VitalsRepository {
    private val repositoryScope = CoroutineScope(ioDispatcher + SupervisorJob())
    private val activeStreams = ConcurrentHashMap<String, SharedFlow<Result<List<Vitals>>>>()

    override fun getVitalStream(macAddress: String): Flow<Result<List<Vitals>>> {
        return activeStreams.getOrPut(macAddress) {
            createStream(macAddress).shareIn(
                scope = repositoryScope,
                started = SharingStarted.WhileSubscribed(10000),
                replay = 1
            )
        }
    }

    private fun createStream(macAddress: String): Flow<Result<List<Vitals>>> =
        flow<Result<List<Vitals>>> {
            val initialEntities = supabase.postgrest["vitals_log"]
                .select {
                    filter { eq("mac_address", macAddress) }
                    order("created_at", order = Order.DESCENDING)
                }
                .decodeList<VitalsEntity>()

            val currentList = initialEntities.map {
                Vitals(
                    temperature = it.temperature ?: 0.0,
                    heartrate = it.heartrate ?: 0,
                    oxygenSaturation = it.oxygenSaturation ?: 0,
                    createdAt = it.createdAt ?: ""
                )
            }.toMutableList()

            emit(Result.Success(currentList.toList()))

            val channel = supabase.realtime.channel("vitals_channel_$macAddress")
            val changeFlow = channel.postgresChangeFlow<PostgresAction>(schema = "public") {
                table = "vitals_log"
                filter("mac_address", FilterOperator.EQ, macAddress)
            }

            channel.subscribe()

            try {
                changeFlow.collect { action ->
                    when (action) {
                        is PostgresAction.Insert -> {
                            val newEntity = action.decodeRecord<VitalsEntity>()
                            val newVitals = Vitals(
                                temperature = newEntity.temperature ?: 0.0,
                                heartrate = newEntity.heartrate ?: 0,
                                oxygenSaturation = newEntity.oxygenSaturation ?: 0,
                                createdAt = newEntity.createdAt ?: ""
                            )
                            currentList.add(0, newVitals)
                        }

                        else -> {}
                    }
                    emit(Result.Success(currentList.toList()))
                }
            } finally {
                supabase.realtime.removeChannel(channel)
            }
        }
            .onStart { emit(Result.Loading) }
            .catch { e -> emit(Result.Error(e, "Connection Lost: ${e.localizedMessage}")) }
            .flowOn(ioDispatcher)
}