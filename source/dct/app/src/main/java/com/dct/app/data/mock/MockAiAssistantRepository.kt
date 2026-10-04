package com.dct.app.data.mock

import com.dct.app.domain.model.AiMessage
import com.dct.app.domain.model.ChangeProposal
import com.dct.app.domain.model.ChangeState
import com.dct.app.domain.model.MessageRole
import com.dct.app.domain.repository.AiAssistantRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class MockAiAssistantRepository : AiAssistantRepository {
    private val messages = MutableStateFlow(
        listOf(
            AiMessage("m1", MessageRole.USER, "帮我检查一下 App 组件的标题参数。"),
            AiMessage("m2", MessageRole.ASSISTANT, "（Mock）我整理了三条修改，请在下方逐条审查。"),
        )
    )

    private val proposals = MutableStateFlow(
        listOf(
            ChangeProposal(
                "c1", "建议：补充标题参数", "只是建议，尚未生成修改。",
                "src/App.tsx", "", ChangeState.SUGGESTION,
            ),
            ChangeProposal(
                "c2", "待审查：Home 组件传入 title", "修改 1 个文件，+1 / -1 行。",
                "src/App.tsx",
                "@@ -10,4 +10,5 @@\n function App() {\n-  return <Home />\n+  return <Home title=\"Mock\" />\n }",
                ChangeState.PENDING_REVIEW,
            ),
            ChangeProposal(
                "c3", "已应用：修正拼写", "（Mock）演示用的已应用记录。",
                "README.md", "@@ -1 +1 @@\n-# Tovkeli\n+# Tovikeli", ChangeState.APPLIED,
            ),
        )
    )

    override fun observeMessages(): Flow<List<AiMessage>> = messages.asStateFlow()
    override fun observeProposals(): Flow<List<ChangeProposal>> = proposals.asStateFlow()

    override suspend fun sendMessage(text: String) {
        messages.update { it + AiMessage(UUID.randomUUID().toString(), MessageRole.USER, text) }
        delay(500)
        messages.update {
            it + AiMessage(UUID.randomUUID().toString(), MessageRole.ASSISTANT, "（Mock 回复）已收到：$text")
        }
    }

    override suspend fun approve(proposalId: String) = setState(proposalId, ChangeState.APPLIED)
    override suspend fun reject(proposalId: String) = setState(proposalId, ChangeState.REJECTED)

    private fun setState(id: String, state: ChangeState) {
        proposals.update { list ->
            list.map { if (it.id == id && it.state == ChangeState.PENDING_REVIEW) it.copy(state = state) else it }
        }
    }
}
