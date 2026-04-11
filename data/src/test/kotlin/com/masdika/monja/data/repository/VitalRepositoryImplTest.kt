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
import org.junit.jupiter.api.Assertions.assertNotSame
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class VitalRepositoryImplTest {

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
    fun `getVitalStream should emit Loading then Success with correctly mapped initial data`() =
        runTest {
            // ARRANGE
            val mockJson = """
            [
                {
                    "id": 1, 
                    "mac_address": "AA:BB:CC:DD:EE:FF", 
                    "temperature": 36.5, 
                    "heartrate": 110, 
                    "oxygen_saturation": 98, 
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
            val repository = VitalRepositoryImpl(
                supabase = createFakeSupabase(mockEngine),
                ioDispatcher = testDispatcher
            )

            // ACT & ASSERT
            repository.getVitalStream("AA:BB:CC:DD:EE:FF").test {
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

                val vitalsList = (successItem as Result.Success).data
                assertEquals(1, vitalsList.size, "Success data size must be equal to 1")

                val firstVital = vitalsList[0]
                assertEquals(
                    36.5,
                    firstVital.temperature,
                    "Temperature must map to Double correctly"
                )
                assertEquals(110, firstVital.heartrate, "Heartrate must map to Int correctly")
                assertEquals(
                    98,
                    firstVital.oxygenSaturation,
                    "Oxygen Saturation must map to Int correctly"
                )

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `getVitalStream should cache and return the EXACT SAME SharedFlow for the same MAC Address`() =
        runTest {
            // ARRANGE
            val mockEngine = MockEngine {
                respond(
                    content = "[]",
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
            val repository = VitalRepositoryImpl(
                supabase = createFakeSupabase(mockEngine),
                ioDispatcher = testDispatcher
            )
            val mac1 = "AA:BB:CC:DD:EE:FF"
            val mac2 = "11:22:33:44:55:66"

            // ACT & ASSERT
            val stream1ForMac1 = repository.getVitalStream(mac1)
            val stream2ForMac1 = repository.getVitalStream(mac1)
            val streamForMac2 = repository.getVitalStream(mac2)

            assertSame(
                stream1ForMac1,
                stream2ForMac1,
                "Architectural Failure: Calls to the same MAC must return exactly the same SharedFlow instance!"
            )
            assertNotSame(
                stream1ForMac1,
                streamForMac2,
                "Different MAC Addresses should result in different SharedFlow instances!"
            )
        }
}