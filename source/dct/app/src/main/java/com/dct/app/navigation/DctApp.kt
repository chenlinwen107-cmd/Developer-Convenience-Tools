package com.dct.app.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dct.app.core.ui.theme.DctDimens
import com.dct.app.core.ui.theme.dct

/** 宽度 >= 600dp（横屏手机、平板）使用 NavigationRail，否则使用底部导航栏。 */
@Composable
fun DctApp() {
    val nav = rememberNavController()
    val backStackEntry by nav.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val selectedRoute = if (currentRoute == Routes.PROJECT) Destination.Home.route else currentRoute
    val useRail = LocalConfiguration.current.screenWidthDp >= 600

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (!useRail) {
                DctBottomBar(selectedRoute) { nav.navigateTopLevel(it) }
            }
        },
    ) { padding ->
        if (useRail) {
            Row(Modifier.fillMaxSize().padding(padding)) {
                DctSideRail(selectedRoute) { nav.navigateTopLevel(it) }
                VerticalDivider(thickness = DctDimens.borderWidth, color = MaterialTheme.dct.border)
                DctNavHost(nav, Modifier.weight(1f))
            }
        } else {
            DctNavHost(nav, Modifier.padding(padding))
        }
    }
}

/**
 * 底部导航：半透明白/黑底 + 顶部细边框，不使用模糊。
 * 背景铺满到手势区域；内容通过 Scaffold 的 padding 位于导航栏上方，不会被遮挡。
 */
@Composable
private fun DctBottomBar(selectedRoute: String?, onSelect: (Destination) -> Unit) {
    val scheme = MaterialTheme.colorScheme
    Column(Modifier.background(scheme.surface.copy(alpha = 0.94f))) {
        HorizontalDivider(thickness = DctDimens.borderWidth, color = MaterialTheme.dct.border)
        NavigationBar(containerColor = Color.Transparent, tonalElevation = 0.dp) {
            TopLevelDestinations.forEach { d ->
                NavigationBarItem(
                    selected = selectedRoute == d.route,
                    onClick = { onSelect(d) },
                    icon = { Icon(d.icon, contentDescription = d.label) },
                    label = { Text(d.label) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = scheme.primary,
                        selectedTextColor = scheme.primary,
                        indicatorColor = Color.Transparent,
                        unselectedIconColor = scheme.onSurfaceVariant,
                        unselectedTextColor = scheme.onSurfaceVariant,
                    ),
                )
            }
        }
    }
}

@Composable
private fun DctSideRail(selectedRoute: String?, onSelect: (Destination) -> Unit) {
    val scheme = MaterialTheme.colorScheme
    NavigationRail(
        containerColor = scheme.surface,
        header = {
            Text(
                "DCT",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 12.dp),
            )
        },
    ) {
        TopLevelDestinations.forEach { d ->
            NavigationRailItem(
                selected = selectedRoute == d.route,
                onClick = { onSelect(d) },
                icon = { Icon(d.icon, contentDescription = d.label) },
                label = { Text(d.label) },
                colors = NavigationRailItemDefaults.colors(
                    selectedIconColor = scheme.primary,
                    selectedTextColor = scheme.primary,
                    indicatorColor = scheme.primaryContainer,
                    unselectedIconColor = scheme.onSurfaceVariant,
                    unselectedTextColor = scheme.onSurfaceVariant,
                ),
            )
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
