package com.masdika.monja.component

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.masdika.monja.component.NavigationItem

@Composable
fun MainBottomBar(
    navController: NavController
) {
    val navigationItem = listOf(
        NavigationItem.Dashboard,
        NavigationItem.History
    )

    val backStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry.value?.destination?.route

    NavigationBar {
        navigationItem.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route.toString(),
                onClick = {
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                label = { Text(item.title) }
            )
        }
    }
}