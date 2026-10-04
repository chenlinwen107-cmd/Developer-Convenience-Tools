package com.dct.app.feature.terminal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.dct.app.core.ui.components.ChipTone
import com.dct.app.core.ui.components.ComingSoonRow
import com.dct.app.core.ui.components.DctButton
import com.dct.app.core.ui.components.DctDivider
import com.dct.app.core.ui.components.DctPanel
import com.dct.app.core.ui.components.DctScreen
import com.dct.app.core.ui.components.LogBlock
import com.dct.app.core.ui.components.SectionLabel
import com.dct.app.core.ui.components.SimulatedBanner
import com.dct.app.core.ui.components.StatusChip
import com.dct.app.core.ui.theme.CodeTextStyle
import com.dct.app.core.ui.theme.DctSpacing
import com.dct.app.di.LocalAppContainer
import com.dct.app.domain.model.CommandStatus
import com.dct.app.feature.home.commandStatusLabel
import com.dct.app.feature.home.commandStatusTone

@Composable
fun TerminalScreen() {
    val container = LocalAppContainer.current
    val vm: TerminalViewModel = viewModel(
        factory = viewModelFactory { initializer { TerminalViewModel(container.commandRepository) } }
    )
    val state by vm.uiState.collectAsStateWithLifecycle()
    var command by rememberSaveable { mutableStateOf("") }

    DctScreen("终端 / 命令任务") {
        item { SimulatedBanner("模拟输出：命令并未在 Termux 中真实执行。包含 “fail” 的命令会模拟失败。") }
        item {
            DctPanel {
                Column(Modifier.padding(DctSpacing.md), verticalArrangement = Arrangement.spacedBy(DctSpacing.sm)) {
                    Text("命令", style = MaterialTheme.typography.titleMedium)
                    OutlinedTextField(
                        value = command,
                        onValueChange = { command = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = MaterialTheme.shapes.small,
                        textStyle = CodeTextStyle.copy(color = MaterialTheme.colorScheme.onSurface),
                        placeholder = { Text("例如：git status", style = CodeTextStyle) },
                    )
                    DctButton(
                        text = "模拟运行",
                        onClick = { vm.submit(command); command = "" },
                        enabled = command.isNotBlank(),
                    )
                }
                DctDivider()
                ComingSoonRow("交互式终端", description = "预留入口，真实 Termux 执行将在后续阶段接入。")
            }
        }
        item { SectionLabel("命令历史") }
        items(state.tasks, key = { it.id }) { t ->
            DctPanel {
                Column(Modifier.padding(DctSpacing.md), verticalArrangement = Arrangement.spacedBy(DctSpacing.sm)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(DctSpacing.sm),
                    ) {
                        Text(
                            "\$ ${t.command}",
                            modifier = Modifier.weight(1f),
                            style = CodeTextStyle.copy(fontWeight = FontWeight.SemiBold),
                        )
                        if (t.isSimulated) StatusChip("模拟", ChipTone.WARNING)
                        StatusChip(commandStatusLabel(t.status), commandStatusTone(t.status))
                    }
                    LogBlock(t.output)
                    if (t.status != CommandStatus.RUNNING) {
                        Text(
                            "退出码 ${t.exitCode ?: "-"} · ${t.durationMs ?: 0} ms",
                            style = CodeTextStyle.copy(fontSize = 12.sp, lineHeight = 16.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    if (t.status == CommandStatus.FAILED) {
                        Text(
                            "命令失败，请查看上方日志。",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
            }
        }
    }
}
