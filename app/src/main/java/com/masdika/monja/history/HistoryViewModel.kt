package com.masdika.monja.history

import androidx.lifecycle.ViewModel
import com.masdika.monja.data.repository.DeviceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: DeviceRepository
) : ViewModel() {
    private val _state = MutableStateFlow(HistoryScreenState())
    val state = _state.asStateFlow()

    init {
        _state.update { it.copy(test = "History Screen Test") }
    }
}