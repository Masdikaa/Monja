package com.masdika.monja.ui.analytic

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.masdika.monja.data.repository.interfaces.VitalsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnalyticViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val vitalRepository: VitalsRepository
) : ViewModel() {
    private val args = savedStateHandle.toRoute<AnalyticRoute>()
    val macAddress = args.macAddress
    val vitalType = args.vitalType

    private val _state = MutableStateFlow(AnalyticScreenState())
    val state = _state.asStateFlow()

    init {
        startObservingVitals()
    }

    private fun startObservingVitals() {
        viewModelScope.launch {
            vitalRepository.getVitalStream(macAddress).collect { vitalData ->
                _state.update { it.copy(vitalState = vitalData) }
            }
        }
    }
}