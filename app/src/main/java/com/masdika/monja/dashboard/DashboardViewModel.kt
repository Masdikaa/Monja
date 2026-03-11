package com.masdika.monja.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masdika.monja.data.repository.DeviceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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
                    _state.update {
                        it.copy(
                            devices = devices,
                            dataLoading = false
                        )
                    }
                }
        }
    }
}