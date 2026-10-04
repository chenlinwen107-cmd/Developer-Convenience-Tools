package com.dct.app.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.dct.app.core.ui.icons.DctIcons

sealed class Destination(val route: String, val label: String, val icon: ImageVector) {
    data object Home : Destination("home", "工作台", DctIcons.Workbench)
    data object Assistant : Destination("assistant", "AI 助手", DctIcons.Assistant)
    data object Terminal : Destination("terminal", "终端", DctIcons.Terminal)
    data object GitHub : Destination("github", "GitHub", DctIcons.Branch)
    data object Settings : Destination("settings", "设置", DctIcons.Settings)
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
