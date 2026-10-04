package com.dct.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.dct.app.feature.assistant.AssistantScreen
import com.dct.app.feature.github.GitHubScreen
import com.dct.app.feature.home.HomeScreen
import com.dct.app.feature.project.ProjectDetailScreen
import com.dct.app.feature.settings.SettingsScreen
import com.dct.app.feature.terminal.TerminalScreen

@Composable
fun DctNavHost(nav: NavHostController, modifier: Modifier = Modifier) {
    NavHost(nav, startDestination = Destination.Home.route, modifier = modifier) {
        composable(Destination.Home.route) {
            HomeScreen(
                onOpenProject = { nav.navigate(Routes.project(it)) },
                onNavigate = { nav.navigateTopLevel(it) },
            )
        }
        composable(
            route = Routes.PROJECT,
            arguments = listOf(navArgument(Routes.PROJECT_ARG) { type = NavType.StringType }),
        ) { entry ->
            val id = entry.arguments?.getString(Routes.PROJECT_ARG).orEmpty()
            ProjectDetailScreen(projectId = id, onBack = { nav.popBackStack() })
        }
        composable(Destination.Assistant.route) { AssistantScreen() }
        composable(Destination.Terminal.route) { TerminalScreen() }
        composable(Destination.GitHub.route) { GitHubScreen() }
        composable(Destination.Settings.route) { SettingsScreen() }
    }
}
