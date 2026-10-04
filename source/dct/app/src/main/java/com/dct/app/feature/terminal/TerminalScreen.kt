package com.dct.app.feature.terminal

import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.dct.app.core.ui.components.ChipTone
import com.dct.app.core.ui.components.DctScreen
import com.dct.app.core.ui.components.LogBlock
import com.dct.app.core.ui.components.PhaseNote
import com.dct.app.core.ui.components.SectionCard
import com.dct.app.core.ui.components.SimulatedBanner
import com.dct.app.core.ui.components.StatusChip
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
            SectionCard("命令") {
                OutlinedTextField(
                    value = command,
                    onValueChange = { command = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("例如：git status") },
                )
                Row(horizontalArrangement = Arrangement.spacedBy(DctSpacing.sm)) {
                    Button(onClick = { vm.submit(command); command = "" }, enabled = command.isNotBlank()) {
                        Text("模拟运行")
                    }
                    FilledTonalButton(onClick = {}, enabled = false) { Text("交互式终端（预留）") }
                }
                PhaseNote("交互式终端与真实 Termux 执行将在后续阶段接入。")
            }
        }
        item { Text("命令历史", style = MaterialTheme.typography.titleMedium) }
        items(state.tasks, key = { it.id }) { t ->
            SectionCard(t.command) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(DctSpacing.sm)) {
                    if (t.isSimulated) StatusChip("模拟", ChipTone.WARNING)
                    StatusChip(commandStatusLabel(t.status), commandStatusTone(t.status))
                    if (t.status != CommandStatus.RUNNING) {
                        Text(
                            "退出码 ${t.exitCode ?: "-"} · ${t.durationMs ?: 0} ms",
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
                LogBlock(t.output)
                if (t.status == CommandStatus.FAILED) {
                    Text("命令失败，请查看上方日志。", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

