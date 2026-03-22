package com.masdika.monja.ui.history

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masdika.monja.data.repository.interfaces.MedicalAlertsRepository
import com.masdika.monja.data.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

        if (passedMacAddress.isNotEmpty()) {
            _state.update { it.copy(macAddress = passedMacAddress) }
            startObservingHistory(passedMacAddress)
        } else {
            _state.update { it.copy(macAddress = "", statusState = Result.Loading) }
        }
    }

    private fun startObservingHistory(macAddress: String) {
        viewModelScope.launch {
            medicalAlertsRepository.getMedicalAlertsStream(macAddress)
                .collect { result ->
                    _state.update {
                        it.copy(statusState = result)
                    }
                }
        }
    }
}