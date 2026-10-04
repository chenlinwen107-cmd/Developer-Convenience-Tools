package com.dct.app.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dct.app.domain.model.CommandTask
import com.dct.app.domain.model.Project
import com.dct.app.domain.repository.CommandExecutionRepository
import com.dct.app.domain.repository.ProjectRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class HomeUiState(
    val projects: List<Project> = emptyList(),
    val recentTasks: List<CommandTask> = emptyList(),
)

class HomeViewModel(
    projects: ProjectRepository,
    commands: CommandExecutionRepository,
) : ViewModel() {
    val uiState: StateFlow<HomeUiState> =
        combine(projects.observeProjects(), commands.observeTasks()) { p, t ->
            HomeUiState(projects = p, recentTasks = t.take(3))
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HomeUiState())
}
