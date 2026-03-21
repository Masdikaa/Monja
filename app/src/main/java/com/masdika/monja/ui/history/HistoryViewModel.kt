package com.masdika.monja.ui.history

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masdika.monja.data.repository.interfaces.MedicalAlertsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val medicalAlertsRepository: MedicalAlertsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = MutableStateFlow(HistoryScreenState())
    val state = _state.asStateFlow()

    init {
        val passedMacAddress = savedStateHandle.get<String>("macAddress") ?: ""
        Log.e("CCTV_HISTORY", "2. HistoryViewModel receive MAC: '$passedMacAddress'")
        _state.update { it.copy(macAddress = passedMacAddress) }

        if (passedMacAddress.isNotEmpty()) {
            startObservingHistory(passedMacAddress)
        } else {
            _state.update { it.copy(historyLoading = false) }
        }

    }

    private fun startObservingHistory(macAddress: String) {
        viewModelScope.launch {
            _state.update { it.copy(historyLoading = true) }

            medicalAlertsRepository.getMedicalAlertsStream(macAddress)
                .catch { e ->
                    e.printStackTrace()
                    _state.update { it.copy(historyLoading = false) }
                }
                .collect { alertList ->
                    _state.update {
                        it.copy(
                            alerts = alertList,
                            historyLoading = false
                        )
                    }
                }
        }
    }
}