package com.masdika.monja.ui.history

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object HistoryRoute

fun NavGraphBuilder.historyScreenRoute() {
    composable<HistoryRoute> {
        val viewModel = hiltViewModel<HistoryViewModel>()
        HistoryScreen(
            viewModel = viewModel
        )
    }
}