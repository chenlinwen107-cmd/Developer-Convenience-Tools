package com.dct.app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Destination(val route: String, val label: String, val icon: ImageVector) {
    data object Home : Destination("home", "工作台", Icons.Filled.Home)
    data object Assistant : Destination("assistant", "AI 助手", Icons.Filled.Create)
    data object Terminal : Destination("terminal", "终端", Icons.Filled.PlayArrow)
    data object GitHub : Destination("github", "GitHub", Icons.Filled.Share)
    data object Settings : Destination("settings", "设置", Icons.Filled.Settings)
}

/** 顶层导航项。放在类外，避免 sealed class 与其 companion 的静态初始化顺序问题。 */
val TopLevelDestinations: List<Destination> by lazy {
    listOf(Destination.Home, Destination.Assistant, Destination.Terminal, Destination.GitHub, Destination.Settings)
}

object Routes {
    const val PROJECT_ARG = "projectId"
    const val PROJECT = "project/{$PROJECT_ARG}"
    fun project(id: String) = "project/$id"
}
