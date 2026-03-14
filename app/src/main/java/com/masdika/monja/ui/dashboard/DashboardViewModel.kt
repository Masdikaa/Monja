package com.masdika.monja.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masdika.monja.data.model.Device
import com.masdika.monja.data.repository.interfaces.DeviceRepository
import com.masdika.monja.data.repository.interfaces.LocationRepository
import com.masdika.monja.data.repository.interfaces.VitalsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
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
) : ViewModel() {
    private val _state = MutableStateFlow(DashboardScreenState())
    val state = _state.asStateFlow()

    init {
        startObservingDevices()
        startObservingVitals()
        startObservingLocation()
    }

    private fun startObservingDevices() {
        _state.update { it.copy(dataLoading = true) }

        viewModelScope.launch {
            deviceRepository.getDeviceStream()
                .catch { e ->
                    e.printStackTrace()
                    _state.update { it.copy(dataLoading = false) }
                }
                .collect { devices ->
                    val sortedDevice = devices.sortedWith(
                        compareByDescending<Device> { it.isOnline }
                            .thenByDescending { it.createdAt }
                    )

                    _state.update { currentState ->
                        val selectedDevice = currentState.selectedDevice?.let { current ->
                            sortedDevice.find { it.macAddress == current.macAddress }
                        } ?: sortedDevice.firstOrNull()
                        currentState.copy(
                            devices = sortedDevice,
                            selectedDevice = selectedDevice,
                            dataLoading = false
                        )
                    }
                }
        }
    }

    private fun startObservingVitals() {
        _state.update { it.copy(dataLoading = true) }

        viewModelScope.launch {
            _state
                .map { it.selectedDevice?.macAddress }
                .distinctUntilChanged()
                .flatMapLatest { macAddress ->
                    if (macAddress == null) {
                        flowOf(null)
                    } else {
                        vitalRepository.getVitalStream(macAddress)
                    }
                }
                .catch { e ->
                    e.printStackTrace()
                }
                .collect { vitalData ->
                    _state.update {
                        it.copy(vitals = vitalData)
                    }
                }
        }
    }

    private fun startObservingLocation() {
        _state.update { it.copy(dataLoading = true) }

        viewModelScope.launch {
            _state
                .map { it.selectedDevice?.macAddress }
                .distinctUntilChanged()
                .flatMapLatest { macAddress ->
                    if (macAddress == null) {
                        flowOf(null)
                    } else {
                        locationRepository.getLocationStream(macAddress)
                    }
                }
                .catch { e ->
                    e.printStackTrace()
                }
                .collect { locationData ->
                    _state.update {
                        it.copy(location = locationData)
                    }
                }

        }
    }

    fun selectDevice(device: Device) {
        _state.update { it.copy(selectedDevice = device) }
    }
}