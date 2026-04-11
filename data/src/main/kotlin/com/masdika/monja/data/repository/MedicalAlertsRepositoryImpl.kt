package com.masdika.monja.data.repository

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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonPrimitive
import javax.inject.Inject

class MedicalAlertsRepositoryImpl @Inject constructor(
    private val supabase: SupabaseClient,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : MedicalAlertsRepository {
    override fun getMedicalAlertsStream(macAddress: String): Flow<Result<List<MedicalAlert>>> =
        flow<Result<List<MedicalAlert>>> {
            val initialEntities = supabase.postgrest["medical_alerts"]
                .select {
                    filter { eq("mac_address", macAddress) }
                    order("created_at", order = Order.DESCENDING)
                }
                .decodeList<MedicalAlertEntity>()

            val currentMedicalAlertList = initialEntities.map { entity ->
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
            }.toMutableList()

            emit(Result.Success(currentMedicalAlertList.toList()))

            val channel = supabase.channel("medical_alert_channel_$macAddress")
            val changeFlow = channel.postgresChangeFlow<PostgresAction>(schema = "public") {
                table = "medical_alerts"
                filter("mac_address", FilterOperator.EQ, macAddress)
            }

            channel.subscribe()

            try {
                changeFlow.collect { action ->
                    when (action) {
                        is PostgresAction.Insert -> {
                            val newEntity = action.decodeRecord<MedicalAlertEntity>()
                            val newMedicalAlert = MedicalAlert(
                                id = newEntity.id ?: 0,
                                macAddress = newEntity.macAddress ?: "Unknown MAC",
                                oldStatus = newEntity.oldStatus ?: "Unknown",
                                newStatus = newEntity.newStatus ?: "Unknown",
                                temperatureAtTime = newEntity.temperatureAtTime,
                                spo2AtTime = newEntity.spo2AtTime,
                                latitude = newEntity.latitude,
                                longitude = newEntity.longitude,
                                createdAt = newEntity.createdAt ?: ""
                            )
                            currentMedicalAlertList.add(0, newMedicalAlert)
                        }

                        is PostgresAction.Delete -> {
                            val deletedId = action.oldRecord["id"]?.jsonPrimitive?.intOrNull
                            if (deletedId != null) {
                                currentMedicalAlertList.removeAll { it.id == deletedId }
                            }
                        }

                        else -> {}
                    }
                    emit(Result.Success(currentMedicalAlertList.toList()))
                }
            } finally {
                supabase.realtime.removeChannel(channel)
            }
        }
            .onStart { emit(Result.Loading) }
            .catch { e -> emit(Result.Error(e, "Connection Lost: ${e.localizedMessage}")) }
            .flowOn(ioDispatcher)

    override suspend fun deleteMedicalAlerts(macAddress: String) {
        withContext(ioDispatcher) {
            supabase.postgrest["medical_alerts"]
                .delete {
                    filter { eq("mac_address", macAddress) }
                }
        }
    }

}