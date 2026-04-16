package com.masdika.monja.ui.history

import app.cash.turbine.test
import com.masdika.monja.R
import com.masdika.monja.data.model.MedicalAlert
import com.masdika.monja.data.repository.interfaces.ActiveDeviceRepository
import com.masdika.monja.data.repository.interfaces.MedicalAlertsRepository
import com.masdika.monja.data.Result
import com.masdika.monja.util.UiText
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@Suppress("UnusedFlow")
@OptIn(ExperimentalCoroutinesApi::class)
class HistoryViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()

    @MockK
    private lateinit var medicalAlertsRepository: MedicalAlertsRepository

    @MockK
    private lateinit var activeDeviceRepository: ActiveDeviceRepository

    private lateinit var activeMacAddressFlow: MutableStateFlow<String?>

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)

        activeMacAddressFlow = MutableStateFlow(null)
        every { activeDeviceRepository.activeMacAddress } returns activeMacAddressFlow
        every { activeDeviceRepository.setActiveDevice(any()) } answers {
            activeMacAddressFlow.value = firstArg()
        }

        every { medicalAlertsRepository.getMedicalAlertsStream(any()) } returns flowOf(Result.Loading)
        coEvery { medicalAlertsRepository.deleteMedicalAlerts(any()) } returns Unit
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel() = HistoryViewModel(
        medicalAlertsRepository,
        activeDeviceRepository
    )

    @Test
    fun `historyStream when Active Mac Is Set updates State With Medical Alerts`() = runTest {
        // ARRANGE
        val macAddress = "MAC_HISTORY"
        activeMacAddressFlow.value = macAddress

        val dummyAlert = MedicalAlert(
            id = 1,
            macAddress = macAddress,
            oldStatus = "Normal",
            newStatus = "Critical",
            temperatureAtTime = 38.0,
            spo2AtTime = 95,
            latitude = "0",
            longitude = "0",
            createdAt = "2026-04-11"
        )
        every { medicalAlertsRepository.getMedicalAlertsStream(macAddress) } returns flowOf(
            Result.Success(
                listOf(dummyAlert)
            )
        )

        // ACT
        val viewModel = createViewModel()

        // ASSERT
        viewModel.state.test {
            val state = awaitItem()
            val result = state.statusState as Result.Success

            assertEquals(1, result.data.size)
            assertEquals("Critical", result.data[0].newStatus)
            assertEquals(macAddress, state.macAddress)
        }
    }

    @Test
    fun `deleteAllMedicalAlerts when Success hides Dialog Triggers Refresh And Shows Success Snackbar`() =
        runTest {
            // ARRANGE
            val macAddress = "MAC_HISTORY"
            activeMacAddressFlow.value = macAddress
            val viewModel = createViewModel()

            viewModel.showDeleteConfirmation()

            // ACT
            viewModel.deleteAllMedicalAlerts()

            // ASSERT
            viewModel.state.test {
                val state = awaitItem()
                assertFalse(
                    state.showDeleteDialog,
                    "Dialog should be hidden after successful delete"
                )
            }

            viewModel.event.test {
                val event = awaitItem()
                assertTrue(event is HistoryScreenEvent.ShowSnackbar)

                val uiText =
                    (event as HistoryScreenEvent.ShowSnackbar).message as UiText.StringResource
                assertEquals(R.string.success_delete_history, uiText.resId)
                assertEquals(macAddress, uiText.args[0])
            }

            coVerify(exactly = 1) { medicalAlertsRepository.deleteMedicalAlerts(macAddress) }
            verify(exactly = 2) { medicalAlertsRepository.getMedicalAlertsStream(macAddress) }
        }

    // EDGE CASES
    @Test
    fun `historyStream when Active Mac Is Null emits Empty List Success`() = runTest {
        // ARRANGE
        activeMacAddressFlow.value = null

        // ACT
        val viewModel = createViewModel()

        // ASSERT
        viewModel.state.test {
            val state = awaitItem()
            val result = state.statusState as Result.Success

            assertTrue(result.data.isEmpty(), "Return empty list when MacAddress not found")
            assertEquals("", state.macAddress)
        }
    }

    @Test
    fun `deleteAllMedicalAlerts when Exception Thrown hides Dialog And Shows Failed Snackbar`() =
        runTest {
            // ARRANGE
            val macAddress = "MAC_HISTORY"
            activeMacAddressFlow.value = macAddress

            coEvery { medicalAlertsRepository.deleteMedicalAlerts(macAddress) } throws Exception("Database timeout")

            val viewModel = createViewModel()
            viewModel.showDeleteConfirmation()

            // ACT
            viewModel.deleteAllMedicalAlerts()

            // ASSERT
            viewModel.state.test {
                val state = awaitItem()
                assertFalse(
                    state.showDeleteDialog,
                    "Dialogs should remain hidden even if an error occurs"
                )
            }

            viewModel.event.test {
                val event = awaitItem()
                assertTrue(event is HistoryScreenEvent.ShowSnackbar)

                val uiText =
                    (event as HistoryScreenEvent.ShowSnackbar).message as UiText.StringResource
                assertEquals(R.string.error_delete_history, uiText.resId)
            }
        }
}