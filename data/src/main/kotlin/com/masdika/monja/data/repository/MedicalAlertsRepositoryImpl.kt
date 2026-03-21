package com.masdika.monja.data.repository

import android.R.attr.action
import android.util.Log
import com.masdika.monja.data.di.IoDispatcher
import com.masdika.monja.data.entity.MedicalAlertEntity
import com.masdika.monja.data.model.MedicalAlert
import com.masdika.monja.data.repository.interfaces.MedicalAlertsRepository
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

class MedicalAlertsRepositoryImpl @Inject constructor(
    private val supabase: SupabaseClient,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : MedicalAlertsRepository {
    override suspend fun getMedicalAlerts(macAddress: String): List<MedicalAlert> {
        return withContext(ioDispatcher) {
            try {
                val entities = supabase.postgrest["medical_alerts"]
                    .select {
                        filter { eq("mac_address", macAddress) }
                        order("created_at", order = Order.DESCENDING)
                    }
                    .decodeList<MedicalAlertEntity>()

                entities.map { entity ->
                    MedicalAlert(
                        id = entity.id ?: 0,
                        macAddress = entity.macAddress,
                        oldStatus = entity.oldStatus,
                        newStatus = entity.newStatus,
                        temperatureAtTime = entity.temperatureAtTime,
                        spo2AtTime = entity.spo2AtTime,
                        latitude = entity.latitude,
                        longitude = entity.longitude,
                        createdAt = entity.createdAt ?: "",
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }

    override fun getMedicalAlertsStream(macAddress: String): Flow<List<MedicalAlert>> =
        channelFlow {
            send(getMedicalAlerts(macAddress))

            val channel = supabase.channel("medical_alert_channel_$macAddress")
            val changeFlow = channel.postgresChangeFlow<PostgresAction>(schema = "public") {
                table = "medical_alert"
                filter("mac_address", FilterOperator.EQ, macAddress)
            }

            val realtimeJob = launch(ioDispatcher) {
                channel.subscribe()
                changeFlow.collect {
                    Log.i("REPO_MEDICAL_ALERT", "New Alert: $macAddress - $action")
                    send(getMedicalAlerts(macAddress))
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
                    "REPO_MEDICAL_ALERT",
                    "Close realtime connection for $macAddress medical alerts"
                )
            }
        }
}