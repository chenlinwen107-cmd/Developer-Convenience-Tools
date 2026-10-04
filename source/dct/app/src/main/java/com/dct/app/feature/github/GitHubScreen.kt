package com.dct.app.feature.github

import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.dct.app.core.ui.components.ActionFlow
import com.dct.app.core.ui.components.ChipTone
import com.dct.app.core.ui.components.DctScreen
import com.dct.app.core.ui.components.DiffView
import com.dct.app.core.ui.components.PhaseNote
import com.dct.app.core.ui.components.SectionCard
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
            SectionCard("连接") {
                FilledTonalButton(onClick = {}, enabled = false) { Text("连接 GitHub 账号") }
                PhaseNote("OAuth 与 Token 安全存储将在后续阶段实现。")
            }
        }
        item { Text("仓库", style = MaterialTheme.typography.titleMedium) }
        items(state.repos, key = { it.id }) { r ->
            SectionCard(r.fullName) {
                Text(r.description, style = MaterialTheme.typography.bodyMedium)
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(DctSpacing.sm)) {
                    if (r.linkedProjectId != null) StatusChip("已关联项目", ChipTone.SUCCESS) else StatusChip("未关联", ChipTone.NEUTRAL)
                    FilledTonalButton(onClick = {}, enabled = false) { Text("关联项目") }
                }
            }
        }
        item {
            SectionCard("提交历史（Mock）") {
                state.commits.forEach { c ->
                    Text("${c.hash}  ${c.message}", style = MaterialTheme.typography.bodyMedium)
                    Text("${c.author} · ${c.time}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
        item {
            SectionCard("工作区差异（Mock）") {
                if (state.diff.isNotEmpty()) DiffView(state.diff)
            }
        }
        item {
            SectionCard("提交 / 推送 / PR") {
                ActionFlow {
                    FilledTonalButton(onClick = {}, enabled = false) { Text("提交") }
                    FilledTonalButton(onClick = {}, enabled = false) { Text("推送") }
                    FilledTonalButton(onClick = {}, enabled = false) { Text("创建 PR") }
                }
                PhaseNote("推送等高风险操作后续将加入明确确认。")
            }
        }
    }
}
