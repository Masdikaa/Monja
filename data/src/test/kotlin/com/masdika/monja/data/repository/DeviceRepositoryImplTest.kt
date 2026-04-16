package com.masdika.monja.data.repository

import app.cash.turbine.test
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
import com.masdika.monja.data.Result

@OptIn(ExperimentalCoroutinesApi::class)
class DeviceRepositoryImplTest {

    private fun createFakeSupabase(mockEngine: MockEngine) = createSupabaseClient(
        supabaseUrl = "https://dummy.supabase.co",
        supabaseKey = "dummy-key"
    ) {
        install(Postgrest)
        install(Realtime)
        httpEngine = mockEngine
    }

    @Test
    fun `getDeviceStream should emit Loading then Success with correctly mapped initial data`() = runTest {
        // testDispatcher INSIDE runTest
        // to operate runTest's 'testScheduler' within it.
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)

        // ARRANGE
        val mockJson = """
            [
                {
                    "mac_address": "AA:BB:CC:11:22:33",
                    "connection_status": "Online",
                    "last_seen": "2026-04-10T22:00:00Z",
                    "created_at": "2026-04-10T20:00:00Z"
                },
                {
                    "mac_address": "DD:EE:FF:44:55:66",
                    "connection_status": "Offline",
                    "last_seen": "2026-04-10T21:55:00Z",
                    "created_at": "2026-04-10T20:00:00Z"
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

        val repository = DeviceRepositoryImpl(
            supabase = createFakeSupabase(mockEngine),
            ioDispatcher = testDispatcher
        )

        // ACT & ASSERT
        repository.getDeviceStream().test {
            val loadingItem = awaitItem()
            assertTrue(loadingItem is Result.Loading, "First item emitted must be Result.Loading")

            val successItem = awaitItem()
            assertTrue(successItem is Result.Success, "Second item emitted must be Result.Success")

            val devices = (successItem as Result.Success).data
            assertEquals(2, devices.size, "Must map exactly 2 devices from JSON")

            val device1 = devices[0]
            assertEquals("AA:BB:CC:11:22:33", device1.macAddress)
            assertTrue(device1.isOnline, "Device 1 should be online")

            val device2 = devices[1]
            assertEquals("DD:EE:FF:44:55:66", device2.macAddress)
            assertTrue(!device2.isOnline, "Device 2 should be offline")

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getDeviceStream should emit multiple Success results due to Polling Job`() = runTest {
        // testDispatcher INSIDE runTest
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)

        // ARRANGE
        val mockJson = """
            [
                {
                    "mac_address": "AA:BB:CC:11:22:33",
                    "connection_status": "Online",
                    "last_seen": "2026-04-10T22:00:00Z",
                    "created_at": "2026-04-10T20:00:00Z"
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

        val repository = DeviceRepositoryImpl(
            supabase = createFakeSupabase(mockEngine),
            ioDispatcher = testDispatcher
        )

        // ACT & ASSERT
        repository.getDeviceStream().test {
            // Emit 1: Loading
            awaitItem()

            // Emit 2: Initial Fetch
            awaitItem()

            // Emisi 3: Occurs because pollingJob is delaying(3000)
            // Since it uses the same testScheduler, a virtual 3 seconds of time passed instantly!
            val pollingItem = awaitItem()
            assertTrue(pollingItem is Result.Success, "Third item must be Success emitted by Polling Job")

            val devicesFromPolling = (pollingItem as Result.Success).data
            assertEquals(1, devicesFromPolling.size)

            cancelAndIgnoreRemainingEvents()
        }
    }
}