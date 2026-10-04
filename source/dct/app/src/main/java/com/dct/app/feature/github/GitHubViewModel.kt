package com.dct.app.feature.github

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dct.app.domain.model.CommitInfo
import com.dct.app.domain.model.GitHubRepo
import com.dct.app.domain.repository.GitHubRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class GitHubUiState(
    val repos: List<GitHubRepo> = emptyList(),
    val commits: List<CommitInfo> = emptyList(),
    val diff: String = "",
)

class GitHubViewModel(private val repo: GitHubRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(GitHubUiState())
    val uiState: StateFlow<GitHubUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repo.observeRepos().collect { repos ->
                _uiState.update { it.copy(repos = repos) }
                if (repos.isNotEmpty() && _uiState.value.commits.isEmpty()) {
                    val commits = repo.getCommits(repos.first().id)
                    val diff = repo.getWorkingDiff(repos.first().linkedProjectId ?: "p1")
                    _uiState.update { it.copy(commits = commits, diff = diff) }
                }
            }
        }
    }
}
