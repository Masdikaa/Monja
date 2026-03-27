package com.masdika.monja.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masdika.monja.data.model.Device
import com.masdika.monja.data.model.Vitals
import com.masdika.monja.data.repository.interfaces.ActiveDeviceRepository
import com.masdika.monja.data.repository.interfaces.DeviceRepository
import com.masdika.monja.data.repository.interfaces.HealthStatusRepository
import com.masdika.monja.data.repository.interfaces.LocationRepository
import com.masdika.monja.data.repository.interfaces.VitalsRepository
import com.masdika.monja.data.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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
    private val activeDeviceRepository: ActiveDeviceRepository
) : ViewModel() {
    private val _state = MutableStateFlow(DashboardScreenState())
    val state = _state.asStateFlow()
    private val _event = Channel<DashboardScreenEvent>()
    val event = _event.receiveAsFlow()

    init {
        startObservingDevices()
        startObservingVitals()
        startObservingLocation()
        startObservingHealthStatus()
    }

    private fun startObservingDevices() {
        viewModelScope.launch {
            // ========== Network Loading Simulation ==========
            _state.update { it.copy(deviceState = Result.Loading) }
            delay(3000)
            // ========== Network Loading Simulation ==========
            deviceRepository.getDeviceStream()
                .collect { result ->
                    _state.update { currentState ->
                        when (result) {
                            is Result.Success -> {
                                val devices = result.data

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
                                    _event.trySend(
                                        DashboardScreenEvent.ShowEmptyDevicesSnackbar("No available devices found!")
                                    )
                                }

                                if (shouldShowDeviceConnectionSnackbar) {
                                    _event.trySend(
                                        DashboardScreenEvent.ShowDeviceConnectionSnackbar(
                                            macAddress = selectedDevice.macAddress,
                                            isOnline = selectedDevice.isOnline
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
                                currentState.copy(
                                    deviceState = Result.Loading,
                                    previousDeviceOnlineState = currentState.previousDeviceOnlineState,
                                    hasShownEmptyDeviceSnackbar = currentState.hasShownEmptyDeviceSnackbar
                                )
                            }

                            is Result.Error -> {
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
                                message = result.message ?: "Failed to load devices"
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
                        flowOf(Result.Success(emptyList<Vitals>()))
                    } else {
                        // ========== Network Loading Simulation ==========
                        _state.update {
                            it.copy(
                                vitalsState = Result.Loading,
                                vitalsChartState = Result.Loading
                            )
                        }
                        delay(3000)
                        // ========== Network Loading Simulation ==========
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
                            val latestVital = allVitals.firstOrNull()

                            val threeMinuteAgo = Instant.now().minus(3, ChronoUnit.MINUTES)
                            val chartVitals = allVitals.filter {
                                try {
                                    Instant.parse(it.createdAt).isAfter(threeMinuteAgo)
                                } catch (e: Exception) {
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
                                    message = vitalData.message ?: "Failed to load vitals data"
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
                        // ========== Network Loading Simulation ==========
                        _state.update { it.copy(locationState = Result.Loading) }
                        delay(4000)
                        // ========== Network Loading Simulation ==========
                        locationRepository.getLocationStream(macAddress)
                    }
                }
                .collect { locationData ->
                    _state.update { it.copy(locationState = locationData) }
                    if (locationData is Result.Error) {
                        _event.trySend(
                            DashboardScreenEvent.ShowEmptyDevicesSnackbar(
                                message = locationData.message ?: "Failed to load location"
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
                        // ========== Network Loading Simulation ==========
                        _state.update { it.copy(healthStatusState = Result.Loading) }
                        delay(5000)
                        // ========== Network Loading Simulation ==========
                        healthStatusRepository.getHealthStatusesStream(macAddress)
                    }
                }
                .collect { healthStatusData ->
                    _state.update { it.copy(healthStatusState = healthStatusData) }
                    if (healthStatusData is Result.Error) {
                        _event.trySend(
                            DashboardScreenEvent.ShowEmptyDevicesSnackbar(
                                message = healthStatusData.message ?: "Failed to load status"
                            )
                        )
                    }
                }
        }
    }

    fun selectDevice(device: Device) {
        activeDeviceRepository.setActiveDevice(device.macAddress)
        _state.update { it.copy(selectedDevice = device) }
    }
}