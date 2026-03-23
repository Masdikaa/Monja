package com.masdika.monja.ui.history

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object HistoryRoute

fun NavController.navigateToHistory() {
    navigate(route = HistoryRoute) {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.historyScreenRoute(
    // TODO() Navigate screen function
) {
    composable<HistoryRoute> {
        val viewModel = hiltViewModel<HistoryViewModel>()
        HistoryScreen(
            viewModel = viewModel
        )
    }
}