package com.dct.app.navigation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

/** 宽度 >= 600dp（横屏手机、平板）使用 NavigationRail，否则使用底部导航栏。 */
@Composable
fun DctApp() {
    val nav = rememberNavController()
    val backStackEntry by nav.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val selectedRoute = if (currentRoute == Routes.PROJECT) Destination.Home.route else currentRoute
    val useRail = LocalConfiguration.current.screenWidthDp >= 600

    Scaffold(
        bottomBar = {
            if (!useRail) {
                NavigationBar {
                    TopLevelDestinations.forEach { d ->
                        NavigationBarItem(
                            selected = selectedRoute == d.route,
                            onClick = { nav.navigateTopLevel(d) },
                            icon = { Icon(d.icon, contentDescription = d.label) },
                            label = { Text(d.label) },
                        )
                    }
                }
            }
        },
    ) { padding ->
        if (useRail) {
            Row(Modifier.fillMaxSize().padding(padding)) {
                NavigationRail {
                    TopLevelDestinations.forEach { d ->
                        NavigationRailItem(
                            selected = selectedRoute == d.route,
                            onClick = { nav.navigateTopLevel(d) },
                            icon = { Icon(d.icon, contentDescription = d.label) },
                            label = { Text(d.label) },
                        )
                    }
                }
                DctNavHost(nav, Modifier.weight(1f))
            }
        } else {
            DctNavHost(nav, Modifier.padding(padding))
        }
    }
}

fun NavHostController.navigateTopLevel(d: Destination) {
    navigate(d.route) {
        popUpTo(graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
