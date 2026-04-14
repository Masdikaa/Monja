package com.masdika.monja.ui.splash

import com.masdika.monja.util.UiText

data class SplashState(
    val initializationState: InitializationState = InitializationState.Loading,
    val statusMessage: UiText = UiText.DynamicString(""),
    val retryCount: Int = 0
)

sealed interface InitializationState {
    data object Loading : InitializationState
    data object Success : InitializationState
    data class Error(val message: UiText) : InitializationState
}