package com.masdika.monja.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.History
import androidx.compose.ui.graphics.vector.ImageVector
import com.masdika.monja.R
import com.masdika.monja.ui.dashboard.DashboardRoute
import com.masdika.monja.ui.history.HistoryRoute
import kotlinx.serialization.Serializable

sealed class NavigationItem(
    val titleResId: Int,
    val icon: ImageVector,
    val route: @Serializable Any
) {
    data object Dashboard : NavigationItem(
        titleResId = R.string.label_nav_dashboard,
        icon = Icons.Default.Dashboard,
        route = DashboardRoute
    )

    data object History : NavigationItem(
        titleResId = R.string.label_nav_history,
        icon = Icons.Default.History,
        route = HistoryRoute
    )
}