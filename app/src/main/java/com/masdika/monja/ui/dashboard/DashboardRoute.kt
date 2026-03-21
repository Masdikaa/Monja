package com.masdika.monja.ui.dashboard

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
    viewModel: DashboardViewModel
) {
    composable<DashboardRoute> {
        DashboardScreen(
            viewModel = viewModel
        )
    }
}