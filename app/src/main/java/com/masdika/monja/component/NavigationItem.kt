package com.masdika.monja.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.History
import androidx.compose.ui.graphics.vector.ImageVector
import com.masdika.monja.dashboard.DashboardRoute
import com.masdika.monja.history.HistoryRoute
import kotlinx.serialization.Serializable

sealed class NavigationItem(
    val title: String,
    val icon: ImageVector,
    val route: @Serializable Any
) {
    data object Dashboard : NavigationItem(
        title = "Dashboard",
        icon = Icons.Default.Dashboard,
        route = DashboardRoute
    )

    data object History : NavigationItem(
        title = "History",
        icon = Icons.Default.History,
        route = HistoryRoute
    )
}