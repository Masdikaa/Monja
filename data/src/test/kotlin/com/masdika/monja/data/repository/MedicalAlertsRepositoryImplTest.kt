package com.masdika.monja.data.repository

import app.cash.turbine.test
import com.masdika.monja.data.utils.Result
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

@OptIn(ExperimentalCoroutinesApi::class)
class MedicalAlertsRepositoryImplTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private fun createFakeSupabase(mockEngine: MockEngine) = createSupabaseClient(
        supabaseUrl = "https://dummy.supabase.co",
        supabaseKey = "dummy-key"
    ) {
        install(Postgrest)
        install(Realtime)
        httpEngine = mockEngine
    }

    @Test
    fun `getMedicalAlertsStream should emit Loading then Success with correctly mapped data`() =
        runTest {
            // ARRANGE
            val macAddress = "AA:BB:CC:DD:EE:FF"
            val mockJson = """
            [
                {
                    "id": 101, 
                    "mac_address": "AA:BB:CC:DD:EE:FF", 
                    "old_status": "Normal", 
                    "new_status": "Critical", 
                    "temp_at_time": 39.5, 
                    "spo2_at_time": 88, 
                    "lat": "-6.200000", 
                    "long": "106.816666", 
                    "created_at": "2026-04-10T15:00:00Z"
                }
            ]
        """.trimIndent()
            val mockEngine = MockEngine {
                respond(
                    content = mockJson,
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
            val repository = MedicalAlertsRepositoryImpl(
                supabase = createFakeSupabase(mockEngine),
                ioDispatcher = testDispatcher
            )

            // ACT & ASSERT
            repository.getMedicalAlertsStream(macAddress).test {
                val loadingItem = awaitItem()
                assertTrue(
                    loadingItem is Result.Loading,
                    "First item emitted must be Result.Loading"
                )

                val successItem = awaitItem()
                assertTrue(
                    successItem is Result.Success,
                    "Second item emitted must be Result.Success"
                )

                val alerts = (successItem as Result.Success).data
                assertEquals(1, alerts.size, "Success data size must be equal to 1")

                val firstAlert = alerts[0]
                assertEquals(101, firstAlert.id)
                assertEquals("Critical", firstAlert.newStatus)
                assertEquals(39.5, firstAlert.temperatureAtTime)
                assertEquals(88, firstAlert.spo2AtTime)

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `deleteMedicalAlerts should execute successfully without throwing exceptions`() = runTest {
        // ARRANGE
        val macAddress = "AA:BB:CC:DD:EE:FF"
        val mockEngine = MockEngine {
            respond(
                content = "",
                status = HttpStatusCode.NoContent, // 204 No Content represent successful delete
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        val repository = MedicalAlertsRepositoryImpl(
            supabase = createFakeSupabase(mockEngine),
            ioDispatcher = testDispatcher
        )

        // ACT & ASSERT
        assertDoesNotThrow("Delete Function must success without throws exception") {
            repository.deleteMedicalAlerts(macAddress)
        }
    }
}