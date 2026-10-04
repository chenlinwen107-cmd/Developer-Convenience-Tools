package com.dct.app.feature.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.dct.app.core.ui.components.ActionFlow
import com.dct.app.core.ui.components.ChipTone
import com.dct.app.core.ui.components.DctScreen
import com.dct.app.core.ui.components.PhaseNote
import com.dct.app.core.ui.components.SectionCard
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
            SectionCard("快速操作") {
                ActionFlow {
                    FilledTonalButton(onClick = {}, enabled = false) { Text("新建项目") }
                    FilledTonalButton(onClick = {}, enabled = false) { Text("导入本地项目") }
                    FilledTonalButton(onClick = {}, enabled = false) { Text("连接 GitHub") }
                }
                PhaseNote("以上入口将在后续阶段实现。")
                ActionFlow {
                    Button(onClick = { onNavigate(Destination.Assistant) }) { Text("AI 助手") }
                    Button(onClick = { onNavigate(Destination.Terminal) }) { Text("终端") }
                }
            }
        }
        item { Text("最近项目", style = MaterialTheme.typography.titleMedium) }
        items(state.projects, key = { it.id }) { p ->
            Card(Modifier.fillMaxWidth().clickable { onOpenProject(p.id) }) {
                Column(Modifier.padding(DctSpacing.md), verticalArrangement = Arrangement.spacedBy(DctSpacing.xs)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(DctSpacing.sm)) {
                        Text(p.name, style = MaterialTheme.typography.titleSmall)
                        StatusChip(p.branch, ChipTone.INFO)
                        StatusChip(gitStatusLabel(p.gitStatus), gitStatusTone(p.gitStatus))
                    }
                    Text(p.path, style = MaterialTheme.typography.bodySmall)
                    Text("最近打开：${p.lastOpened}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
        item { Text("最近任务", style = MaterialTheme.typography.titleMedium) }
        items(state.recentTasks, key = { it.id }) { t ->
            Card(Modifier.fillMaxWidth()) {
                Row(
                    Modifier.padding(DctSpacing.md),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(DctSpacing.sm),
                ) {
                    Text(t.command, Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
                    StatusChip("模拟", ChipTone.WARNING)
                    StatusChip(commandStatusLabel(t.status), commandStatusTone(t.status))
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
