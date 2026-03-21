package com.masdika.monja

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.masdika.monja.ui.component.MainBottomBar
import com.masdika.monja.ui.dashboard.DashboardRoute
import com.masdika.monja.ui.dashboard.DashboardViewModel
import com.masdika.monja.ui.dashboard.dashboardScreenRoute
import com.masdika.monja.ui.history.historyScreenRoute

@Composable
fun MainScreen(
    dashboardViewModel: DashboardViewModel = hiltViewModel()
) {
    val navController = rememberNavController()

    val dashboardState by dashboardViewModel.state.collectAsStateWithLifecycle()
    val activeMacAddress = dashboardState.selectedDevice?.macAddress ?: ""

    Log.e("CCTV_HISTORY", "1. MainScreen sending MAC: '$activeMacAddress'")

    Scaffold(
        bottomBar = {
            MainBottomBar(navController = navController, currentMacAddress = activeMacAddress)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = DashboardRoute,
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            dashboardScreenRoute(viewModel = dashboardViewModel)
            historyScreenRoute()
        }
    }
}