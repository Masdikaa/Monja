package com.masdika.monja.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masdika.monja.data.repository.DeviceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: DeviceRepository
) : ViewModel() {
    private val _state = MutableStateFlow(DashboardScreenState())
    val state = _state.asStateFlow()

    init {
        fetchDevices()
    }

    fun fetchDevices() {
        _state.update { it.copy(dataLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            val devices = async { repository.getAvailableDevices() }
            withContext(Dispatchers.Main) {
                _state.update {
                    it.copy(
                        devices = devices.await(),
                        dataLoading = false
                    )
                }
            }
        }
    }
}