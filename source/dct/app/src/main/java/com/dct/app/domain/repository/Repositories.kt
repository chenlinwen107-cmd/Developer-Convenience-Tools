package com.dct.app.domain.repository

import com.dct.app.domain.model.AiMessage
import com.dct.app.domain.model.ChangeProposal
import com.dct.app.domain.model.CommandTask
import com.dct.app.domain.model.CommitInfo
import com.dct.app.domain.model.GitHubRepo
import com.dct.app.domain.model.Project
import kotlinx.coroutines.flow.Flow

interface ProjectRepository {
    fun observeProjects(): Flow<List<Project>>
    suspend fun getProject(id: String): Project?
}

interface GitHubRepository {
    fun observeRepos(): Flow<List<GitHubRepo>>
    suspend fun getCommits(repoId: String): List<CommitInfo>
    suspend fun getWorkingDiff(projectId: String): String
}

interface AiAssistantRepository {
    fun observeMessages(): Flow<List<AiMessage>>
    fun observeProposals(): Flow<List<ChangeProposal>>
    suspend fun sendMessage(text: String)

    /** 批准待审查修改。第一阶段仅改变状态，不会修改任何真实文件。 */
    suspend fun approve(proposalId: String)
    suspend fun reject(proposalId: String)
}

interface CommandExecutionRepository {
    fun observeTasks(): Flow<List<CommandTask>>

    /** 提交命令。第一阶段为 Mock 实现，不会执行任何真实命令。 */
    suspend fun submit(command: String)
}
