package com.masdika.monja.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masdika.monja.data.model.Device
import com.masdika.monja.data.repository.interfaces.DeviceRepository
import com.masdika.monja.data.repository.interfaces.HealthStatusRepository
import com.masdika.monja.data.repository.interfaces.LocationRepository
import com.masdika.monja.data.repository.interfaces.VitalsRepository
import com.masdika.monja.data.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val deviceRepository: DeviceRepository,
    private val vitalRepository: VitalsRepository,
    private val locationRepository: LocationRepository,
    private val healthStatusRepository: HealthStatusRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(DashboardScreenState())
    val state = _state.asStateFlow()

    init {
        startObservingDevices()
        startObservingVitals()
        startObservingLocation()
        startObservingHealthStatus()
    }

    private fun startObservingDevices() {
        viewModelScope.launch {
            deviceRepository.getDeviceStream()
                .collect { result ->
                    _state.update { currentState ->
                        when (result) {
                            is Result.Success -> {
                                val devices = result.data
                                val sortedDevice = devices.sortedWith(
                                    compareByDescending<Device> { it.isOnline }
                                        .thenByDescending { it.createdAt }
                                )
                                val selectedDevice = currentState.selectedDevice?.let { current ->
                                    sortedDevice.find { it.macAddress == current.macAddress }
                                } ?: sortedDevice.firstOrNull()

                                currentState.copy(
                                    deviceState = Result.Success(sortedDevice),
                                    selectedDevice = selectedDevice
                                )
                            }

                            is Result.Loading -> {
                                currentState.copy(deviceState = Result.Loading)
                            }

                            is Result.Error -> {
                                currentState.copy(
                                    deviceState = Result.Error(result.exception, result.message)
                                )
                            }
                        }
                    }
                }
        }
    }

    private fun startObservingVitals() {
        viewModelScope.launch {
            _state
                .map { it.selectedDevice?.macAddress }
                .distinctUntilChanged()
                .flatMapLatest { macAddress ->
                    if (macAddress == null) {
                        flowOf(Result.Success(null))
                    } else {
                        // ========== Network Loading Simulation ==========
                        _state.update { it.copy(vitalsState = Result.Loading) }
                        delay(7000)
                        // ========== Network Loading Simulation ==========
                        vitalRepository.getVitalStream(macAddress)
                    }
                }
                .collect { vitalData ->
                    _state.update { it.copy(vitalsState = vitalData) }
                }
        }
    }

    private fun startObservingLocation() {
        viewModelScope.launch {
            _state
                .map { it.selectedDevice?.macAddress }
                .distinctUntilChanged()
                .flatMapLatest { macAddress ->
                    if (macAddress == null) {
                        flowOf(Result.Success(null))
                    } else {
                        // ========== Network Loading Simulation ==========
                        _state.update { it.copy(locationState = Result.Loading) }
                        delay(3000)
                        // ========== Network Loading Simulation ==========
                        locationRepository.getLocationStream(macAddress)
                    }
                }
                .collect { locationData ->
                    _state.update { it.copy(locationState = locationData) }
                }
        }
    }

    private fun startObservingHealthStatus() {
        viewModelScope.launch {
            _state
                .map { it.selectedDevice?.macAddress }
                .distinctUntilChanged()
                .flatMapLatest { macAddress ->
                    if (macAddress == null) {
                        flowOf(Result.Success(null))
                    } else {
                        // ========== Network Loading Simulation ==========
                        _state.update { it.copy(healthStatusState = Result.Loading) }
                        delay(7000)
                        // ========== Network Loading Simulation ==========
                        healthStatusRepository.getHealthStatusesStream(macAddress)
                    }
                }
                .collect { healthStatusData ->
                    _state.update { it.copy(healthStatusState = healthStatusData) }
                }
        }
    }

    fun selectDevice(device: Device) {
        _state.update { it.copy(selectedDevice = device) }
    }
}