package com.masdika.monja.ui.analytic

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AnalyticViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val args = savedStateHandle.toRoute<AnalyticRoute>()
    val macAddress = args.macAddress
    val vitalType = args.vitalType

    init {
        println("Analytic Screen opened for device: $macAddress, vital: $vitalType")
    }
}