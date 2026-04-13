package com.masdika.monja.ui.dashboard

import app.cash.turbine.test
import com.masdika.monja.R
import com.masdika.monja.data.model.Device
import com.masdika.monja.data.model.HealthStatus
import com.masdika.monja.data.model.Location
import com.masdika.monja.data.model.SevereMonitor
import com.masdika.monja.data.model.Vitals
import com.masdika.monja.data.repository.interfaces.ActiveDeviceRepository
import com.masdika.monja.data.repository.interfaces.DeviceRepository
import com.masdika.monja.data.repository.interfaces.HealthStatusRepository
import com.masdika.monja.data.repository.interfaces.LocationRepository
import com.masdika.monja.data.repository.interfaces.SevereMonitorRepository
import com.masdika.monja.data.repository.interfaces.VitalsRepository
import com.masdika.monja.data.utils.Result
import com.masdika.monja.util.UiText
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
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
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()

    @MockK
    private lateinit var deviceRepository: DeviceRepository

    @MockK
    private lateinit var vitalRepository: VitalsRepository

    @MockK
    private lateinit var locationRepository: LocationRepository

    @MockK
    private lateinit var healthStatusRepository: HealthStatusRepository

    @MockK
    private lateinit var activeDeviceRepository: ActiveDeviceRepository

    @MockK
    private lateinit var severeMonitorRepository: SevereMonitorRepository

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

        every { deviceRepository.getDeviceStream() } returns flowOf(Result.Loading)
        every { vitalRepository.getVitalStream(any()) } returns flowOf(Result.Loading)
        every { locationRepository.getLocationStream(any()) } returns flowOf(Result.Loading)
        every { healthStatusRepository.getHealthStatusesStream(any()) } returns flowOf(Result.Loading)
        every { severeMonitorRepository.getSevereMonitorStream(any()) } returns flowOf(Result.Loading)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel() = DashboardViewModel(
        deviceRepository,
        vitalRepository,
        locationRepository,
        healthStatusRepository,
        activeDeviceRepository,
        severeMonitorRepository
    )

    // DEVICE OBSERVATION TESTS
    @Test
    fun `deviceStream when Success updates State With Sorted Devices And Sets Active Mac`() = runTest {
        // ARRANGE
        val device1 = Device("MAC1", false, "2026-04-10T10:00:00Z", "2026-04-10T10:00:00Z")
        val device2 = Device("MAC2", true, "2026-04-10T11:00:00Z", "2026-04-10T11:00:00Z")
        every { deviceRepository.getDeviceStream() } returns flowOf(
            Result.Success(
                listOf(
                    device1,
                    device2
                )
            )
        )

        // ACT
        val viewModel = createViewModel()

        // ASSERT
        viewModel.state.test {
            val state = awaitItem()
            val deviceResult = state.deviceState as Result.Success

            assertEquals("MAC2", deviceResult.data[0].macAddress)
            assertEquals("MAC1", deviceResult.data[1].macAddress)

            assertEquals("MAC2", state.selectedDevice?.macAddress)

            assertEquals("MAC2", activeMacAddressFlow.value)
        }
    }

    @Test
    fun `deviceStream when List Is Empty shows Empty Device Snackbar Once`() = runTest {
        // ARRANGE
        every { deviceRepository.getDeviceStream() } returns flowOf(Result.Success(emptyList()))

        // ACT
        val viewModel = createViewModel()

        // ASSERT Event Channel
        viewModel.event.test {
            val event = awaitItem()
            assertTrue(event is DashboardScreenEvent.ShowEmptyDevicesSnackbar)

            val uiText = (event as DashboardScreenEvent.ShowEmptyDevicesSnackbar).message as UiText.StringResource
            assertEquals(R.string.error_no_device, uiText.resId)
        }
    }

    // LOCATION OBSERVATION TESTS
    @Test
    fun `locationStream when Success updates Location State`() = runTest {
        // ARRANGE
        val macAddress = "MAC_LOC"
        activeMacAddressFlow.value = macAddress

        val dummyLocation = Location(latitude = "-6.200000", longitude = "106.816666")
        every { locationRepository.getLocationStream(macAddress) } returns flowOf(
            Result.Success(
                dummyLocation
            )
        )

        // ACT
        val viewModel = createViewModel()

        // ASSERT
        viewModel.state.test {
            val state = awaitItem()

            val locationResult = state.locationState as Result.Success
            assertNotNull(locationResult.data)
            assertEquals("-6.200000", locationResult.data?.latitude)
        }
    }

    @Test
    fun `locationStream when Error shows Error Snackbar`() = runTest {
        // ARRANGE
        val macAddress = "MAC_LOC"
        activeMacAddressFlow.value = macAddress
        val errorMessage = "GPS signal lost"

        every { locationRepository.getLocationStream(macAddress) } returns flowOf(
            Result.Error(
                Exception(),
                errorMessage
            )
        )

        // ACT
        val viewModel = createViewModel()

        // ASSERT
        viewModel.event.test {
            val event = awaitItem()
            assertTrue(event is DashboardScreenEvent.ShowEmptyDevicesSnackbar)

            val uiText = (event as DashboardScreenEvent.ShowEmptyDevicesSnackbar).message as UiText.StringResource
            assertEquals(R.string.error_location, uiText.resId)
        }
    }

    // HEALTH STATUS OBSERVATION TESTS
    @Test
    fun `healthStatusStream when Success updates Health Status State`() = runTest {
        // ARRANGE
        val macAddress = "MAC_HEALTH"
        activeMacAddressFlow.value = macAddress
        val dummyHealth = HealthStatus(status = "Normal")

        every { healthStatusRepository.getHealthStatusesStream(macAddress) } returns flowOf(
            Result.Success(
                dummyHealth
            )
        )

        // ACT
        val viewModel = createViewModel()

        // ASSERT
        viewModel.state.test {
            val state = awaitItem()

            val healthResult = state.healthStatusState as Result.Success
            assertNotNull(healthResult.data)
            assertEquals("Normal", healthResult.data?.status)
        }
    }

    @Test
    fun `healthStatusStream when Error shows Error Snackbar`() = runTest {
        // ARRANGE
        val macAddress = "MAC_HEALTH"
        activeMacAddressFlow.value = macAddress
        val errorMessage = "Failed to connect to health sensor"

        every { healthStatusRepository.getHealthStatusesStream(macAddress) } returns flowOf(
            Result.Error(
                Exception(),
                errorMessage
            )
        )

        // ACT
        val viewModel = createViewModel()

        // ASSERT
        viewModel.event.test {
            val event = awaitItem()
            assertTrue(event is DashboardScreenEvent.ShowEmptyDevicesSnackbar)
            val uiText = (event as DashboardScreenEvent.ShowEmptyDevicesSnackbar).message as UiText.StringResource
            assertEquals(R.string.error_health_status, uiText.resId)
        }
    }

    // VITALS OBSERVATION TESTS
    @Test
    fun `vitalsStream when Success filters Chart Data For Last Three Minutes And Sets Latest Vital`() =
        runTest {
            // ARRANGE
            val macAddress = "MAC_VITALS"
            activeMacAddressFlow.value = macAddress

            val now = Instant.now()
            val twoMinsAgo = now.minus(2, ChronoUnit.MINUTES).toString()
            val fiveMinsAgo = now.minus(5, ChronoUnit.MINUTES).toString()

            val vitalLatest = Vitals(36.5, 80, 98, twoMinsAgo)
            val vitalOld = Vitals(37.0, 85, 95, fiveMinsAgo)

            every { vitalRepository.getVitalStream(macAddress) } returns flowOf(
                Result.Success(
                    listOf(vitalLatest, vitalOld)
                )
            )

            // ACT
            val viewModel = createViewModel()

            // ASSERT
            viewModel.state.test {
                val state = awaitItem()

                val latestState = state.vitalsState as Result.Success
                assertEquals(80, latestState.data?.heartrate)

                val chartState = state.vitalsChartState as Result.Success
                assertEquals(1, chartState.data.size)
                assertEquals(80, chartState.data[0].heartrate)
            }
        }

    @Test
    fun `vitalsStream when Active Mac Is Null emits Empty List Success`() = runTest {
        // ARRANGE
        activeMacAddressFlow.value = null

        // ACT
        val viewModel = createViewModel()

        // ASSERT
        viewModel.state.test {
            val state = awaitItem()

            val latestState = state.vitalsState as Result.Success
            assertNull(latestState.data, "Latest vital must be null when list is empty")

            val chartState = state.vitalsChartState as Result.Success
            assertTrue(chartState.data.isEmpty(), "Chart vitals must be an empty list")
        }
    }

    // SEVERE MONITOR TESTS
    @Test
    fun `severeMonitorStream when Status Transitions To Severe emits Evacuation Alert`() = runTest {
        // ARRANGE
        val macAddress = "MAC_SEVERE"
        activeMacAddressFlow.value = macAddress

        val severeData = SevereMonitor(macAddress, isSevere = true, severeStartTime = "Now")
        every { severeMonitorRepository.getSevereMonitorStream(macAddress) } returns flowOf(
            Result.Success(
                severeData
            )
        )

        // ACT
        val viewModel = createViewModel()

        // ASSERT Event
        viewModel.event.test {
            val event = awaitItem()
            assertTrue(event is DashboardScreenEvent.ShowEvacuationAlert)
            assertEquals(macAddress, (event as DashboardScreenEvent.ShowEvacuationAlert).macAddress)
        }
    }

    @Test
    fun `severeMonitorStream when Status Remains Severe does Not Emit Multiple Alerts`() = runTest {
        // ARRANGE
        val macAddress = "MAC_SEVERE"
        activeMacAddressFlow.value = macAddress

        val severeFlow = MutableStateFlow<Result<SevereMonitor?>>(Result.Loading)
        every { severeMonitorRepository.getSevereMonitorStream(macAddress) } returns severeFlow

        val viewModel = createViewModel()

        viewModel.event.test {
            severeFlow.value = Result.Success(SevereMonitor(macAddress, true, "Time1"))
            assertTrue(awaitItem() is DashboardScreenEvent.ShowEvacuationAlert)

            severeFlow.value = Result.Success(SevereMonitor(macAddress, true, "Time2"))
            expectNoEvents()
        }
    }
}