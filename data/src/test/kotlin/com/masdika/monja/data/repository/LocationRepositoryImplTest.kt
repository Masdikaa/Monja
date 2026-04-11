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
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class LocationRepositoryImplTest {

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
    fun `getLocationStream should emit Success with Location object when database has data`() =
        runTest {
            // ARRANGE
            val mockJson = """
            [
                {
                    "id": 1, 
                    "mac_address": "AA:BB:CC:DD:EE:FF", 
                    "latitude": "-6.200000", 
                    "longitude": "106.816666", 
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
            val repository = LocationRepositoryImpl(
                supabase = createFakeSupabase(mockEngine),
                ioDispatcher = testDispatcher
            )

            // ACT & ASSERT
            repository.getLocationStream("AA:BB:CC:DD:EE:FF").test {
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

                val location = (successItem as Result.Success).data
                assertNotNull(location, "Location must be not null")
                assertEquals("-6.200000", location?.latitude)
                assertEquals("106.816666", location?.longitude)

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `getLocationStream should emit Success with NULL when device has no location history`() =
        runTest {
            // ARRANGE
            val mockJsonNull = "[]"
            val mockEngineNullResponse = MockEngine {
                respond(
                    content = mockJsonNull,
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
            val repository = LocationRepositoryImpl(
                supabase = createFakeSupabase(mockEngineNullResponse),
                ioDispatcher = testDispatcher
            )

            // ACT & ASSERT
            repository.getLocationStream("AA:BB:CC:DD:EE:FF").test {
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

                val location = (successItem as Result.Success).data
                assertNull(location, "Location Must be NULL")

                cancelAndIgnoreRemainingEvents()
            }
        }
}