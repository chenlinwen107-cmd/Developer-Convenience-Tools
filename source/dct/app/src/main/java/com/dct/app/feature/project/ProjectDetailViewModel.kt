package com.dct.app.feature.project

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dct.app.domain.model.Project
import com.dct.app.domain.repository.ProjectRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProjectDetailUiState(
    val loading: Boolean = true,
    val project: Project? = null,
)

class ProjectDetailViewModel(
    private val projects: ProjectRepository,
    private val projectId: String,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProjectDetailUiState())
    val uiState: StateFlow<ProjectDetailUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.value = ProjectDetailUiState(loading = false, project = projects.getProject(projectId))
        }
    }
}
