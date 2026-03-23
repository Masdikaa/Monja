package com.masdika.monja.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masdika.monja.data.repository.interfaces.ActiveDeviceRepository
import com.masdika.monja.data.repository.interfaces.MedicalAlertsRepository
import com.masdika.monja.data.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val medicalAlertsRepository: MedicalAlertsRepository,
    private val activeDeviceRepository: ActiveDeviceRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(HistoryScreenState())
    val state = _state.asStateFlow()

    init {
        startObservingHistory()
    }

    private fun startObservingHistory() {
        viewModelScope.launch {
            activeDeviceRepository.activeMacAddress
                .flatMapLatest { macAddress ->
                    if (macAddress == null) {
                        _state.update { it.copy(macAddress = "") }
                        flowOf(Result.Success(emptyList()))
                    } else {
                        _state.update { it.copy(macAddress = macAddress) }
                        medicalAlertsRepository.getMedicalAlertsStream(macAddress)
                    }
                }
                .collect { result ->
                    _state.update { it.copy(statusState = result) }
                }
        }
    }
}