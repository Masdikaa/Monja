package com.masdika.monja.ui.analytic

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import app.cash.turbine.test
import com.masdika.monja.data.model.Vitals
import com.masdika.monja.data.repository.interfaces.VitalsRepository
import com.masdika.monja.data.Result
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AnalyticViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()

    @MockK
    private lateinit var vitalRepository: VitalsRepository

    @MockK
    private lateinit var savedStateHandle: SavedStateHandle

    private val testMacAddress = "MAC_ANALYTIC"
    private val testVitalType = "HeartRate"

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)

        mockkStatic("androidx.navigation.SavedStateHandleKt")

        every { savedStateHandle.toRoute<AnalyticRoute>() } returns AnalyticRoute(
            macAddress = testMacAddress,
            vitalType = testVitalType
        )

        every { vitalRepository.getVitalStream(any()) } returns flowOf(Result.Loading)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
        // Unmock to prevent interference with other test
        unmockkStatic("androidx.navigation.SavedStateHandleKt")
    }

    private fun createViewModel() = AnalyticViewModel(
        savedStateHandle = savedStateHandle,
        vitalRepository = vitalRepository
    )

    // =========================================================================
    // HAPPY PATHS
    // =========================================================================

    @Test
    fun `init extracts Arguments From Saved State Handle correctly`() = runTest {
        // ARRANGE
        every { vitalRepository.getVitalStream(testMacAddress) } returns flowOf(Result.Loading)

        // ACT
        val viewModel = createViewModel()

        // ASSERT
        assertEquals(testMacAddress, viewModel.macAddress)
        assertEquals(testVitalType, viewModel.vitalType)
    }

    @Test
    fun `vitalsStream when Success updates Vital State With Data`() = runTest {
        // ARRANGE
        val dummyVitals = listOf(
            Vitals(
                temperature = 36.5,
                heartrate = 80,
                oxygenSaturation = 98,
                createdAt = "2026-04-11"
            )
        )
        every { vitalRepository.getVitalStream(testMacAddress) } returns flowOf(
            Result.Success(
                dummyVitals
            )
        )

        // ACT
        val viewModel = createViewModel()

        // ASSERT
        viewModel.state.test {
            val state = awaitItem()
            val result = state.vitalState as Result.Success

            assertEquals(1, result.data.size)
            assertEquals(80, result.data[0].heartrate)
        }
    }

    @Test
    fun `vitalsStream when Error updates Vital State With Exception`() = runTest {
        // ARRANGE
        val errorMessage = "Failed to fetch analytic data"
        every { vitalRepository.getVitalStream(testMacAddress) } returns flowOf(
            Result.Error(
                Exception(),
                errorMessage
            )
        )

        // ACT
        val viewModel = createViewModel()

        // ASSERT
        viewModel.state.test {
            val state = awaitItem()

            assertTrue(state.vitalState is Result.Error)
            assertEquals(errorMessage, (state.vitalState as Result.Error).message)
        }
    }
}