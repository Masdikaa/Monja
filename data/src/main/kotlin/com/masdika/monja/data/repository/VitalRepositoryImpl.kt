package com.masdika.monja.data.repository

import android.util.Log
import com.masdika.monja.data.di.IoDispatcher
import com.masdika.monja.data.entity.VitalsEntity
import com.masdika.monja.data.model.Vitals
import com.masdika.monja.data.repository.interfaces.VitalsRepository
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

class VitalRepositoryImpl @Inject constructor(
    private val supabase: SupabaseClient,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : VitalsRepository {
    override suspend fun getAvailableVitals(macAddress: String): Vitals? {
        return withContext(ioDispatcher) {
            try {
                val entity = supabase.postgrest["vitals_log"]
                    .select {
                        filter { eq("mac_address", macAddress) }
                        order("created_at", order = Order.DESCENDING)
                        limit(1)
                    }
                    .decodeSingleOrNull<VitalsEntity>()

                entity?.let {
                    Vitals(
                        temperature = it.temperature,
                        heartrate = it.heartrate,
                        oxygenSaturation = it.oxygenSaturation
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    override fun getVitalStream(macAddress: String): Flow<Vitals?> = channelFlow {
        send(getAvailableVitals(macAddress))

        val channel = supabase.channel("vitals_channel_$macAddress")
        val changeFlow = channel.postgresChangeFlow<PostgresAction>(schema = "public") {
            table = "vitals_log"
            filter("mac_address", FilterOperator.EQ, macAddress)
        }

        val realtimeJob = launch(ioDispatcher) {
            channel.subscribe()
            changeFlow.collect { action ->
                Log.i("REPOSITORY SUPABASE VITALS", "New Vitals: $macAddress - $action")
                send(getAvailableVitals(macAddress))
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
                "REPOSITORY SUPABASE VITALS",
                "Close realtime connection for $macAddress vitals log"
            )
        }

    }
}