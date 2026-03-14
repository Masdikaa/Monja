package com.masdika.monja.ui.dashboard

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object DashboardRoute

fun NavController.navigateToDashboard() {
    navigate(route = DashboardRoute)
}

fun NavGraphBuilder.dashboardScreenRoute(
    // TODO() Navigate screen function
) {
    composable<DashboardRoute> {
        val viewModel = hiltViewModel<DashboardViewModel>()
        DashboardScreen(
            viewModel = viewModel
        )
    }
}