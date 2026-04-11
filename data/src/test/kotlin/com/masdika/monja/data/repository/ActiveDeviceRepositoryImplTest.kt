package com.masdika.monja.data.repository

import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ActiveDeviceRepositoryImplTest {
    private lateinit var repository: ActiveDeviceRepositoryImpl

    @BeforeEach
    fun setUp() {
        repository = ActiveDeviceRepositoryImpl()
    }

    @Test
    fun `initial activeMacAddress should be null`() = runTest {
        // ACT & ASSERT
        repository.activeMacAddress.test {
            val initialItem = awaitItem()
            assertNull(
                initialItem,
                "Initial value of StateFlow must be null before any device is selected"
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `setActiveDevice should update activeMacAddress state continuously`() = runTest {
        // ACT & ASSERT
        repository.activeMacAddress.test {
            // First Emit must be null
            assertNull(awaitItem())

            // Selecting first device
            repository.setActiveDevice("AA:BB:CC:11:22:33")
            assertEquals(
                "AA:BB:CC:11:22:33",
                awaitItem(),
                "State must change to first device"
            )

            // Changing device
            repository.setActiveDevice("DD:EE:FF:44:55:66")
            assertEquals(
                "DD:EE:FF:44:55:66",
                awaitItem(),
                "State must change to second device"
            )

            cancelAndIgnoreRemainingEvents()
        }
    }
}