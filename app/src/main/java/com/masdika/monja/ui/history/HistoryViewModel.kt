package com.masdika.monja.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masdika.monja.R
import com.masdika.monja.data.repository.interfaces.ActiveDeviceRepository
import com.masdika.monja.data.repository.interfaces.MedicalAlertsRepository
import com.masdika.monja.data.utils.Result
import com.masdika.monja.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.receiveAsFlow
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
    private val _event = Channel<HistoryScreenEvent>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()
    private val refreshTrigger = MutableStateFlow(0)

    init {
        startObservingHistory()
    }

    private fun startObservingHistory() {
        viewModelScope.launch {
            combine(
                activeDeviceRepository.activeMacAddress,
                refreshTrigger
            ) { macAddress, _ ->
                macAddress
            }.flatMapLatest { macAddress ->
                if (macAddress == null) {
                    _state.update { it.copy(macAddress = "") }
                    flowOf(Result.Success(emptyList()))
                } else {
                    _state.update { it.copy(macAddress = macAddress) }
                    medicalAlertsRepository.getMedicalAlertsStream(macAddress)
                }
            }.collect { result ->
                _state.update { it.copy(statusState = result) }
            }
        }
    }

    fun deleteAllMedicalAlerts() {
        viewModelScope.launch {
            try {
                val macAddress = _state.value.macAddress
                if (macAddress.isNotEmpty()) {
                    medicalAlertsRepository.deleteMedicalAlerts(macAddress)
                    refreshTrigger.update { it + 1 }

                    _state.update {
                        it.copy(showDeleteDialog = false)
                    }

                    _event.trySend(
                        HistoryScreenEvent.ShowSnackbar(
                            message = UiText.StringResource(
                                R.string.success_delete_history,
                                macAddress
                            )
                        )
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(showDeleteDialog = false) }
                _event.trySend(
                    HistoryScreenEvent.ShowSnackbar(
                        message = UiText.StringResource(R.string.error_delete_history)
                    )
                )
                e.printStackTrace()
            }
        }
    }

    fun showDeleteConfirmation() {
        _state.update { it.copy(showDeleteDialog = true) }
    }

    fun dismissDeleteConfirmation() {
        _state.update { it.copy(showDeleteDialog = false) }
    }
}