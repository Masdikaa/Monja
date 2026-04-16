package com.masdika.monja.data.repository

import app.cash.turbine.test
import com.masdika.monja.data.Result
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
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class SevereMonitorRepositoryImplTest {
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
    fun `getSevereMonitorStream should emit Loading then Success with correctly mapped data`() =
        runTest {
            // ARRANGE
            val macAddress = "AA:BB:CC:DD:EE:FF"

            val mockJson = """
                [
                    {
                        "mac_address": "AA:BB:CC:DD:EE:FF",
                        "severe_start_time": "2026-04-10T20:00:00Z",
                        "is_severe": true,
                        "updated_at": "2026-04-10T20:07:00Z"
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

            val repository = SevereMonitorRepositoryImpl(
                supabase = createFakeSupabase(mockEngine),
                ioDispatcher = testDispatcher
            )

            // ACT & ASSERT
            repository.getSevereMonitorStream(macAddress).test {
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

                val monitorData = (successItem as Result.Success).data
                assertNotNull(monitorData, "Monitor data must not be null when database has record")

                assertEquals("AA:BB:CC:DD:EE:FF", monitorData.macAddress)
                assertEquals(true, monitorData.isSevere, "isSevere must be true as per JSON")
                assertEquals("2026-04-10T20:00:00Z", monitorData.severeStartTime)

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `getSevereMonitorStream should emit Success with NULL when device has no severe history`() = runTest {
        // ARRANGE
        val macAddress = "AA:BB:CC:DD:EE:FF"
        val mockJsonNull = "[]"

        val mockEngine = MockEngine {
            respond(
                content = mockJsonNull,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val repository = SevereMonitorRepositoryImpl(
            supabase = createFakeSupabase(mockEngine),
            ioDispatcher = testDispatcher
        )

        // ACT & ASSERT
        repository.getSevereMonitorStream(macAddress).test {
            val loadingItem = awaitItem()
            assertTrue(loadingItem is Result.Loading, "First item emitted must be Result.Loading")

            val successItem = awaitItem()
            assertTrue(successItem is Result.Success, "Second item emitted must be Result.Success")

            val monitorData = (successItem as Result.Success).data
            assertNull(monitorData, "Monitor data MUST be null when mock JSON returns empty array []")

            cancelAndIgnoreRemainingEvents()
        }
    }
}