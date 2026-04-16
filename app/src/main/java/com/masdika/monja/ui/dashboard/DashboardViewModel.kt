package com.masdika.monja.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masdika.monja.R
import com.masdika.monja.data.model.Device
import com.masdika.monja.data.model.Vitals
import com.masdika.monja.data.repository.interfaces.ActiveDeviceRepository
import com.masdika.monja.data.repository.interfaces.DeviceRepository
import com.masdika.monja.data.repository.interfaces.HealthStatusRepository
import com.masdika.monja.data.repository.interfaces.LocationRepository
import com.masdika.monja.data.repository.interfaces.SevereMonitorRepository
import com.masdika.monja.data.repository.interfaces.VitalsRepository
import com.masdika.monja.data.Result
import com.masdika.monja.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val deviceRepository: DeviceRepository,
    private val vitalRepository: VitalsRepository,
    private val locationRepository: LocationRepository,
    private val healthStatusRepository: HealthStatusRepository,
    private val activeDeviceRepository: ActiveDeviceRepository,
    private val severeMonitorRepository: SevereMonitorRepository
) : ViewModel() {
    private val _state = MutableStateFlow(DashboardScreenState())
    val state = _state.asStateFlow()
    private val _event = Channel<DashboardScreenEvent>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()
    private var lastSevereStatus = false

    init {
        Timber.d("Initializing DashboardViewModel and starting observers...")
        startObservingDevices()
        startObservingVitals()
        startObservingLocation()
        startObservingHealthStatus()
        startObservingSevereMonitor()
    }

    private fun startObservingDevices() {
        viewModelScope.launch {
            Timber.d("Starting to observe devices...")
            deviceRepository.getDeviceStream()
                .collect { result ->
                    _state.update { currentState ->
                        when (result) {
                            is Result.Success -> {
                                val devices = result.data
                                Timber.d("Device stream success. Fetched ${devices.size} devices.")

                                val shouldShowEmptyDeviceSnackbar =
                                    devices.isEmpty() && !currentState.hasShownEmptyDeviceSnackbar

                                val sortedDevice = devices.sortedWith(
                                    compareByDescending<Device> { it.isOnline }
                                        .thenByDescending { it.createdAt }
                                )
                                val currentActiveMac = activeDeviceRepository.activeMacAddress.value

                                val selectedDevice = if (currentActiveMac != null) {
                                    sortedDevice.find { it.macAddress == currentActiveMac }
                                } else {
                                    sortedDevice.firstOrNull()
                                }

                                val previousOnlineState = currentState.previousDeviceOnlineState
                                val shouldShowDeviceConnectionSnackbar =
                                    selectedDevice != null && previousOnlineState != null && selectedDevice.isOnline != previousOnlineState

                                selectedDevice?.let {
                                    if (currentActiveMac != it.macAddress) {
                                        activeDeviceRepository.setActiveDevice(it.macAddress)
                                    }
                                }

                                if (shouldShowEmptyDeviceSnackbar) {
                                    Timber.d("Triggering ShowEmptyDevicesSnackbar event.")
                                    _event.trySend(
                                        DashboardScreenEvent.ShowEmptyDevicesSnackbar(
                                            message = UiText.StringResource(R.string.error_no_device)
                                        )
                                    )
                                }

                                if (shouldShowDeviceConnectionSnackbar) {
                                    Timber.d("Device ${selectedDevice.macAddress} connection state changed to: ${selectedDevice.isOnline}")
                                    val stringResId = if (selectedDevice.isOnline) {
                                        R.string.format_device_online
                                    } else {
                                        R.string.format_device_offline
                                    }
                                    _event.trySend(
                                        DashboardScreenEvent.ShowDeviceConnectionSnackbar(
                                            message = UiText.StringResource(
                                                stringResId,
                                                selectedDevice.macAddress
                                            )
                                        )
                                    )
                                }

                                currentState.copy(
                                    deviceState = Result.Success(sortedDevice),
                                    selectedDevice = selectedDevice,
                                    hasShownEmptyDeviceSnackbar = devices.isEmpty(),
                                    previousDeviceOnlineState = selectedDevice?.isOnline
                                )
                            }

                            is Result.Loading -> {
                                Timber.v("Device stream is loading...")
                                currentState.copy(
                                    deviceState = Result.Loading,
                                    previousDeviceOnlineState = currentState.previousDeviceOnlineState,
                                    hasShownEmptyDeviceSnackbar = currentState.hasShownEmptyDeviceSnackbar
                                )
                            }

                            is Result.Error -> {
                                Timber.e(result.exception, "Device stream error: ${result.message}")
                                currentState.copy(
                                    deviceState = Result.Error(result.exception, result.message),
                                    previousDeviceOnlineState = currentState.previousDeviceOnlineState,
                                    hasShownEmptyDeviceSnackbar = currentState.hasShownEmptyDeviceSnackbar
                                )
                            }
                        }
                    }
                    if (result is Result.Error) {
                        _event.trySend(
                            DashboardScreenEvent.ShowEmptyDevicesSnackbar(
                                message = UiText.StringResource(R.string.error_device)
                            )
                        )
                    }
                }
        }
    }

    private fun startObservingVitals() {
        viewModelScope.launch {
            activeDeviceRepository.activeMacAddress
                .flatMapLatest { macAddress ->
                    if (macAddress == null) {
                        Timber.d("Active MAC is null. Emitting empty vitals list.")
                        flowOf(Result.Success(emptyList<Vitals>()))
                    } else {
                        Timber.d("Active MAC changed to $macAddress. Observing vitals...")
                        vitalRepository.getVitalStream(macAddress)
                    }
                }
                .collect { vitalData ->
                    when (vitalData) {
                        is Result.Loading -> {
                            _state.update {
                                it.copy(
                                    vitalsState = Result.Loading,
                                    vitalsChartState = Result.Loading
                                )
                            }
                        }

                        is Result.Success -> {
                            val allVitals = vitalData.data
                            Timber.d("Vitals updated. Count: ${allVitals.size}")
                            val latestVital = allVitals.firstOrNull()

                            val threeMinuteAgo = Instant.now().minus(3, ChronoUnit.MINUTES)
                            val chartVitals = allVitals.filter {
                                try {
                                    Instant.parse(it.createdAt).isAfter(threeMinuteAgo)
                                } catch (e: Exception) {
                                    Timber.w(e, "Failed to parse vital timestamp: ${it.createdAt}")
                                    false
                                }
                            }.reversed()

                            _state.update {
                                it.copy(
                                    vitalsState = Result.Success(latestVital),
                                    vitalsChartState = Result.Success(chartVitals)
                                )
                            }
                        }

                        is Result.Error -> {
                            Timber.e(
                                vitalData.exception,
                                "Vitals stream error: ${vitalData.message}"
                            )
                            _state.update {
                                it.copy(
                                    vitalsState = Result.Error(
                                        vitalData.exception,
                                        vitalData.message
                                    ),
                                    vitalsChartState = Result.Error(
                                        vitalData.exception,
                                        vitalData.message
                                    )
                                )
                            }
                            _event.trySend(
                                DashboardScreenEvent.ShowEmptyDevicesSnackbar(
                                    message = UiText.StringResource(R.string.error_vitals)
                                )
                            )
                        }
                    }
                }
        }
    }

    private fun startObservingLocation() {
        viewModelScope.launch {
            activeDeviceRepository.activeMacAddress
                .flatMapLatest { macAddress ->
                    if (macAddress == null) {
                        flowOf(Result.Success(null))
                    } else {
                        Timber.d("Observing location for MAC: $macAddress")
                        locationRepository.getLocationStream(macAddress)
                    }
                }
                .collect { locationData ->
                    if (locationData is Result.Success && locationData.data != null) {
                        Timber.v("Location updated for active device.")
                    }
                    _state.update { it.copy(locationState = locationData) }

                    if (locationData is Result.Error) {
                        Timber.e(locationData.exception, "Location stream error.")
                        _event.trySend(
                            DashboardScreenEvent.ShowEmptyDevicesSnackbar(
                                message = UiText.StringResource(R.string.error_location)
                            )
                        )
                    }
                }
        }
    }

    private fun startObservingHealthStatus() {
        viewModelScope.launch {
            activeDeviceRepository.activeMacAddress
                .flatMapLatest { macAddress ->
                    if (macAddress == null) {
                        flowOf(Result.Success(null))
                    } else {
                        Timber.d("Observing health status for MAC: $macAddress")
                        healthStatusRepository.getHealthStatusesStream(macAddress)
                    }
                }
                .collect { healthStatusData ->
                    if (healthStatusData is Result.Success && healthStatusData.data != null) {
                        Timber.v("Health status updated: ${healthStatusData.data!!.status}")
                    }
                    _state.update { it.copy(healthStatusState = healthStatusData) }
                    if (healthStatusData is Result.Error) {
                        Timber.e(healthStatusData.exception, "Health status stream error.")
                        _event.trySend(
                            DashboardScreenEvent.ShowEmptyDevicesSnackbar(
                                message = UiText.StringResource(R.string.error_health_status)
                            )
                        )
                    }
                }
        }
    }

    private fun startObservingSevereMonitor() {
        viewModelScope.launch {
            activeDeviceRepository.activeMacAddress
                .flatMapLatest { macAddress ->
                    if (macAddress == null) {
                        flowOf(Result.Success(null))
                    } else {
                        Timber.d("Observing severe monitor for MAC: $macAddress")
                        severeMonitorRepository.getSevereMonitorStream(macAddress)
                    }
                }
                .collect { severeMonitorData ->
                    if (severeMonitorData is Result.Success && severeMonitorData.data != null) {
                        val isCurrentlySevere = severeMonitorData.data!!.isSevere
                        Timber.v("Severe monitor updated. isSevere: $isCurrentlySevere")

                        if (isCurrentlySevere && !lastSevereStatus) {
                            Timber.w("Severe status changed to TRUE. Triggering Evacuation Alert for ${severeMonitorData.data!!.macAddress}")
                            _event.trySend(
                                DashboardScreenEvent.ShowEvacuationAlert(severeMonitorData.data!!.macAddress)
                            )
                        }

                        lastSevereStatus = isCurrentlySevere
                    } else if (severeMonitorData is Result.Error) {
                        Timber.e(severeMonitorData.exception, "Severe monitor stream error.")
                    }
                }
        }
    }

    fun selectDevice(device: Device) {
        Timber.d("User manually selected device: ${device.macAddress}")
        activeDeviceRepository.setActiveDevice(device.macAddress)
        _state.update { it.copy(selectedDevice = device) }
    }
}