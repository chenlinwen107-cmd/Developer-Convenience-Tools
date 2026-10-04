package com.dct.app.feature.assistant

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.dct.app.core.ui.components.ChipTone
import com.dct.app.core.ui.components.DctButton
import com.dct.app.core.ui.components.DctButtonVariant
import com.dct.app.core.ui.components.DctScreen
import com.dct.app.core.ui.components.DiffView
import com.dct.app.core.ui.components.PhaseNote
import com.dct.app.core.ui.components.SectionCard
import com.dct.app.core.ui.components.SectionLabel
import com.dct.app.core.ui.components.SimulatedBanner
import com.dct.app.core.ui.components.StatusChip
import com.dct.app.core.ui.theme.CodeTextStyle
import com.dct.app.core.ui.theme.DctSpacing
import com.dct.app.di.LocalAppContainer
import com.dct.app.domain.model.AiMessage
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
        item {
            Text(
                "当前项目：tovikeli-web（Mock）",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        items(state.messages, key = { it.id }) { m -> ChatMessage(m) }
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(DctSpacing.sm),
            ) {
                OutlinedTextField(
                    value = input,
                    onValueChange = { input = it },
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.small,
                    placeholder = { Text("向 AI 描述需求…") },
                )
                DctButton(
                    text = "发送",
                    onClick = { vm.send(input); input = "" },
                    enabled = input.isNotBlank(),
                )
            }
        }
        item { SectionLabel("修改") }
        items(state.proposals, key = { it.id }) { p ->
            ProposalCard(p, onApprove = { vm.approve(p.id) }, onReject = { vm.reject(p.id) })
        }
    }
}

/** 用户消息靠右、浅灰气泡；AI 消息靠左、无气泡，保持自然的聊天层级。 */
@Composable
private fun ChatMessage(m: AiMessage) {
    if (m.role == MessageRole.USER) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurface,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.widthIn(max = 320.dp),
            ) {
                Text(
                    m.text,
                    Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    } else {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                "AI（Mock）",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(m.text, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun ProposalCard(p: ChangeProposal, onApprove: () -> Unit, onReject: () -> Unit) {
    SectionCard(p.title) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(DctSpacing.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            StatusChip(stateLabel(p.state), stateTone(p.state))
            Text(
                p.filePath,
                style = CodeTextStyle,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Text(p.summary, style = MaterialTheme.typography.bodyMedium)
        if (p.diff.isNotEmpty()) DiffView(p.diff)
        if (p.state == ChangeState.PENDING_REVIEW) {
            Row(horizontalArrangement = Arrangement.spacedBy(DctSpacing.sm)) {
                DctButton("批准", onApprove)
                DctButton("拒绝", onReject, variant = DctButtonVariant.SECONDARY)
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
