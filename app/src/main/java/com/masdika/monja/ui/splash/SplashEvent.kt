package com.masdika.monja.ui.splash

sealed interface SplashEvent {
    data object RetryInitialization : SplashEvent
    data object NavigateToDashboard : SplashEvent
}