package com.masdika.monja.data.repository

import android.util.Log
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
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject

class VitalRepositoryImpl @Inject constructor(
    private val supabase: SupabaseClient,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : VitalsRepository {
    private val cleanupMutex = Mutex()

    override suspend fun getAvailableVitals(macAddress: String, limit: Int): List<Vitals> {
        return withContext(ioDispatcher) {
            try {
                val entity = supabase.postgrest["vitals_log"]
                    .select {
                        filter { eq("mac_address", macAddress) }
                        order("created_at", order = Order.DESCENDING)
                        limit(limit.toLong())
                    }
                    .decodeList<VitalsEntity>()

                entity.map {
                    Vitals(
                        temperature = it.temperature ?: 0.0,
                        heartrate = it.heartrate ?: 0,
                        oxygenSaturation = it.oxygenSaturation ?: 0,
                        createdAt = it.createdAt ?: ""
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                throw e
            }
        }
    }

    override fun getVitalStream(macAddress: String): Flow<Result<List<Vitals>>> = channelFlow {
        send(Result.Loading)

        val currentVitals = mutableListOf<Vitals>()

        try {
            val initialVitalsData = getAvailableVitals(macAddress, 100)
            currentVitals.addAll(initialVitalsData)
            send(Result.Success(currentVitals.toList()))
        } catch (e: Exception) {
            send(Result.Error(e, "Failed initiating vitals data: ${e.message}"))
        }

        val channel = supabase.channel("vitals_channel_$macAddress")
        val changeFlow = channel.postgresChangeFlow<PostgresAction>(schema = "public") {
            table = "vitals_log"
            filter("mac_address", FilterOperator.EQ, macAddress)
        }

        val realtimeJob = launch(ioDispatcher) {
            channel.subscribe()
            changeFlow.collect { action ->
                Log.i("REPOSITORY SUPABASE VITALS", "New Vitals: $macAddress - $action")

                try {
                    when (action) {
                        is PostgresAction.Insert -> {
                            val entity = action.decodeRecord<VitalsEntity>()
                            val newVital = Vitals(
                                temperature = entity.temperature ?: 0.0,
                                heartrate = entity.heartrate ?: 0,
                                oxygenSaturation = entity.oxygenSaturation ?: 0,
                                createdAt = entity.createdAt ?: ""
                            )
                            currentVitals.add(0, newVital)
                            if (currentVitals.size > 150) currentVitals.removeAt(currentVitals.lastIndex)

                            send(Result.Success(currentVitals.toList()))
                        }

//                        is PostgresAction.Update -> {
//                            val entity = action.decodeRecord<VitalsEntity>()
//                            send(
//                                Result.Success(
//                                    Vitals(
//                                        temperature = entity.temperature ?: 0.0,
//                                        heartrate = entity.heartrate ?: 0,
//                                        oxygenSaturation = entity.oxygenSaturation ?: 0
//                                    )
//                                )
//                            )
//                        }

                        else -> {}
                    }
                } catch (e: Exception) {
                    send(Result.Error(e, "Failed to update vitals data: ${e.message}"))
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
                        "REPOSITORY SUPABASE VITALS",
                        "Close realtime connection for $macAddress vitals log"
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}