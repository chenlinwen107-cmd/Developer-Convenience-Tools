package com.dct.app.feature.project

import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.dct.app.core.ui.components.ActionFlow
import com.dct.app.core.ui.components.ChipTone
import com.dct.app.core.ui.components.DctScreen
import com.dct.app.core.ui.components.PhaseNote
import com.dct.app.core.ui.components.SectionCard
import com.dct.app.core.ui.components.StatusChip
import com.dct.app.di.LocalAppContainer
import com.dct.app.feature.home.gitStatusLabel
import com.dct.app.feature.home.gitStatusTone

@Composable
fun ProjectDetailScreen(projectId: String, onBack: () -> Unit) {
    val container = LocalAppContainer.current
    val vm: ProjectDetailViewModel = viewModel(
        key = "project-$projectId",
        factory = viewModelFactory {
            initializer { ProjectDetailViewModel(container.projectRepository, projectId) }
        },
    )
    val state by vm.uiState.collectAsStateWithLifecycle()
    val project = state.project

    DctScreen(project?.name ?: "项目详情", onBack = onBack) {
        if (project == null) {
            item { Text(if (state.loading) "加载中…" else "未找到该项目（Mock 数据）") }
        } else {
            item {
                SectionCard("基本信息") {
                    Text("路径：${project.path}")
                    ActionFlow {
                        StatusChip("分支 ${project.branch}", ChipTone.INFO)
                        StatusChip(gitStatusLabel(project.gitStatus), gitStatusTone(project.gitStatus))
                    }
                }
            }
            item {
                SectionCard("文件与修改记录") {
                    FilledTonalButton(onClick = {}, enabled = false) { Text("文件列表") }
                    FilledTonalButton(onClick = {}, enabled = false) { Text("最近修改记录") }
                    PhaseNote("占位入口，后续阶段实现。")
                }
            }
            item {
                SectionCard("操作") {
                    ActionFlow {
                        FilledTonalButton(onClick = {}, enabled = false) { Text("运行") }
                        FilledTonalButton(onClick = {}, enabled = false) { Text("测试") }
                        FilledTonalButton(onClick = {}, enabled = false) { Text("查看差异") }
                    }
                    PhaseNote("占位入口，后续阶段实现。")
                }
            }
        }
    }
}
