package com.dct.app.feature.project

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.dct.app.core.ui.components.ComingSoonRow
import com.dct.app.core.ui.components.DctDivider
import com.dct.app.core.ui.components.DctPanel
import com.dct.app.core.ui.components.DctScreen
import com.dct.app.core.ui.components.KeyValueRow
import com.dct.app.core.ui.components.SectionLabel
import com.dct.app.core.ui.components.StatusChip
import com.dct.app.core.ui.theme.DctSpacing
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
            item {
                Text(
                    if (state.loading) "加载中…" else "未找到该项目（Mock 数据）",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        } else {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(DctSpacing.sm)) {
                    SectionLabel("基本信息")
                    DctPanel {
                        KeyValueRow("路径", project.path, mono = true)
                        DctDivider()
                        KeyValueRow("分支", project.branch, mono = true)
                        DctDivider()
                        KeyValueRow("Git 状态") {
                            StatusChip(gitStatusLabel(project.gitStatus), gitStatusTone(project.gitStatus))
                        }
                    }
                }
            }
            item {
                Column(verticalArrangement = Arrangement.spacedBy(DctSpacing.sm)) {
                    SectionLabel("文件与修改记录")
                    DctPanel {
                        ComingSoonRow("文件列表")
                        DctDivider()
                        ComingSoonRow("最近修改记录")
                    }
                }
            }
            item {
                Column(verticalArrangement = Arrangement.spacedBy(DctSpacing.sm)) {
                    SectionLabel("操作")
                    DctPanel {
                        ComingSoonRow("运行")
                        DctDivider()
                        ComingSoonRow("测试")
                        DctDivider()
                        ComingSoonRow("查看差异")
                    }
                }
            }
        }
    }
}
