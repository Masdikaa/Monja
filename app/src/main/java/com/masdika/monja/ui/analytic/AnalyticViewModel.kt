package com.masdika.monja.ui.analytic

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.masdika.monja.data.repository.interfaces.VitalsRepository
import com.masdika.monja.data.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
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
        Timber.d("Init AnalyticViewModel. MAC: $macAddress, Type: $vitalType")
        startObservingVitals()
    }

    private fun startObservingVitals() {
        viewModelScope.launch {
            vitalRepository.getVitalStream(macAddress).collect { vitalData ->
                when (vitalData) {
                    is Result.Loading -> Timber.v("Analytic vital stream loading")
                    is Result.Success -> Timber.d("Analytic vital stream success: ${vitalData.data.size} items")
                    is Result.Error -> Timber.e(vitalData.exception, "Analytic vital stream error")
                }
                _state.update { it.copy(vitalState = vitalData) }
            }
        }
    }
}