package com.masdika.monja.ui.splash

import app.cash.turbine.test
import com.masdika.monja.R
import com.masdika.monja.data.repository.interfaces.DeviceRepository
import com.masdika.monja.util.UiText
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle

@OptIn(ExperimentalCoroutinesApi::class)
class SplashViewModelTest {

    @MockK
    private lateinit var deviceRepository: DeviceRepository

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel() = SplashViewModel(deviceRepository)

    @Test
    fun `initializeApp when Success updates State To Success And Navigates To Dashboard`() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        try {
            // ARRANGE
            coEvery { deviceRepository.getAvailableDevices() } returns emptyList()

            // ACT
            val viewModel = createViewModel()

            advanceUntilIdle()

            val finalState = viewModel.state.value
            assertTrue(finalState.initializationState is InitializationState.Success, "State should be Success")

            // ASSERT EVENT
            viewModel.event.test {
                val event = awaitItem()
                assertTrue(event is SplashEvent.NavigateToDashboard, "Should trigger navigation to dashboard")
                cancelAndIgnoreRemainingEvents()
            }

            coVerify(exactly = 1) { deviceRepository.getAvailableDevices() }
        } finally {
            Dispatchers.resetMain()
        }
    }

    @Test
    fun `initializeApp when Fails Max Retries updates State To Error`() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        try {
            // ARRANGE
            coEvery { deviceRepository.getAvailableDevices() } throws Exception("Connection Timeout")

            // ACT
            val viewModel = createViewModel()
            advanceUntilIdle()

            // ASSERT
            val finalState = viewModel.state.value
            assertTrue(finalState.initializationState is InitializationState.Error, "State should be Error after max retries")

            val errorUiText = (finalState.initializationState as InitializationState.Error).message as UiText.StringResource
            assertEquals(R.string.splash_error_connection, errorUiText.resId)
            assertEquals(3, finalState.retryCount, "Retry count should reach the maximum (3)")

            coVerify(exactly = 3) { deviceRepository.getAvailableDevices() }
        } finally {
            Dispatchers.resetMain()
        }
    }

    @Test
    fun `initializeApp when Fails First Time But Succeeds Second Time navigates To Dashboard`() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        try {
            // ARRANGE
            var attempt = 0
            coEvery { deviceRepository.getAvailableDevices() } answers {
                attempt++
                if (attempt == 1) throw Exception("Temporary Network Glitch")
                else emptyList()
            }

            // ACT
            val viewModel = createViewModel()
            advanceUntilIdle()

            // ASSERT
            val finalState = viewModel.state.value
            assertTrue(finalState.initializationState is InitializationState.Success, "State should be Success after retry")
            assertEquals(1, finalState.retryCount, "Retry count should be 1")

            viewModel.event.test {
                val event = awaitItem()
                assertTrue(event is SplashEvent.NavigateToDashboard, "Should navigate to dashboard after successful retry")
                cancelAndIgnoreRemainingEvents()
            }

            coVerify(exactly = 2) { deviceRepository.getAvailableDevices() }
        } finally {
            Dispatchers.resetMain()
        }
    }

    @Test
    fun `retry function triggers Initialize App Again`() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        try {
            // ARRANGE
            coEvery { deviceRepository.getAvailableDevices() } throws Exception("Timeout")
            val viewModel = createViewModel()

            advanceUntilIdle()
            coVerify(exactly = 3) { deviceRepository.getAvailableDevices() }
            coEvery { deviceRepository.getAvailableDevices() } returns emptyList()

            // ACT
            viewModel.retry()
            advanceUntilIdle()

            // ASSERT
            val finalState = viewModel.state.value
            assertTrue(finalState.initializationState is InitializationState.Success, "State should recover to Success after manual retry")
            coVerify(exactly = 4) { deviceRepository.getAvailableDevices() }
        } finally {
            Dispatchers.resetMain()
        }
    }
}