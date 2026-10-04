package com.dct.app.feature.github

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.dct.app.core.ui.components.ChipTone
import com.dct.app.core.ui.components.CodeTag
import com.dct.app.core.ui.components.ComingSoonRow
import com.dct.app.core.ui.components.DctDivider
import com.dct.app.core.ui.components.DctPanel
import com.dct.app.core.ui.components.DctScreen
import com.dct.app.core.ui.components.DiffView
import com.dct.app.core.ui.components.ListRow
import com.dct.app.core.ui.components.PhaseNote
import com.dct.app.core.ui.components.SectionLabel
import com.dct.app.core.ui.components.SimulatedBanner
import com.dct.app.core.ui.components.StatusChip
import com.dct.app.core.ui.theme.DctSpacing
import com.dct.app.di.LocalAppContainer

@Composable
fun GitHubScreen() {
    val container = LocalAppContainer.current
    val vm: GitHubViewModel = viewModel(
        factory = viewModelFactory { initializer { GitHubViewModel(container.gitHubRepository) } }
    )
    val state by vm.uiState.collectAsStateWithLifecycle()

    DctScreen("GitHub") {
        item { SimulatedBanner("Mock 数据：未进行 GitHub 授权，也没有任何网络请求。") }
        item {
            DctPanel {
                ComingSoonRow(
                    "连接 GitHub 账号",
                    description = "OAuth 与 Token 安全存储将在后续阶段实现。",
                )
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(DctSpacing.sm)) {
                SectionLabel("仓库")
                DctPanel {
                    state.repos.forEachIndexed { index, r ->
                        if (index > 0) DctDivider()
                        ListRow(
                            title = r.fullName,
                            subtitle = r.description,
                            trailing = {
                                if (r.linkedProjectId != null) {
                                    StatusChip("已关联项目", ChipTone.SUCCESS)
                                } else {
                                    StatusChip("未关联", ChipTone.NEUTRAL)
                                }
                            },
                        )
                    }
                    if (state.repos.isNotEmpty()) DctDivider()
                    ComingSoonRow("关联项目")
                }
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(DctSpacing.sm)) {
                SectionLabel("提交历史（Mock）")
                DctPanel {
                    state.commits.forEachIndexed { index, c ->
                        if (index > 0) DctDivider()
                        ListRow(
                            title = c.message,
                            subtitle = "${c.author} · ${c.time}",
                            leading = { CodeTag(c.hash) },
                        )
                    }
                }
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(DctSpacing.sm)) {
                SectionLabel("工作区差异（Mock）")
                if (state.diff.isNotEmpty()) DiffView(state.diff)
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(DctSpacing.sm)) {
                SectionLabel("提交 / 推送 / PR")
                DctPanel {
                    ComingSoonRow("提交")
                    DctDivider()
                    ComingSoonRow("推送")
                    DctDivider()
                    ComingSoonRow("创建 PR")
                }
                PhaseNote("推送等高风险操作后续将加入明确确认。")
            }
        }
    }
}
