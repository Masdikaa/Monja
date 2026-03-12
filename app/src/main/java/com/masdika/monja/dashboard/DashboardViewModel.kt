package com.masdika.monja.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masdika.monja.data.model.Device
import com.masdika.monja.data.repository.DeviceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.format.DateTimeParseException
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: DeviceRepository
) : ViewModel() {
    private val _state = MutableStateFlow(DashboardScreenState())
    val state = _state.asStateFlow()

    init {
        startObservingDevices()
    }

    private fun startObservingDevices() {
        _state.update { it.copy(dataLoading = true) }

        viewModelScope.launch {
            repository.getDeviceStream()
                .catch { e ->
                    e.printStackTrace()
                    _state.update { it.copy(dataLoading = false) }
                }
                .collect { devices ->
                    val sortedDevice = devices.sortedWith(compareBy { device ->
                        try {
                            Instant.parse(device.lastSeen)
                        } catch (e: DateTimeParseException) {
                            e.printStackTrace()
                            Instant.MAX
                        }
                    })

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

    fun selectDevice(device: Device) {
        _state.update { it.copy(selectedDevice = device) }
    }
}