package com.dct.app.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.dct.app.core.ui.components.ActionTile
import com.dct.app.core.ui.components.ChipTone
import com.dct.app.core.ui.components.CodeTag
import com.dct.app.core.ui.components.ComingSoonRow
import com.dct.app.core.ui.components.DctDivider
import com.dct.app.core.ui.components.DctPanel
import com.dct.app.core.ui.components.DctScreen
import com.dct.app.core.ui.components.ListRow
import com.dct.app.core.ui.components.SectionLabel
import com.dct.app.core.ui.components.SimulatedBanner
import com.dct.app.core.ui.components.StatusChip
import com.dct.app.core.ui.theme.DctSpacing
import com.dct.app.di.LocalAppContainer
import com.dct.app.domain.model.CommandStatus
import com.dct.app.domain.model.GitStatus
import com.dct.app.navigation.Destination

@Composable
fun HomeScreen(
    onOpenProject: (String) -> Unit,
    onNavigate: (Destination) -> Unit,
) {
    val container = LocalAppContainer.current
    val vm: HomeViewModel = viewModel(
        factory = viewModelFactory {
            initializer { HomeViewModel(container.projectRepository, container.commandRepository) }
        }
    )
    val state by vm.uiState.collectAsStateWithLifecycle()

    DctScreen("工作台") {
        item { SimulatedBanner("第一阶段：所有数据均为 Mock，不会访问真实项目、GitHub 或 Termux。") }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(DctSpacing.sm)) {
                SectionLabel("快速进入")
                Row(
                    Modifier.height(IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.spacedBy(DctSpacing.sm),
                ) {
                    ActionTile(
                        title = "AI 助手",
                        description = "对话式代码协助",
                        onClick = { onNavigate(Destination.Assistant) },
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                        primary = true,
                    )
                    ActionTile(
                        title = "终端",
                        description = "命令任务与日志",
                        onClick = { onNavigate(Destination.Terminal) },
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                    )
                }
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(DctSpacing.sm)) {
                SectionLabel("更多入口")
                DctPanel {
                    ComingSoonRow("新建项目")
                    DctDivider()
                    ComingSoonRow("导入本地项目")
                    DctDivider()
                    ComingSoonRow("连接 GitHub")
                }
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(DctSpacing.sm)) {
                SectionLabel("最近项目")
                DctPanel {
                    if (state.projects.isEmpty()) {
                        Text(
                            "暂无项目",
                            Modifier.padding(DctSpacing.md),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    state.projects.forEachIndexed { index, p ->
                        if (index > 0) DctDivider()
                        ListRow(
                            title = p.name,
                            subtitle = "${p.path}\n最近打开：${p.lastOpened}",
                            onClick = { onOpenProject(p.id) },
                            trailing = {
                                Column(
                                    horizontalAlignment = Alignment.End,
                                    verticalArrangement = Arrangement.spacedBy(4.dp),
                                ) {
                                    StatusChip(gitStatusLabel(p.gitStatus), gitStatusTone(p.gitStatus))
                                    CodeTag(p.branch)
                                }
                            },
                        )
                    }
                }
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(DctSpacing.sm)) {
                SectionLabel("最近任务")
                DctPanel {
                    if (state.recentTasks.isEmpty()) {
                        Text(
                            "暂无任务",
                            Modifier.padding(DctSpacing.md),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    state.recentTasks.forEachIndexed { index, t ->
                        if (index > 0) DctDivider()
                        ListRow(
                            title = t.command,
                            monoTitle = true,
                            trailing = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                ) {
                                    StatusChip("模拟", ChipTone.WARNING)
                                    StatusChip(commandStatusLabel(t.status), commandStatusTone(t.status))
                                }
                            },
                        )
                    }
                }
            }
        }
    }
}

fun gitStatusLabel(s: GitStatus) = when (s) {
    GitStatus.CLEAN -> "干净"
    GitStatus.MODIFIED -> "有修改"
    GitStatus.AHEAD -> "待推送"
}

fun gitStatusTone(s: GitStatus) = when (s) {
    GitStatus.CLEAN -> ChipTone.SUCCESS
    GitStatus.MODIFIED -> ChipTone.WARNING
    GitStatus.AHEAD -> ChipTone.INFO
}

fun commandStatusLabel(s: CommandStatus) = when (s) {
    CommandStatus.RUNNING -> "运行中"
    CommandStatus.SUCCESS -> "成功"
    CommandStatus.FAILED -> "失败"
}

fun commandStatusTone(s: CommandStatus) = when (s) {
    CommandStatus.RUNNING -> ChipTone.INFO
    CommandStatus.SUCCESS -> ChipTone.SUCCESS
    CommandStatus.FAILED -> ChipTone.ERROR
}
