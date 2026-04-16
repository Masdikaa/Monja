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
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)

class HealthStatusRepositoryImplTest {
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
    fun `getHealthStatusesStream should emit Loading then Success with correctly mapped data`() =
        runTest {
            // ARRANGE
            val macAddress = "AA:BB:CC:DD:EE:FF"
            val mockJson = """
            [
                {
                    "mac_address": "AA:BB:CC:DD:EE:FF",
                    "status": "Normal",
                    "updated_at": "2026-04-10T20:00:00Z"
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

            val repository = HealthStatusRepositoryImpl(
                supabase = createFakeSupabase(mockEngine),
                ioDispatcher = testDispatcher
            )

            // ACT & ASSERT
            repository.getHealthStatusesStream(macAddress).test {
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

                val healthData = (successItem as Result.Success).data
                assertNotNull(
                    healthData,
                    "Health status data must not be null when database has record"
                )

                assertEquals("Normal", healthData?.status, "Status must be mapped correctly")

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `getHealthStatusesStream should emit Success with NULL when device has no health status history`() =
        runTest {
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

            val repository = HealthStatusRepositoryImpl(
                supabase = createFakeSupabase(mockEngine),
                ioDispatcher = testDispatcher
            )

            // ACT & ASSERT
            repository.getHealthStatusesStream(macAddress).test {
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

                val healthData = (successItem as Result.Success).data
                assertNull(
                    healthData,
                    "Health status data MUST be null when mock JSON returns empty array []"
                )

                cancelAndIgnoreRemainingEvents()
            }
        }
}