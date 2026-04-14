package com.masdika.monja.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masdika.monja.R
import com.masdika.monja.data.repository.interfaces.DeviceRepository
import com.masdika.monja.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val deviceRepository: DeviceRepository
) : ViewModel() {
    private val _state = MutableStateFlow(SplashState())
    val state = _state.asStateFlow()

    private val _event = Channel<SplashEvent>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    companion object {
        private const val MAX_RETRIES = 3
        private const val INITIAL_DELAY_MS = 1000L
    }

    init {
        initializeApp()
    }

    private fun initializeApp() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    initializationState = InitializationState.Loading,
                    statusMessage = UiText.StringResource(R.string.splash_initializing)
                )
            }

            var delay = INITIAL_DELAY_MS
            repeat(MAX_RETRIES) { attempt ->
                try {
                    deviceRepository.getAvailableDevices()

                    _state.update { it.copy(initializationState = InitializationState.Success) }
                    _event.send(SplashEvent.NavigateToDashboard)
                    return@launch
                } catch (e: Exception) {
                    e.printStackTrace()
                    _state.update {
                        it.copy(
                            statusMessage = UiText.StringResource(
                                R.string.splash_retrying,
                                attempt + 1, MAX_RETRIES
                            ),
                            retryCount = attempt + 1
                        )
                    }
                    delay(delay)
                    delay *= 2 // Exponential Backoff
                }
            }
            _state.update {
                it.copy(
                    initializationState = InitializationState.Error(UiText.StringResource(R.string.splash_error_connection)),
                    statusMessage = UiText.DynamicString("")
                )
            }
        }
    }

    fun retry() {
        initializeApp()
    }
}