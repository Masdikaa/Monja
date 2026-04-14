package com.masdika.monja

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import com.masdika.monja.ui.splash.SplashRoute
import com.masdika.monja.ui.splash.splashScreenRoute

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val isHiddenScreen =
        currentDestination?.hasRoute<SplashRoute>() == true || currentDestination?.hasRoute<AnalyticRoute>() == true

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = !isHiddenScreen,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(durationMillis = 500)
                ) + fadeIn(animationSpec = tween(durationMillis = 500)),
                exit = slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(durationMillis = 500)
                ) + fadeOut(animationSpec = tween(durationMillis = 500))
            ) {
                MainBottomBar(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = SplashRoute,
            modifier = Modifier.padding(bottom = if (!isHiddenScreen) innerPadding.calculateBottomPadding() else 0.dp)
        ) {
            splashScreenRoute(
                onNavigateToDashboard = {
                    navController.navigate(DashboardRoute) {
                        popUpTo<SplashRoute> { inclusive = true }
                    }
                }
            )
            dashboardScreenRoute(
                onNavigateToAnalytic = { macAddress, vitalType ->
                    navController.navigate(AnalyticRoute(macAddress, vitalType))
                }
            )
            historyScreenRoute()
            analyticScreenRoute()
        }
    }
}