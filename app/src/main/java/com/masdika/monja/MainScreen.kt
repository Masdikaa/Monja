package com.masdika.monja

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.masdika.monja.ui.component.MainBottomBar
import com.masdika.monja.ui.dashboard.DashboardRoute
import com.masdika.monja.ui.dashboard.dashboardScreenRoute
import com.masdika.monja.ui.history.historyScreenRoute

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            MainBottomBar(navController = navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = DashboardRoute,
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            dashboardScreenRoute()
            historyScreenRoute()
        }
    }
}