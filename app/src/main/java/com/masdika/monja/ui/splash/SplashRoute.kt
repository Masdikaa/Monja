package com.masdika.monja.ui.splash

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object SplashRoute

fun NavGraphBuilder.splashScreenRoute(onNavigateToDashboard: () -> Unit) {
    composable<SplashRoute> {
        val viewModel = hiltViewModel<SplashViewModel>()
        SplashScreen(
            viewModel = viewModel,
            onNavigateToDashboard = onNavigateToDashboard
        )
    }
}