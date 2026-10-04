package com.dct.app.feature.terminal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dct.app.domain.model.CommandTask
import com.dct.app.domain.repository.CommandExecutionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class TerminalUiState(val tasks: List<CommandTask> = emptyList())

class TerminalViewModel(private val repo: CommandExecutionRepository) : ViewModel() {
    val uiState: StateFlow<TerminalUiState> =
        repo.observeTasks().map { TerminalUiState(it) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), TerminalUiState())

    /** 仅提交给 Repository；终端页面自身不执行任何命令。 */
    fun submit(command: String) {
        viewModelScope.launch { repo.submit(command) }
    }
}
