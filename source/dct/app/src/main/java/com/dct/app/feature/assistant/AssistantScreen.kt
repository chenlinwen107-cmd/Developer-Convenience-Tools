package com.dct.app.feature.assistant

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.dct.app.core.ui.components.ChipTone
import com.dct.app.core.ui.components.DctScreen
import com.dct.app.core.ui.components.DiffView
import com.dct.app.core.ui.components.PhaseNote
import com.dct.app.core.ui.components.SectionCard
import com.dct.app.core.ui.components.SimulatedBanner
import com.dct.app.core.ui.components.StatusChip
import com.dct.app.core.ui.theme.DctSpacing
import com.dct.app.di.LocalAppContainer
import com.dct.app.domain.model.ChangeProposal
import com.dct.app.domain.model.ChangeState
import com.dct.app.domain.model.MessageRole

@Composable
fun AssistantScreen() {
    val container = LocalAppContainer.current
    val vm: AssistantViewModel = viewModel(
        factory = viewModelFactory { initializer { AssistantViewModel(container.aiAssistantRepository) } }
    )
    val state by vm.uiState.collectAsStateWithLifecycle()
    var input by rememberSaveable { mutableStateOf("") }

    DctScreen("AI 助手") {
        item { SimulatedBanner("AI 回复为 Mock 数据，未调用任何真实模型。批准修改只改变状态，不会修改真实文件。") }
        item { Text("当前项目：tovikeli-web（Mock）", style = MaterialTheme.typography.labelLarge) }
        items(state.messages, key = { it.id }) { m ->
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(DctSpacing.md)) {
                    Text(
                        if (m.role == MessageRole.USER) "你" else "AI（Mock）",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Text(m.text)
                }
            }
        }
        item {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(DctSpacing.sm)) {
                OutlinedTextField(
                    value = input,
                    onValueChange = { input = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("向 AI 描述需求…") },
                )
                Button(onClick = { vm.send(input); input = "" }, enabled = input.isNotBlank()) {
                    Text("发送")
                }
            }
        }
        item { Text("修改", style = MaterialTheme.typography.titleMedium) }
        items(state.proposals, key = { it.id }) { p ->
            ProposalCard(p, onApprove = { vm.approve(p.id) }, onReject = { vm.reject(p.id) })
        }
    }
}

@Composable
private fun ProposalCard(p: ChangeProposal, onApprove: () -> Unit, onReject: () -> Unit) {
    SectionCard(p.title) {
        Row(horizontalArrangement = Arrangement.spacedBy(DctSpacing.sm), verticalAlignment = Alignment.CenterVertically) {
            StatusChip(stateLabel(p.state), stateTone(p.state))
            Text(p.filePath, style = MaterialTheme.typography.bodySmall)
        }
        Text(p.summary, style = MaterialTheme.typography.bodyMedium)
        if (p.diff.isNotEmpty()) DiffView(p.diff)
        if (p.state == ChangeState.PENDING_REVIEW) {
            Row(horizontalArrangement = Arrangement.spacedBy(DctSpacing.sm)) {
                Button(onClick = onApprove) { Text("批准") }
                OutlinedButton(onClick = onReject) { Text("拒绝") }
            }
            PhaseNote("先审查差异，再决定是否应用。")
        }
    }
}

private fun stateLabel(s: ChangeState) = when (s) {
    ChangeState.SUGGESTION -> "建议"
    ChangeState.PENDING_REVIEW -> "待审查修改"
    ChangeState.APPLIED -> "已应用修改"
    ChangeState.REJECTED -> "已拒绝"
}

private fun stateTone(s: ChangeState) = when (s) {
    ChangeState.SUGGESTION -> ChipTone.NEUTRAL
    ChangeState.PENDING_REVIEW -> ChipTone.WARNING
    ChangeState.APPLIED -> ChipTone.SUCCESS
    ChangeState.REJECTED -> ChipTone.ERROR
}
