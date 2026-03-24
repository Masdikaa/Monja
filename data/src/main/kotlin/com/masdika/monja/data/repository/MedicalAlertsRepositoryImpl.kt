package com.masdika.monja.data.repository

import android.util.Log
import com.masdika.monja.data.di.IoDispatcher
import com.masdika.monja.data.entity.MedicalAlertEntity
import com.masdika.monja.data.model.MedicalAlert
import com.masdika.monja.data.repository.interfaces.MedicalAlertsRepository
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

class MedicalAlertsRepositoryImpl @Inject constructor(
    private val supabase: SupabaseClient,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : MedicalAlertsRepository {
    private val cleanupMutex = Mutex()

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
                        macAddress = entity.macAddress ?: "Unknown MAC",
                        oldStatus = entity.oldStatus ?: "Unknown",
                        newStatus = entity.newStatus ?: "Unknown",
                        temperatureAtTime = entity.temperatureAtTime,
                        spo2AtTime = entity.spo2AtTime,
                        latitude = entity.latitude,
                        longitude = entity.longitude,
                        createdAt = entity.createdAt ?: ""
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                throw e
            }
        }
    }

    override fun getMedicalAlertsStream(
        macAddress: String
    ): Flow<Result<List<MedicalAlert>>> = channelFlow {
        send(Result.Loading)

        val currentMedicalAlerts = mutableListOf<MedicalAlert>()

        try {
            val initialMedicalAlertsData = getMedicalAlerts(macAddress)
            currentMedicalAlerts.addAll(initialMedicalAlertsData)
            send(Result.Success(currentMedicalAlerts.toList()))
        } catch (e: Exception) {
            send(Result.Error(e, "Failed initiating medical alerts data: ${e.message}"))
        }

        val channel = supabase.channel("medical_alert_channel_$macAddress")
        val changeFlow = channel.postgresChangeFlow<PostgresAction>(schema = "public") {
            table = "medical_alerts"
            filter("mac_address", FilterOperator.EQ, macAddress)
        }

        val realtimeJob = launch(ioDispatcher) {
            channel.subscribe()
            changeFlow.collect { action ->
                Log.i("REPO_MEDICAL_ALERT", "New Alert: $macAddress")
                try {
                    var isListUpdated = false
                    when (action) {
                        is PostgresAction.Insert -> {
                            val entity = action.decodeRecord<MedicalAlertEntity>()

                            val newMedicalAlerts = MedicalAlert(
                                id = entity.id ?: 0,
                                macAddress = entity.macAddress ?: "Unknown MAC",
                                oldStatus = entity.oldStatus ?: "Unknown",
                                newStatus = entity.newStatus ?: "Unknown",
                                temperatureAtTime = entity.temperatureAtTime,
                                spo2AtTime = entity.spo2AtTime,
                                latitude = entity.latitude,
                                longitude = entity.longitude,
                                createdAt = entity.createdAt ?: ""
                            )

                            currentMedicalAlerts.add(0, newMedicalAlerts)
                            isListUpdated = true
                        }

                        else -> {}
                    }
                    if (isListUpdated) {
                        send(Result.Success(currentMedicalAlerts.toList()))
                    }
                } catch (e: Exception) {
                    send(Result.Error(e, "Failed to update medical alerts data: ${e.message}"))
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
                        "REPO_MEDICAL_ALERT",
                        "Close realtime connection for $macAddress medical alerts"
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}