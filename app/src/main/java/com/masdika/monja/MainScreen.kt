package com.masdika.monja

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.masdika.monja.ui.analytic.AnalyticRoute
import com.masdika.monja.ui.analytic.analyticScreenRoute
import com.masdika.monja.ui.component.MainBottomBar
import com.masdika.monja.ui.dashboard.DashboardRoute
import com.masdika.monja.ui.dashboard.dashboardScreenRoute
import com.masdika.monja.ui.history.historyScreenRoute

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val isAnalyticScreen = currentDestination?.hasRoute<AnalyticRoute>() == true

    Scaffold(
        bottomBar = {
            if (!isAnalyticScreen) {
                MainBottomBar(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = DashboardRoute,
            modifier = Modifier.padding(bottom = if (!isAnalyticScreen) innerPadding.calculateBottomPadding() else 0.dp)
        ) {
            dashboardScreenRoute(
                onNavigateToAnalytic = { macAddress, vitalType ->
                    navController.navigate(AnalyticRoute(macAddress, vitalType))
                }
            )
            historyScreenRoute()
            analyticScreenRoute(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}