package com.masdika.monja.ui.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.bottombar.AnimatedBottomBar
import com.example.bottombar.components.BottomBarItem
import com.example.bottombar.model.IndicatorDirection
import com.example.bottombar.model.IndicatorStyle
import com.masdika.monja.ui.dashboard.DashboardRoute
import com.masdika.monja.ui.history.HistoryRoute

@Composable
fun MainBottomBar(
    navController: NavController,
) {
    val navigationItems = listOf(
        NavigationItem.Dashboard,
        NavigationItem.History
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val selectedIndex = navigationItems.indexOfFirst { item ->
        currentDestination?.hierarchy?.any {
            it.hasRoute(item.route::class)
        } == true
    }.takeIf { it >= 0 } ?: 0

    AnimatedBottomBar(
        selectedItem = selectedIndex,
        itemSize = navigationItems.size,
        containerColor = MaterialTheme.colorScheme.background,
        indicatorStyle = IndicatorStyle.WORM,
        indicatorDirection = IndicatorDirection.TOP,
        bottomBarHeight = 56.dp
    ) {
        navigationItems.forEachIndexed { index, item ->
            BottomBarItem(
                selected = selectedIndex == index,
                onClick = {
                    if (selectedIndex != index) {
                        val targetRoute: Any = when (item) {
                            NavigationItem.History -> HistoryRoute
                            NavigationItem.Dashboard -> DashboardRoute
                        }

                        navController.navigate(targetRoute) {
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                imageVector = item.icon,
                label = item.title,
                containerColor = Color.Transparent
            )
        }
    }
}