package com.masdika.monja.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masdika.monja.R
import com.masdika.monja.data.repository.interfaces.ActiveDeviceRepository
import com.masdika.monja.data.repository.interfaces.MedicalAlertsRepository
import com.masdika.monja.data.Result
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
import timber.log.Timber
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
        Timber.d("Initializing HistoryViewModel...")
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
                    Timber.d("Active MAC is null. Emitting empty history.")
                    _state.update { it.copy(macAddress = "") }
                    flowOf(Result.Success(emptyList()))
                } else {
                    Timber.d("Observing history for MAC: $macAddress")
                    _state.update { it.copy(macAddress = macAddress) }
                    medicalAlertsRepository.getMedicalAlertsStream(macAddress)
                }
            }.collect { result ->
                if (result is Result.Error) {
                    Timber.e(result.exception, "History stream error: ${result.message}")
                }
                _state.update { it.copy(statusState = result) }
            }
        }
    }

    fun deleteAllMedicalAlerts() {
        viewModelScope.launch {
            Timber.d("Attempting to delete all medical alerts...")
            try {
                val macAddress = _state.value.macAddress
                if (macAddress.isNotEmpty()) {
                    medicalAlertsRepository.deleteMedicalAlerts(macAddress)
                    Timber.d("Successfully deleted medical alerts for $macAddress.")

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
                } else {
                    Timber.w("Can't delete alerts: MAC Address is empty.")
                }
            } catch (e: Exception) {
                Timber.e(e, "Failed to delete medical alerts.")
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
        Timber.d("Showing delete confirmation dialog.")
        _state.update { it.copy(showDeleteDialog = true) }
    }

    fun dismissDeleteConfirmation() {
        Timber.d("Dismissing delete confirmation dialog.")
        _state.update { it.copy(showDeleteDialog = false) }
    }
}