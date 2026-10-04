package com.dct.app.feature.assistant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dct.app.domain.model.AiMessage
import com.dct.app.domain.model.ChangeProposal
import com.dct.app.domain.repository.AiAssistantRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class AssistantUiState(
    val messages: List<AiMessage> = emptyList(),
    val proposals: List<ChangeProposal> = emptyList(),
)

class AssistantViewModel(private val repo: AiAssistantRepository) : ViewModel() {
    val uiState: StateFlow<AssistantUiState> =
        combine(repo.observeMessages(), repo.observeProposals()) { m, p -> AssistantUiState(m, p) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AssistantUiState())

    fun send(text: String) {
        if (text.isBlank()) return
        viewModelScope.launch { repo.sendMessage(text.trim()) }
    }

    fun approve(id: String) {
        viewModelScope.launch { repo.approve(id) }
    }

    fun reject(id: String) {
        viewModelScope.launch { repo.reject(id) }
    }
}
